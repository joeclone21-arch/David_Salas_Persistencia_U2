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

@Entity
@Table(name = "tb_detalle_factura")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "det_id")
    private Integer id;

    @Column(name = "det_cantidad")
    private Double cantidad;

    @Column(name = "det_precio_venta")
    private Double precioVenta;

    @Column(name = "det_descuento_prod")
    private Double descuentoProd;

    @Column(name = "det_precio_final")
    private Double precioFinal;

    @ManyToOne
    @JoinColumn(name = "fac_id", nullable = false)
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "pr_id", nullable = false)
    private Producto producto;
}