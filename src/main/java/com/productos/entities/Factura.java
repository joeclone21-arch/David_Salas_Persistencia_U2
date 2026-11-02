/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.productos.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_factura")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fac_id")
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "fac_fecha", nullable = false)
    private Date fecha;

    @Column(name = "fac_subtotal")
    private Double subtotal;

    @Column(name = "fac_iva")
    private Double iva;

    @Column(name = "fac_descuento")
    private Double descuento;

    @Column(name = "fac_valor_total")
    private Double valorTotal;

    @ManyToOne
    @JoinColumn(name = "cli_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<DetalleFactura> detalles;
}
