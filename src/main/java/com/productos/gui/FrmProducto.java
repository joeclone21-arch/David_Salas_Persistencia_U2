package com.productos.gui;

import com.producto.dao.ProductoDAO;
import com.productos.entities.Categoria;
import com.productos.entities.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;  

public class FrmProducto extends JFrame {

    private JTextField txtId, txtCodigo, txtNombre, txtTalla, txtCantidad, txtPrecio;
    private JComboBox<String> cbDepartamento, cbCategoria, cbSubcategoria;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private ProductoDAO productoDAO;
    private JComboBox<String> cbFiltrarCategoria; // ComboBox para filtrar busq x categoria

    public FrmProducto() {
        productoDAO = new ProductoDAO();
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Roland Ecuador - Gestión de Productos (CRUD)");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel de Formulario ---
        JPanel panelForm = new JPanel(new GridLayout(9, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));

        panelForm.add(new JLabel("ID (Autogenerado):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelForm.add(txtId);

        panelForm.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        panelForm.add(txtCodigo);

        panelForm.add(new JLabel("Nombre Producto:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Departamento:"));
        cbDepartamento = new JComboBox<>(new String[]{"Hombre", "Mujer", "Medias", "Beauty", "Accesorios"});
        panelForm.add(cbDepartamento);

        panelForm.add(new JLabel("Categoría:"));
        cbCategoria = new JComboBox<>(new String[]{"Ropa", "Medias", "Calzado", "Belleza", "Accesorios"});
        panelForm.add(cbCategoria);

        panelForm.add(new JLabel("Subcategoría:"));
        cbSubcategoria = new JComboBox<>(new String[]{"Pijamas", "Ropa casual", "Ropa at home", "Underwear", "Zapatos","Pantuflas", "Medias deportivas", "Cuidado Capilar"});
        panelForm.add(cbSubcategoria);

        panelForm.add(new JLabel("Talla:"));
        txtTalla = new JTextField();
        panelForm.add(txtTalla);

        panelForm.add(new JLabel("Cantidad (Stock):"));
        txtCantidad = new JTextField();
        panelForm.add(txtCantidad);

        panelForm.add(new JLabel("Precio ($):"));
        txtPrecio = new JTextField();
        panelForm.add(txtPrecio);

        // --- Panel de Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar Campos");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        
              
        // --- Panel de Filtro por JPQL ---
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltro.setBorder(BorderFactory.createTitledBorder("Filtrar Registros por Categoria (JPQL)"));
        panelFiltro.add(new JLabel("Seleccionar Categoria:"));
        // Opciones que coincidan con las de tu base de datos
        cbFiltrarCategoria = new JComboBox<>(new String[]{"-- Todas las categorias --", "Ropa", "Medias", "Calzado", "Belleza", "Accesorios"});
        panelFiltro.add(cbFiltrarCategoria);

        // Boton para restablecer la vista completa 
        JButton btnVerTodos = new JButton("Mostrar Todos");
        panelFiltro.add(btnVerTodos);
        
        JPanel panelSuperiorCompleto = new JPanel(new BorderLayout());
        panelSuperiorCompleto.add(panelSuperior, BorderLayout.CENTER);
        panelSuperiorCompleto.add(panelFiltro, BorderLayout.SOUTH);

        add(panelSuperiorCompleto, BorderLayout.NORTH);
        
        // Evento del ComboBox de filtro
        cbFiltrarCategoria.addActionListener(e -> {
        String seleccion = (String) cbFiltrarCategoria.getSelectedItem();
        if (seleccion != null && !seleccion.equals("-- Todas las categorías --")) {
        cargarTablaPorCategoria(seleccion);
        } else if (seleccion != null && seleccion.equals("-- Todas las categorías --")) {
        cargarTabla(); // Metodo original que lista todo
        }
        });

        // Evento del boton para limpiar el filtro
        btnVerTodos.addActionListener(e -> {
        cbFiltrarCategoria.setSelectedIndex(0);
        cargarTabla();
        });

        // --- Panel de Tabla ---
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Código", "Nombre", "Departamento", "Categoría", "Subcategoría", "Talla", "Stock", "Precio"}, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Catálogo de Productos en Base de Datos"));

        add(panelSuperiorCompleto, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

        // --- Eventos de Botones ---
        btnGuardar.addActionListener(e -> guardarProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // Cargar datos en los campos al seleccionar una fila de la tabla
        tablaProductos.getSelectionModel().addListSelectionListener(e -> seleccionarFila());
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Producto> lista = productoDAO.listarTodos();
        for (Producto p : lista) {
            // Verificamos que la categoría no sea nula antes de obtener su nombre
            String nombreCat = "";
            if (p.getCategoria() != null) {
                nombreCat = p.getCategoria().getNombre();
            }

            modeloTabla.addRow(new Object[]{
                p.getId(), 
                p.getCodigo(), 
                p.getNombre(), 
                p.getDepartamento(),
                nombreCat, // Se muestra el nombre de la entidad Categoria
                p.getSubcategoria(), 
                p.getTalla(), 
                p.getCantidad(), 
                p.getPrecio()
            });
        }
    }

    private void guardarProducto() {
        try {
            // Creamos la instancia del objeto Categoria asignándole el nombre seleccionado en el JComboBox
            Categoria categoriaObj = new Categoria();
            categoriaObj.setNombre(cbCategoria.getSelectedItem().toString());
            categoriaObj.setDescripcion("Categoría general del catálogo"); // Usando el setter correcto de la entidad Categoria

            Producto p = new Producto(
                txtCodigo.getText(),
                txtNombre.getText(),
                cbDepartamento.getSelectedItem().toString(),
                categoriaObj, // Pasamos el objeto Categoria en lugar de un String
                cbSubcategoria.getSelectedItem().toString(),
                txtTalla.getText(),
                Integer.parseInt(txtCantidad.getText()),
                Double.parseDouble(txtPrecio.getText())
            );
            
            productoDAO.guardar(p);
            JOptionPane.showMessageDialog(this, "¡Producto registrado en PostgreSQL!");
            cargarTabla();
            limpiarCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: Verifique que los campos numéricos sean válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void actualizarProducto() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para actualizar.");
            return;
        }
        try {
            Categoria categoriaObj = new Categoria();
            categoriaObj.setNombre(cbCategoria.getSelectedItem().toString());

            Producto p = new Producto(
                Integer.parseInt(txtId.getText()),
                txtCodigo.getText(),
                txtNombre.getText(),
                cbDepartamento.getSelectedItem().toString(),
                categoriaObj,
                cbSubcategoria.getSelectedItem().toString(),
                txtTalla.getText(),
                Integer.parseInt(txtCantidad.getText()),
                Double.parseDouble(txtPrecio.getText())
            );
            
            productoDAO.actualizar(p);
            JOptionPane.showMessageDialog(this, "¡Producto actualizado exitosamente!");
            cargarTabla();
            limpiarCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminarProducto() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productoDAO.eliminar(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "Producto eliminado.");
            cargarTabla();
            limpiarCampos();
        }
    }

    private void seleccionarFila() {
        int fila = tablaProductos.getSelectedRow();
        if (fila >= 0) {
            txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtCodigo.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 2).toString());
            cbDepartamento.setSelectedItem(modeloTabla.getValueAt(fila, 3).toString());
            cbCategoria.setSelectedItem(modeloTabla.getValueAt(fila, 4).toString());
            cbSubcategoria.setSelectedItem(modeloTabla.getValueAt(fila, 5).toString());
            txtTalla.setText(modeloTabla.getValueAt(fila, 6).toString());
            txtCantidad.setText(modeloTabla.getValueAt(fila, 7).toString());
            txtPrecio.setText(modeloTabla.getValueAt(fila, 8).toString());
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtCodigo.setText("");
        txtNombre.setText("");
        txtTalla.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        tablaProductos.clearSelection();
    }
    
    // Metodo para buscar y actualizar la JTable filtrando por categoría
    private void cargarTablaPorCategoria(String categoriaSeleccionada) {
    modeloTabla.setRowCount(0);
    List<Producto> lista = productoDAO.listarPorCategoria(categoriaSeleccionada);
    for (Producto p : lista) {
        String nombreCat = (p.getCategoria() != null) ? p.getCategoria().getNombre() : "";
        modeloTabla.addRow(new Object[]{
            p.getId(), 
            p.getCodigo(), 
            p.getNombre(), 
            p.getDepartamento(),
            nombreCat, 
            p.getSubcategoria(), 
            p.getTalla(), 
            p.getCantidad(), 
            p.getPrecio()
            });
        }
    }
}