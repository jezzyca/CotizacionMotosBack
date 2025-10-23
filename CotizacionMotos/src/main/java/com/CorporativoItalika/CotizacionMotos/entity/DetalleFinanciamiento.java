package com.CorporativoItalika.CotizacionMotos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "DETALLE_FINANCIAMIENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleFinanciamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cotizacion", nullable = false)
    private Cotizacion cotizacion;

    @Column(name = "plazo_meses", nullable = false)
    private Integer plazoMeses;

    @Column(name = "tasa_interes", precision = 5, scale = 2, nullable = false)
    private BigDecimal tasaInteres;

    @Column(name = "pago_mensual", precision = 12, scale = 2, nullable = false)
    private BigDecimal pagoMensual;

    @Column(name = "total_intereses", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalIntereses;

    @Column(name = "total_pagar", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalPagar;
}
