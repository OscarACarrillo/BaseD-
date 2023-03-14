/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Modelo.Bajas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Producto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class BajasJpaController implements Serializable {

    public BajasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Bajas bajas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto = bajas.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getIdproducto());
                bajas.setProducto(producto);
            }
            em.persist(bajas);
            if (producto != null) {
                producto.getBajasList().add(bajas);
                producto = em.merge(producto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Bajas bajas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Bajas persistentBajas = em.find(Bajas.class, bajas.getIdbajas());
            Producto productoOld = persistentBajas.getProducto();
            Producto productoNew = bajas.getProducto();
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getIdproducto());
                bajas.setProducto(productoNew);
            }
            bajas = em.merge(bajas);
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getBajasList().remove(bajas);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getBajasList().add(bajas);
                productoNew = em.merge(productoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bajas.getIdbajas();
                if (findBajas(id) == null) {
                    throw new NonexistentEntityException("The bajas with id " + id + " no longer exists.");
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
            Bajas bajas;
            try {
                bajas = em.getReference(Bajas.class, id);
                bajas.getIdbajas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bajas with id " + id + " no longer exists.", enfe);
            }
            Producto producto = bajas.getProducto();
            if (producto != null) {
                producto.getBajasList().remove(bajas);
                producto = em.merge(producto);
            }
            em.remove(bajas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Bajas> findBajasEntities() {
        return findBajasEntities(true, -1, -1);
    }

    public List<Bajas> findBajasEntities(int maxResults, int firstResult) {
        return findBajasEntities(false, maxResults, firstResult);
    }

    private List<Bajas> findBajasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Bajas.class));
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

    public Bajas findBajas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Bajas.class, id);
        } finally {
            em.close();
        }
    }

    public int getBajasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Bajas> rt = cq.from(Bajas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
