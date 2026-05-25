package com.portable.microservices.ms_inventory.movement.infrastructure.persistence.adapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.entity.KardexJpaEntity;
import com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.repository.KardexJpaRepository;
import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity.LoteJpaEntity;
import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.repository.LoteJpaRepository;
import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.KardexPersistencePortOut;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.LotePersistencePortOut;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovimientoPersistencePortOut;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.entity.MovimientoJpaEntity;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.mapper.MovimientoPersistenceMapper;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.repository.MovimientoJpaRepository;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MovimientoPersistenceAdapter implements MovimientoPersistencePortOut, KardexPersistencePortOut, LotePersistencePortOut {

    private final MovimientoJpaRepository movimientoRepository;
    private final KardexJpaRepository kardexRepository;
    private final LoteJpaRepository loteRepository;
    private final MovimientoPersistenceMapper mapper;

    @Override
    
    public Movimiento save(@NonNull Movimiento movimiento) {
        // Obtener el lote para asociarlo
        LoteJpaEntity lote = loteRepository.findById( movimiento.idLote())
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado"));

        // Convertir a entity
        MovimientoJpaEntity entity = mapper.toEntity(movimiento);
        entity.setLote(lote);

        // Persistir
        MovimientoJpaEntity saved = movimientoRepository.save(entity);
        
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Movimiento> findById(@NonNull UUID id) {
        return movimientoRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Movimiento> findByIdLote(UUID idLote) {
        return movimientoRepository.findByLote_IdLote(idLote)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public KardexJpaEntity registrarEntrada(@NonNull UUID idMovimiento, UUID idProducto, Integer cantidad, BigDecimal costoProm) {
        // Obtener movimiento y producto
        MovimientoJpaEntity movimiento = movimientoRepository.findById(idMovimiento)
                .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado"));
        
        ProductJpaEntity producto = movimiento.getLote().getProducto();

        // Para el caso de registro de entrada simple sin historial de kardex por fecha,
        // consideramos stock anterior como 0 y el stock actual como la cantidad ingresada.
        Integer stockAnterior = 0;
        Integer stockActual = cantidad;

        // Crear registro en kardex
        KardexJpaEntity kardex = new KardexJpaEntity();
        kardex.setMovimiento(movimiento);
        kardex.setProducto(producto);
        kardex.setStockAnterior(stockAnterior);
        kardex.setCantIngreso(cantidad);
        kardex.setCantSalida(0);
        kardex.setStockActual(stockActual);
        kardex.setCostoProm(costoProm);

        return kardexRepository.save(kardex);
    }

    @Override
    public Optional<LoteJpaEntity> findLoteById(@NonNull UUID idLote) {
        return loteRepository.findById(idLote);
    }

    @Override
    public LoteJpaEntity update(@NonNull LoteJpaEntity lote) {
        return loteRepository.save(lote);
    }
}
