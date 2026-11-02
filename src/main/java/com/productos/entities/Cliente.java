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
import java.util.List;

@Entity
@Table(name = "tb_cliente")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cli_id")
    private Integer id;

    @Column(name = "cli_nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "cli_direccion", length = 150)
    private String direccion;

    @Column(name = "cli_telefono", length = 10)
    private String telefono;

    @Column(name = "cli_cedula", nullable = false, length = 10, unique = true)
    private String cedula;

    @OneToMany(mappedBy = "cliente")
    private List<Factura> facturas;
}
