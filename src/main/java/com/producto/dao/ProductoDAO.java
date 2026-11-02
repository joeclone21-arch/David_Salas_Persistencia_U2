/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.producto.dao;

import com.productos.entities.Categoria;
import com.productos.entities.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class ProductoDAO {

    private final EntityManagerFactory emf;

    public ProductoDAO() {
        // Usa el nombre exacto de la persistence-unit definido en persistence.xml
        this.emf = Persistence.createEntityManagerFactory("com.productos_Tarea1_DS_Roland_jar_1.0-SNAPSHOTPU");
    }

    // CREATE - Guardar un producto
    public void guardar(Producto producto) {
    EntityManager em = emf.createEntityManager();
    try {
        em.getTransaction().begin();

        // 1. Verificar si la categoría ya existe en la base de datos por su nombre
        String nombreCategoria = producto.getCategoria().getNombre();
        Categoria categoriaDb = null;
        
        try {
            categoriaDb = em.createQuery(
                "SELECT c FROM Categoria c WHERE c.nombre = :nombre", 
                Categoria.class)
                .setParameter("nombre", nombreCategoria)
                .getSingleResult();
        } catch (Exception e) {
            // Si no se encuentra, la consulta lanza excepción y categoriaDb queda en null
        }

        // 2. Si ya existe, asociamos esa entidad con su ID real; si no, la guardamos
        if (categoriaDb != null) {
            producto.setCategoria(categoriaDb);
        } else {
            em.persist(producto.getCategoria());
        }

        // 3. Guardar el producto con su categoría correctamente vinculada
        em.persist(producto);
        em.getTransaction().commit();
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        e.printStackTrace();
    } finally {
        em.close();
    }
}

    // READ - Obtener todos los productos para la tabla
    public List<Producto> listarTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Producto p ORDER BY p.id DESC", Producto.class).getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE - Actualizar un producto existente
    public void actualizar(Producto producto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(producto);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // DELETE - Eliminar por ID
    public void eliminar(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Producto p = em.find(Producto.class, id);
            if (p != null) {
                em.remove(p);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    // CONSULTAR PRODUCTOS POR CATEGORIA JPQL
    public List<Producto> listarPorCategoria(String nombreCategoria) {
    EntityManager em = emf.createEntityManager();
    try {
        // Consulta JPQL navegando por la relaciOn de la entidad Categoria
        String jpql = "SELECT p FROM Producto p JOIN p.categoria c WHERE c.nombre = :nombreCat";
        return em.createQuery(jpql, Producto.class)
                 .setParameter("nombreCat", nombreCategoria)
                 .getResultList();
    } finally {
        em.close();
    }
}
}
