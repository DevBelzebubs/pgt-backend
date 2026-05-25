package com.portable.microservices.ms_inventory.movement.domain.ports.out;

import java.util.UUID;

import com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.entity.KardexJpaEntity;

public interface KardexPersistencePortOut {
    /**
     * Registra una entrada en el kardex
     * 
     * @param idMovimiento ID del movimiento
     * @param idProducto ID del producto
     * @param cantidad Cantidad que ingresa
     * @param costoProm Costo promedio unitario
     * @return El registro de kardex creado
     */
    KardexJpaEntity registrarEntrada(UUID idMovimiento, UUID idProducto, Integer cantidad, java.math.BigDecimal costoProm);
}
