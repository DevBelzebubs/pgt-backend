package com.portable.microservices.ms_inventory.alert.infrastructure.persistence.adapter;

import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.alert.domain.ports.out.ProductThresholdPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductThresholdAdapter implements ProductThresholdPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer getMinStockThreshold(UUID productId) {
        String sql = "SELECT stock_minimo FROM inventory.producto WHERE id_producto = ?";
        
        try {
            Integer minStock = jdbcTemplate.queryForObject(sql, Integer.class, productId);
            return minStock != null ? minStock : 5;
            
        } catch (Exception e) {
            log.warn("No se pudo encontrar el stock mínimo para el producto {}. Usando default (5).", productId);
            return 5;
        }
    }
}