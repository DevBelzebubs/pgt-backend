package com.portable.microservices.ms_inventory.movement.application.usecases;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.movement.domain.ports.in.ListMovementsPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovimientoPersistencePortOut;
import com.portable.microservices.ms_inventory.movement.infrastructure.messaging.service.UserNameService;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.entity.MovimientoJpaEntity;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.MovimientoListadoResponse;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.mapper.MovimientoWebMapper;
import com.portable.shared.infrastructure.presentation.PagedResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListMovementsUseCase implements ListMovementsPortIn {

    private final MovimientoPersistencePortOut persistence;
    private final MovimientoWebMapper mapper;
    private final UserNameService userNameService;

    @Override
    public PagedResponse<MovimientoListadoResponse> execute(String tipo, LocalDate fechaDesde, LocalDate fechaHasta,
            UUID idProducto, String texto, int pagina, int tamanioPagina) {
        String textoSeguro = (texto == null) ? "" : texto;
        var rows = persistence.findAllWithFilters(tipo, fechaDesde, fechaHasta, idProducto, textoSeguro, pagina, tamanioPagina);
        var userIds = rows.stream()
            .map(row -> ((MovimientoJpaEntity) row[0]).getIdUsuario())
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        var userNames = userNameService.resolveUserNames(userIds);
        List<MovimientoListadoResponse> items = rows.stream()
            .map(row -> mapper.toListadoResponse(row, userNames))
            .toList();
        long total = persistence.countAllWithFilters(tipo, fechaDesde, fechaHasta, idProducto, textoSeguro);
        return new PagedResponse<>(items, total, pagina, tamanioPagina);
    }
}
