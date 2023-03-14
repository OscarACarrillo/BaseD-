/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Modelo.Acceso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Login;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class AccesoJpaController implements Serializable {

    public AccesoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Acceso acceso) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Login usuario = acceso.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                acceso.setUsuario(usuario);
            }
            em.persist(acceso);
            if (usuario != null) {
                usuario.getAccesoList().add(acceso);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Acceso acceso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Acceso persistentAcceso = em.find(Acceso.class, acceso.getIdacceso());
            Login usuarioOld = persistentAcceso.getUsuario();
            Login usuarioNew = acceso.getUsuario();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                acceso.setUsuario(usuarioNew);
            }
            acceso = em.merge(acceso);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getAccesoList().remove(acceso);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getAccesoList().add(acceso);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = acceso.getIdacceso();
                if (findAcceso(id) == null) {
                    throw new NonexistentEntityException("The acceso with id " + id + " no longer exists.");
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
            Acceso acceso;
            try {
                acceso = em.getReference(Acceso.class, id);
                acceso.getIdacceso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The acceso with id " + id + " no longer exists.", enfe);
            }
            Login usuario = acceso.getUsuario();
            if (usuario != null) {
                usuario.getAccesoList().remove(acceso);
                usuario = em.merge(usuario);
            }
            em.remove(acceso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Acceso> findAccesoEntities() {
        return findAccesoEntities(true, -1, -1);
    }

    public List<Acceso> findAccesoEntities(int maxResults, int firstResult) {
        return findAccesoEntities(false, maxResults, firstResult);
    }

    private List<Acceso> findAccesoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Acceso.class));
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

    public Acceso findAcceso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Acceso.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccesoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Acceso> rt = cq.from(Acceso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
