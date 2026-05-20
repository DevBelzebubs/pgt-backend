package com.portable.microservices.ms_inventory.movement.infrastructure.persistence.entity;

import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity.LoteJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "inventory", name = "movimiento")
public class MovimientoJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lote", nullable = false)
    private LoteJpaEntity lote;

    @Column(nullable = false)
    private Long idUsuario;

    @Column(nullable = false, length = 20)
    private String tipo;

    @CreationTimestamp
    private OffsetDateTime fecha;

    @Column(length = 100)
    private String motivo;

    @Column(length = 100)
    private String docRef;
}