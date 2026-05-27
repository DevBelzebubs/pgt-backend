package com.portable.microservices.ms_inventory.movement.domain.ports.out;

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
}
