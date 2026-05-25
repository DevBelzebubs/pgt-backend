package com.portable.microservices.ms_inventory.movement.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.entity.MovimientoJpaEntity;

@Repository
public interface MovimientoJpaRepository extends JpaRepository<MovimientoJpaEntity, UUID> {

    /**
     * Busca todos los movimientos asociados a un lote.
     * Usa Lote_IdLote para navegar la relación @ManyToOne correctamente.
     */
    List<MovimientoJpaEntity> findByLote_IdLote(UUID idLote);
}
