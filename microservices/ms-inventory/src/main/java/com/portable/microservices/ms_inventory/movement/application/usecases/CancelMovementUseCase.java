package com.portable.microservices.ms_inventory.movement.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity.LoteJpaEntity;
import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.CancelMovementPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterMovementPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterMovementPortIn.RegisterMovementCommand;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.LotePersistencePortOut;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovimientoPersistencePortOut;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CancelMovementUseCase implements CancelMovementPortIn {

    private final MovimientoPersistencePortOut movimientoPersistence;
    private final LotePersistencePortOut lotePersistence;
    private final RegisterMovementPortIn registerMovementUseCase;

    @Override
    @Transactional
    public void execute(UUID id) {
        Movimiento mov = movimientoPersistence.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado: " + id));

        String tipo = mov.tipo();
        String opuesto;
        if ("INGRESO".equals(tipo) || "AJUSTE_POSITIVO".equals(tipo)) {
            opuesto = "SALIDA";
        } else if ("SALIDA".equals(tipo) || "EGRESO".equals(tipo) || "AJUSTE_NEGATIVO".equals(tipo)) {
            opuesto = "INGRESO";
        } else {
            throw new IllegalArgumentException("Tipo de movimiento no soportado para anulación: " + tipo);
        }

        LoteJpaEntity lote = lotePersistence.findLoteById(mov.idLote())
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado: " + mov.idLote()));

        // Si la reversión es INGRESO (cancelar SALIDA), restaurar el lote manualmente
        // RegisterMovementUseCase no modifica lotes en INGRESO cuando lotId != null
        if ("INGRESO".equals(opuesto) || "AJUSTE_POSITIVO".equals(opuesto)) {
            lote.setCantidad(lote.getCantidad() + mov.cantidad());
            if (lote.getCantidad() > 0) {
                lote.setEstado("DISPONIBLE");
            }
            lotePersistence.update(lote);
        }

        RegisterMovementCommand cmd = new RegisterMovementCommand(
                opuesto,
                lote.getProducto().getId_producto(),
                mov.idLote(),
                null,
                mov.cantidad(),
                "Anulación del movimiento " + id,
                "ANULA-" + id.toString().substring(0, 8).toUpperCase(),
                null, null, lote.getCostoUnit(), null, mov.idUsuario());

        registerMovementUseCase.execute(cmd);
        log.info("Movimiento {} anulado. Reversión tipo {} creada.", id, opuesto);
    }
}
