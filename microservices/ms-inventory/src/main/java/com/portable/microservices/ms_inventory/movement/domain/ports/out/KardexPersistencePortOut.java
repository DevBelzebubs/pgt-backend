package com.portable.microservices.ms_inventory.movement.domain.ports.out;

import java.util.Optional;
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

    /**
     * Registra una salida en el kardex
     * 
     * @param idMovimiento ID del movimiento
     * @param idProducto ID del producto
     * @param cantidad Cantidad que sale
     * @param costoProm Costo promedio unitario
     * @return El registro de kardex creado
     */
    KardexJpaEntity registrarSalida(UUID idMovimiento, UUID idProducto, Integer cantidad, java.math.BigDecimal costoProm);

    /**
     * Obtiene el stock actual de un producto
     * 
     * @param idProducto ID del producto
     * @return Cantidad de stock disponible
     */

    Integer getStockActual(UUID idProducto);
    Optional<KardexJpaEntity> findLastByProductId(UUID idProducto);
}
