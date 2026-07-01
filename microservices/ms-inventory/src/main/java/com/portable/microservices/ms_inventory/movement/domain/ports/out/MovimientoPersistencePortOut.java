package com.portable.microservices.ms_inventory.movement.domain.ports.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;

public interface MovimientoPersistencePortOut {
    Movimiento save(Movimiento movimiento);

    Optional<Movimiento> findById(UUID id);

    List<Movimiento> findByIdLote(UUID idLote);

    List<Object[]> findAllWithFilters(String tipo, LocalDate fechaDesde, LocalDate fechaHasta,
                                       UUID idProducto, String texto, int pagina, int tamanioPagina);

    long countAllWithFilters(String tipo, LocalDate fechaDesde, LocalDate fechaHasta,
                              UUID idProducto, String texto);

    Optional<Object[]> findByIdWithDetails(UUID id);

    void deleteById(UUID id);
}
