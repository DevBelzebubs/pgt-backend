package com.portable.microservices.ms_inventory.kardex.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;

@Service
public class CostoPromedioCalculator {
    private static final int ESCALA = 4;
    private static final RoundingMode REDONDEO = RoundingMode.HALF_UP;
    public ResultadoCalculoPPP calcularParaIngreso(
        Optional<Kardex> ultimoKardex,
        Integer cantidadIngreso,
        BigDecimal costoUnitLote
    ) {
        int stockAnterior = ultimoKardex.map(Kardex::stockActual).orElse(0);
        BigDecimal costoPromAnterior = ultimoKardex.map(Kardex::costoProm).orElse(BigDecimal.ZERO);
        // Caso 1: NO hay historial
        if (stockAnterior == 0) {
            return new ResultadoCalculoPPP(
                0,
                costoUnitLote.setScale(ESCALA, REDONDEO),
                cantidadIngreso,
                costoUnitLote.setScale(ESCALA, REDONDEO)
            );
        }
        // Caso 2: SI hay historial
        BigDecimal valorAnterior = costoPromAnterior.multiply(BigDecimal.valueOf(stockAnterior));
        BigDecimal valorIngreso = costoUnitLote.multiply(BigDecimal.valueOf(cantidadIngreso));
        BigDecimal valorTotal = valorAnterior.add(valorIngreso);
        
        int stockTotal = stockAnterior + cantidadIngreso;
        BigDecimal nuevoCostoProm = valorTotal.divide(
            BigDecimal.valueOf(stockTotal),
            ESCALA,
            REDONDEO
        );
        return new ResultadoCalculoPPP(
            stockAnterior,
            costoPromAnterior,
            stockTotal,
            nuevoCostoProm
        );
    }

    public ResultadoCalculoPPP calcularParaSalida(
        Optional<Kardex> ultimoKardex,
        Integer cantidadSalida
    ) {
        int stockAnterior = ultimoKardex.map(Kardex::stockActual).orElse(0);
        BigDecimal costoPromVigente = ultimoKardex.map(Kardex::costoProm).orElse(BigDecimal.ZERO);
        int stockActual = Math.max(0, stockAnterior - cantidadSalida);
        return new ResultadoCalculoPPP(
            stockAnterior,
            costoPromVigente,
            stockActual,
            costoPromVigente
        );
    }
    public record ResultadoCalculoPPP(
        Integer stockAnterior,
        BigDecimal costoPromAnterior,
        Integer stockActual,
        BigDecimal costoPromNuevo
    ) {}
}