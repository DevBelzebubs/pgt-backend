package com.portable.microservices.ms_inventory.movement.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity.LoteJpaEntity;

public interface LotePersistencePortOut {
    /**
     * Obtiene un lote por su ID
     */
    Optional<LoteJpaEntity> findLoteById(UUID idLote);
    
    /**
     * Actualiza el estado de un lote
     */
    LoteJpaEntity update(LoteJpaEntity lote);

    /**
     * Obtiene los lotes asociados a un producto en una locación específica
     */
    List<LoteJpaEntity> findLotesByProductAndLocation(UUID idProducto, UUID idLocacion);

    /**
     * Obtiene los lotes asociados a un producto, ordenados por fecha de ingreso ASC (FIFO)
     */
    List<LoteJpaEntity> findLotesByProductId(UUID idProducto);
}
