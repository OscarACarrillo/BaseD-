/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Modelo.Muebles;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Procedencia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class MueblesJpaController implements Serializable {

    public MueblesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Muebles muebles) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Procedencia procedencia = muebles.getProcedencia();
            if (procedencia != null) {
                procedencia = em.getReference(procedencia.getClass(), procedencia.getIdprocedencia());
                muebles.setProcedencia(procedencia);
            }
            em.persist(muebles);
            if (procedencia != null) {
                procedencia.getMueblesList().add(muebles);
                procedencia = em.merge(procedencia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Muebles muebles) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Muebles persistentMuebles = em.find(Muebles.class, muebles.getIdmuebles());
            Procedencia procedenciaOld = persistentMuebles.getProcedencia();
            Procedencia procedenciaNew = muebles.getProcedencia();
            if (procedenciaNew != null) {
                procedenciaNew = em.getReference(procedenciaNew.getClass(), procedenciaNew.getIdprocedencia());
                muebles.setProcedencia(procedenciaNew);
            }
            muebles = em.merge(muebles);
            if (procedenciaOld != null && !procedenciaOld.equals(procedenciaNew)) {
                procedenciaOld.getMueblesList().remove(muebles);
                procedenciaOld = em.merge(procedenciaOld);
            }
            if (procedenciaNew != null && !procedenciaNew.equals(procedenciaOld)) {
                procedenciaNew.getMueblesList().add(muebles);
                procedenciaNew = em.merge(procedenciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = muebles.getIdmuebles();
                if (findMuebles(id) == null) {
                    throw new NonexistentEntityException("The muebles with id " + id + " no longer exists.");
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
            Muebles muebles;
            try {
                muebles = em.getReference(Muebles.class, id);
                muebles.getIdmuebles();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The muebles with id " + id + " no longer exists.", enfe);
            }
            Procedencia procedencia = muebles.getProcedencia();
            if (procedencia != null) {
                procedencia.getMueblesList().remove(muebles);
                procedencia = em.merge(procedencia);
            }
            em.remove(muebles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Muebles> findMueblesEntities() {
        return findMueblesEntities(true, -1, -1);
    }

    public List<Muebles> findMueblesEntities(int maxResults, int firstResult) {
        return findMueblesEntities(false, maxResults, firstResult);
    }

    private List<Muebles> findMueblesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Muebles.class));
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

    public Muebles findMuebles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Muebles.class, id);
        } finally {
            em.close();
        }
    }

    public int getMueblesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Muebles> rt = cq.from(Muebles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
