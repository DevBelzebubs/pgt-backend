package com.portable.microservices.ms_inventory.kardex.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.microservices.ms_inventory.kardex.domain.model.MetodoCosto;
import com.portable.microservices.ms_inventory.kardex.domain.ports.out.LotPersistencePortOut;
import com.portable.microservices.ms_inventory.lot.domain.model.Lot;
import com.portable.microservices.ms_inventory.movement.domain.model.Movement;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KardexCostoSimulator {

    private final LotPersistencePortOut lotPersistence;

    private static final int ESCALA = 4;
    private static final RoundingMode REDONDEO = RoundingMode.HALF_UP;

    public Map<UUID, BigDecimal> calcularCostos(List<KardexEntry> entries, MetodoCosto metodo) {
        Map<UUID, BigDecimal> costos = new LinkedHashMap<>();
        if (entries.isEmpty())
            return costos;

        List<KardexEntry> sorted = entries.stream()
                .sorted(Comparator.comparing(KardexEntry::movement,
                        Comparator.nullsLast(Comparator.comparing(Movement::fecha,
                                Comparator.nullsLast(Comparator.naturalOrder())))))
                .toList();

        if (metodo == MetodoCosto.PPP) {
            for (KardexEntry e : sorted)
                costos.put(e.kardex().id(), e.kardex().costoProm());
            return costos;
        }

        Deque<LotEntry> inventory = new LinkedList<>();

        for (KardexEntry entry : sorted) {
            Kardex k = entry.kardex();
            if (k.cantIngreso() > 0) {
                Movement m = entry.movement();
                BigDecimal cu = (m != null && m.lotId() != null)
                        ? resolveCostoUnit(m)
                        : k.costoProm();
                inventory.addLast(new LotEntry(cu, k.cantIngreso()));
                costos.put(k.id(), cu);
            } else if (k.cantSalida() > 0) {
                int restante = k.cantSalida();
                BigDecimal total = BigDecimal.ZERO;
                while (restante > 0 && !inventory.isEmpty()) {
                    LotEntry l = (metodo == MetodoCosto.PEPS)
                            ? inventory.removeFirst()
                            : inventory.removeLast();
                    int toma = Math.min(restante, l.cantidad());
                    total = total.add(l.costoUnit().multiply(BigDecimal.valueOf(toma)));
                    restante -= toma;
                    if (toma < l.cantidad()) {
                        LotEntry rem = new LotEntry(l.costoUnit(), l.cantidad() - toma);
                        if (metodo == MetodoCosto.PEPS)
                            inventory.addFirst(rem);
                        else
                            inventory.addLast(rem);
                    }
                }
                BigDecimal cu = total.divide(BigDecimal.valueOf(k.cantSalida()), ESCALA, REDONDEO);
                costos.put(k.id(), cu);
            } else {
                costos.put(k.id(), k.costoProm());
            }
        }
        return costos;
    }

    private BigDecimal resolveCostoUnit(Movement m) {
        if (m == null || m.lotId() == null)
            return BigDecimal.ZERO;
        return lotPersistence.findById(m.lotId())
                .map(Lot::costoUnit).orElse(BigDecimal.ZERO);
    }

    public record KardexEntry(Kardex kardex, Movement movement) {
    }

    private record LotEntry(BigDecimal costoUnit, int cantidad) {
    }
}