/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Acceso;
import Modelo.Login;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class LoginJpaController implements Serializable {

    public LoginJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Login login) {
        if (login.getAccesoList() == null) {
            login.setAccesoList(new ArrayList<Acceso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Acceso> attachedAccesoList = new ArrayList<Acceso>();
            for (Acceso accesoListAccesoToAttach : login.getAccesoList()) {
                accesoListAccesoToAttach = em.getReference(accesoListAccesoToAttach.getClass(), accesoListAccesoToAttach.getIdacceso());
                attachedAccesoList.add(accesoListAccesoToAttach);
            }
            login.setAccesoList(attachedAccesoList);
            em.persist(login);
            for (Acceso accesoListAcceso : login.getAccesoList()) {
                Login oldUsuarioOfAccesoListAcceso = accesoListAcceso.getUsuario();
                accesoListAcceso.setUsuario(login);
                accesoListAcceso = em.merge(accesoListAcceso);
                if (oldUsuarioOfAccesoListAcceso != null) {
                    oldUsuarioOfAccesoListAcceso.getAccesoList().remove(accesoListAcceso);
                    oldUsuarioOfAccesoListAcceso = em.merge(oldUsuarioOfAccesoListAcceso);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Login login) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Login persistentLogin = em.find(Login.class, login.getId());
            List<Acceso> accesoListOld = persistentLogin.getAccesoList();
            List<Acceso> accesoListNew = login.getAccesoList();
            List<String> illegalOrphanMessages = null;
            for (Acceso accesoListOldAcceso : accesoListOld) {
                if (!accesoListNew.contains(accesoListOldAcceso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Acceso " + accesoListOldAcceso + " since its usuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Acceso> attachedAccesoListNew = new ArrayList<Acceso>();
            for (Acceso accesoListNewAccesoToAttach : accesoListNew) {
                accesoListNewAccesoToAttach = em.getReference(accesoListNewAccesoToAttach.getClass(), accesoListNewAccesoToAttach.getIdacceso());
                attachedAccesoListNew.add(accesoListNewAccesoToAttach);
            }
            accesoListNew = attachedAccesoListNew;
            login.setAccesoList(accesoListNew);
            login = em.merge(login);
            for (Acceso accesoListNewAcceso : accesoListNew) {
                if (!accesoListOld.contains(accesoListNewAcceso)) {
                    Login oldUsuarioOfAccesoListNewAcceso = accesoListNewAcceso.getUsuario();
                    accesoListNewAcceso.setUsuario(login);
                    accesoListNewAcceso = em.merge(accesoListNewAcceso);
                    if (oldUsuarioOfAccesoListNewAcceso != null && !oldUsuarioOfAccesoListNewAcceso.equals(login)) {
                        oldUsuarioOfAccesoListNewAcceso.getAccesoList().remove(accesoListNewAcceso);
                        oldUsuarioOfAccesoListNewAcceso = em.merge(oldUsuarioOfAccesoListNewAcceso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = login.getId();
                if (findLogin(id) == null) {
                    throw new NonexistentEntityException("The login with id " + id + " no longer exists.");
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
            Login login;
            try {
                login = em.getReference(Login.class, id);
                login.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The login with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Acceso> accesoListOrphanCheck = login.getAccesoList();
            for (Acceso accesoListOrphanCheckAcceso : accesoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Login (" + login + ") cannot be destroyed since the Acceso " + accesoListOrphanCheckAcceso + " in its accesoList field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(login);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Login> findLoginEntities() {
        return findLoginEntities(true, -1, -1);
    }

    public List<Login> findLoginEntities(int maxResults, int firstResult) {
        return findLoginEntities(false, maxResults, firstResult);
    }

    private List<Login> findLoginEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Login.class));
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

    public Login findLogin(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Login.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoginCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Login> rt = cq.from(Login.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
