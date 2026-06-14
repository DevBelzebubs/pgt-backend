package com.portable.microservices.ms_tracking.picking.application.usecases;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_tracking.picking.domain.model.DetallePick;
import com.portable.microservices.ms_tracking.picking.domain.model.RutaPick;
import com.portable.microservices.ms_tracking.picking.domain.ports.in.OptimizarRutaPortIn;
import com.portable.microservices.ms_tracking.picking.domain.ports.out.OrdenPickPersistencePortOut;
import com.portable.microservices.ms_tracking.picking.domain.ports.out.RutaPickPersistencePortOut;
import com.portable.microservices.ms_tracking.picking.domain.service.ConstructorGrafoService;
import com.portable.microservices.ms_tracking.picking.domain.service.OptimizadorRutaService;
import com.portable.microservices.ms_tracking.picking.infrastructure.client.InventoryFeignClient;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptimizarRutaUseCase implements OptimizarRutaPortIn {

    private final OrdenPickPersistencePortOut ordenPickPersistence;
    private final RutaPickPersistencePortOut rutaPickPersistence;
    private final InventoryFeignClient inventoryFeignClient;
    private final ConstructorGrafoService constructorGrafo;
    private final OptimizadorRutaService optimizador;

    @Override
    @Transactional
    public RutaPick execute(UUID idOrden) {
        var orden = ordenPickPersistence.findById(idOrden)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + idOrden));

        var locationResponse = inventoryFeignClient.getAllLocations();
        var locations = locationResponse.getData().stream()
                .map(loc -> new ConstructorGrafoService.LocationData(
                        loc.getIdLocacion(), loc.getIdAlmacen(),
                        loc.getZona(), loc.getPasillo(), loc.getEstante()))
                .toList();

        var grafo = constructorGrafo.construir(locations, 1L);

        List<UUID> locacionesRecoger = ordenPickPersistence.findDetallesByIdOrden(idOrden)
                .stream()
                .map(DetallePick::locacionId)
                .distinct()
                .toList();

        var rutaOptima = optimizador.optimizar(grafo, locacionesRecoger);

        var ruta = RutaPick.builder()
                .idRuta(UUID.randomUUID())
                .idOrden(idOrden)
                .pathSeq(rutaOptima.pathCompleto())
                .distanciaEstimada(rutaOptima.distanciaTotal())
                .fecCreacion(OffsetDateTime.now())
                .build();

        return rutaPickPersistence.save(ruta);
    }

}
