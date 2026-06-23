package com.portable.microservices.ms_tracking.picking.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_tracking.picking.domain.model.AristaGrafo;
import com.portable.microservices.ms_tracking.picking.domain.model.GrafoAlmacen;
import com.portable.microservices.ms_tracking.picking.domain.model.NodoGrafo;

@Component
public class ConstructorGrafoService {
    public record LocationData(UUID idLocacion, Long idAlmacen, String zona, String pasillo, String estante) {
    }

    public GrafoAlmacen construir(List<LocationData> locations, Long warehouseId) {
        var locs = locations.stream()
                .filter(loc -> loc.idAlmacen.equals(warehouseId))
                .toList();
        List<NodoGrafo> nodos = new ArrayList<>();
        List<AristaGrafo> aristas = new ArrayList<>();

        UUID entradaId = UUID.randomUUID();
        nodos.add(new NodoGrafo(entradaId, "Entrada", "", "", "Entrada"));
        UUID salidaId = UUID.randomUUID();
        nodos.add(new NodoGrafo(salidaId, "Salida", "", "", "Salida"));
        Map<String, Map<String, List<LocationData>>> grouped = locs.stream()
                .collect(Collectors.groupingBy(LocationData::zona,
                        Collectors.groupingBy(LocationData::pasillo, TreeMap::new, Collectors.toList())));
        List<NodoGrafo> inicioPasillos = new ArrayList<>();
        for (var zonaEntry : grouped.entrySet()) {
            String zona = zonaEntry.getKey();
            var pasillos = zonaEntry.getValue();
            for (var pasilloEntry : pasillos.entrySet()) {
                String pasillo = pasilloEntry.getKey();
                var estantes = pasilloEntry.getValue();
                estantes.sort(Comparator.comparing(LocationData::estante));
                List<NodoGrafo> nodosPasillo = new ArrayList<>();
                for (var loc : estantes) {
                    NodoGrafo nodo = new NodoGrafo(loc.idLocacion, zona, pasillo, loc.estante, "Estante");
                    nodos.add(nodo);
                    nodosPasillo.add(nodo);
                }
                for (int i = 0; i < nodosPasillo.size() - 1; i++) {
                    NodoGrafo nodoActual = nodosPasillo.get(i);
                    NodoGrafo nodoSiguiente = nodosPasillo.get(i + 1);
                    BigDecimal peso = BigDecimal.ONE;
                    aristas.add(new AristaGrafo(nodoActual.idNodo(), nodoSiguiente.idNodo(), peso));
                    aristas.add(new AristaGrafo(nodoSiguiente.idNodo(), nodoActual.idNodo(), peso));
                }
                nodos.addAll(nodosPasillo);
                if (!nodosPasillo.isEmpty()) {
                    inicioPasillos.add(nodosPasillo.getFirst());
                }
            }
        }
        if (!inicioPasillos.isEmpty()) {
            aristas.add(new AristaGrafo(entradaId, inicioPasillos.get(0).idNodo(), BigDecimal.ONE));
        }
        for (int i = 0; i < inicioPasillos.size() - 1; i++) {
            UUID finA = inicioPasillos.get(i).idNodo();
            UUID finB = inicioPasillos.get(i + 1).idNodo();
            BigDecimal peso = BigDecimal.valueOf(2);
            aristas.add(new AristaGrafo(finA, finB, peso));
            aristas.add(new AristaGrafo(finB, finA, peso));
        }
        if (!inicioPasillos.isEmpty()) {
            aristas.add(new AristaGrafo(inicioPasillos.getLast().idNodo(), salidaId, BigDecimal.ONE));
            aristas.add(new AristaGrafo(salidaId, inicioPasillos.getLast().idNodo(), BigDecimal.ONE));
        }
        nodos.addAll(Arrays.asList(
            new NodoGrafo(entradaId, "Entrada", "", "", "Entrada"),
                new NodoGrafo(salidaId, "Salida", "", "", "Salida")
            )
        );
        return new GrafoAlmacen(nodos, aristas);
    }
}
