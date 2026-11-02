/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.productos.tarea1_ds_roland;

import com.productos.gui.FrmProducto;
import javax.swing.SwingUtilities;

public class Tarea1_DS_Roland {

    public static void main(String[] args) {
        // Ejecutar la interfaz gráfica Java Swing para el CRUD de Roland de Ecuador
        SwingUtilities.invokeLater(() -> {
            new FrmProducto().setVisible(true);
        });
    }
}