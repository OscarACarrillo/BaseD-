/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Modelo.Destino;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Prestamo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class DestinoJpaController implements Serializable {

    public DestinoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Destino destino) {
        if (destino.getPrestamoList() == null) {
            destino.setPrestamoList(new ArrayList<Prestamo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Prestamo> attachedPrestamoList = new ArrayList<Prestamo>();
            for (Prestamo prestamoListPrestamoToAttach : destino.getPrestamoList()) {
                prestamoListPrestamoToAttach = em.getReference(prestamoListPrestamoToAttach.getClass(), prestamoListPrestamoToAttach.getIdprestamo());
                attachedPrestamoList.add(prestamoListPrestamoToAttach);
            }
            destino.setPrestamoList(attachedPrestamoList);
            em.persist(destino);
            for (Prestamo prestamoListPrestamo : destino.getPrestamoList()) {
                Destino oldDestinoOfPrestamoListPrestamo = prestamoListPrestamo.getDestino();
                prestamoListPrestamo.setDestino(destino);
                prestamoListPrestamo = em.merge(prestamoListPrestamo);
                if (oldDestinoOfPrestamoListPrestamo != null) {
                    oldDestinoOfPrestamoListPrestamo.getPrestamoList().remove(prestamoListPrestamo);
                    oldDestinoOfPrestamoListPrestamo = em.merge(oldDestinoOfPrestamoListPrestamo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Destino destino) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Destino persistentDestino = em.find(Destino.class, destino.getIddestino());
            List<Prestamo> prestamoListOld = persistentDestino.getPrestamoList();
            List<Prestamo> prestamoListNew = destino.getPrestamoList();
            List<String> illegalOrphanMessages = null;
            for (Prestamo prestamoListOldPrestamo : prestamoListOld) {
                if (!prestamoListNew.contains(prestamoListOldPrestamo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Prestamo " + prestamoListOldPrestamo + " since its destino field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Prestamo> attachedPrestamoListNew = new ArrayList<Prestamo>();
            for (Prestamo prestamoListNewPrestamoToAttach : prestamoListNew) {
                prestamoListNewPrestamoToAttach = em.getReference(prestamoListNewPrestamoToAttach.getClass(), prestamoListNewPrestamoToAttach.getIdprestamo());
                attachedPrestamoListNew.add(prestamoListNewPrestamoToAttach);
            }
            prestamoListNew = attachedPrestamoListNew;
            destino.setPrestamoList(prestamoListNew);
            destino = em.merge(destino);
            for (Prestamo prestamoListNewPrestamo : prestamoListNew) {
                if (!prestamoListOld.contains(prestamoListNewPrestamo)) {
                    Destino oldDestinoOfPrestamoListNewPrestamo = prestamoListNewPrestamo.getDestino();
                    prestamoListNewPrestamo.setDestino(destino);
                    prestamoListNewPrestamo = em.merge(prestamoListNewPrestamo);
                    if (oldDestinoOfPrestamoListNewPrestamo != null && !oldDestinoOfPrestamoListNewPrestamo.equals(destino)) {
                        oldDestinoOfPrestamoListNewPrestamo.getPrestamoList().remove(prestamoListNewPrestamo);
                        oldDestinoOfPrestamoListNewPrestamo = em.merge(oldDestinoOfPrestamoListNewPrestamo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = destino.getIddestino();
                if (findDestino(id) == null) {
                    throw new NonexistentEntityException("The destino with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Destino destino;
            try {
                destino = em.getReference(Destino.class, id);
                destino.getIddestino();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The destino with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Prestamo> prestamoListOrphanCheck = destino.getPrestamoList();
            for (Prestamo prestamoListOrphanCheckPrestamo : prestamoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Destino (" + destino + ") cannot be destroyed since the Prestamo " + prestamoListOrphanCheckPrestamo + " in its prestamoList field has a non-nullable destino field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(destino);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Destino> findDestinoEntities() {
        return findDestinoEntities(true, -1, -1);
    }

    public List<Destino> findDestinoEntities(int maxResults, int firstResult) {
        return findDestinoEntities(false, maxResults, firstResult);
    }

    private List<Destino> findDestinoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Destino.class));
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

    public Destino findDestino(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Destino.class, id);
        } finally {
            em.close();
        }
    }

    public int getDestinoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Destino> rt = cq.from(Destino.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
