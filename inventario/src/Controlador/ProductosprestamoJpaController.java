/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Producto;
import Modelo.Productosprestamo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class ProductosprestamoJpaController implements Serializable {

    public ProductosprestamoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Productosprestamo productosprestamo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto = productosprestamo.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getIdproducto());
                productosprestamo.setProducto(producto);
            }
            em.persist(productosprestamo);
            if (producto != null) {
                producto.getProductosprestamoList().add(productosprestamo);
                producto = em.merge(producto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Productosprestamo productosprestamo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productosprestamo persistentProductosprestamo = em.find(Productosprestamo.class, productosprestamo.getIdproductosprestamo());
            Producto productoOld = persistentProductosprestamo.getProducto();
            Producto productoNew = productosprestamo.getProducto();
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getIdproducto());
                productosprestamo.setProducto(productoNew);
            }
            productosprestamo = em.merge(productosprestamo);
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getProductosprestamoList().remove(productosprestamo);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getProductosprestamoList().add(productosprestamo);
                productoNew = em.merge(productoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = productosprestamo.getIdproductosprestamo();
                if (findProductosprestamo(id) == null) {
                    throw new NonexistentEntityException("The productosprestamo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productosprestamo productosprestamo;
            try {
                productosprestamo = em.getReference(Productosprestamo.class, id);
                productosprestamo.getIdproductosprestamo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productosprestamo with id " + id + " no longer exists.", enfe);
            }
            Producto producto = productosprestamo.getProducto();
            if (producto != null) {
                producto.getProductosprestamoList().remove(productosprestamo);
                producto = em.merge(producto);
            }
            em.remove(productosprestamo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Productosprestamo> findProductosprestamoEntities() {
        return findProductosprestamoEntities(true, -1, -1);
    }

    public List<Productosprestamo> findProductosprestamoEntities(int maxResults, int firstResult) {
        return findProductosprestamoEntities(false, maxResults, firstResult);
    }

    private List<Productosprestamo> findProductosprestamoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Productosprestamo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Productosprestamo findProductosprestamo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Productosprestamo.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductosprestamoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Productosprestamo> rt = cq.from(Productosprestamo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
