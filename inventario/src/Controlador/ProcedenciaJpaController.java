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
import Modelo.Producto;
import java.util.ArrayList;
import java.util.List;
import Modelo.Muebles;
import Modelo.Procedencia;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class ProcedenciaJpaController implements Serializable {

    public ProcedenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Procedencia procedencia) {
        if (procedencia.getProductoList() == null) {
            procedencia.setProductoList(new ArrayList<Producto>());
        }
        if (procedencia.getMueblesList() == null) {
            procedencia.setMueblesList(new ArrayList<Muebles>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Producto> attachedProductoList = new ArrayList<Producto>();
            for (Producto productoListProductoToAttach : procedencia.getProductoList()) {
                productoListProductoToAttach = em.getReference(productoListProductoToAttach.getClass(), productoListProductoToAttach.getIdproducto());
                attachedProductoList.add(productoListProductoToAttach);
            }
            procedencia.setProductoList(attachedProductoList);
            List<Muebles> attachedMueblesList = new ArrayList<Muebles>();
            for (Muebles mueblesListMueblesToAttach : procedencia.getMueblesList()) {
                mueblesListMueblesToAttach = em.getReference(mueblesListMueblesToAttach.getClass(), mueblesListMueblesToAttach.getIdmuebles());
                attachedMueblesList.add(mueblesListMueblesToAttach);
            }
            procedencia.setMueblesList(attachedMueblesList);
            em.persist(procedencia);
            for (Producto productoListProducto : procedencia.getProductoList()) {
                Procedencia oldProcedenciaOfProductoListProducto = productoListProducto.getProcedencia();
                productoListProducto.setProcedencia(procedencia);
                productoListProducto = em.merge(productoListProducto);
                if (oldProcedenciaOfProductoListProducto != null) {
                    oldProcedenciaOfProductoListProducto.getProductoList().remove(productoListProducto);
                    oldProcedenciaOfProductoListProducto = em.merge(oldProcedenciaOfProductoListProducto);
                }
            }
            for (Muebles mueblesListMuebles : procedencia.getMueblesList()) {
                Procedencia oldProcedenciaOfMueblesListMuebles = mueblesListMuebles.getProcedencia();
                mueblesListMuebles.setProcedencia(procedencia);
                mueblesListMuebles = em.merge(mueblesListMuebles);
                if (oldProcedenciaOfMueblesListMuebles != null) {
                    oldProcedenciaOfMueblesListMuebles.getMueblesList().remove(mueblesListMuebles);
                    oldProcedenciaOfMueblesListMuebles = em.merge(oldProcedenciaOfMueblesListMuebles);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Procedencia procedencia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Procedencia persistentProcedencia = em.find(Procedencia.class, procedencia.getIdprocedencia());
            List<Producto> productoListOld = persistentProcedencia.getProductoList();
            List<Producto> productoListNew = procedencia.getProductoList();
            List<Muebles> mueblesListOld = persistentProcedencia.getMueblesList();
            List<Muebles> mueblesListNew = procedencia.getMueblesList();
            List<String> illegalOrphanMessages = null;
            for (Producto productoListOldProducto : productoListOld) {
                if (!productoListNew.contains(productoListOldProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Producto " + productoListOldProducto + " since its procedencia field is not nullable.");
                }
            }
            for (Muebles mueblesListOldMuebles : mueblesListOld) {
                if (!mueblesListNew.contains(mueblesListOldMuebles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Muebles " + mueblesListOldMuebles + " since its procedencia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Producto> attachedProductoListNew = new ArrayList<Producto>();
            for (Producto productoListNewProductoToAttach : productoListNew) {
                productoListNewProductoToAttach = em.getReference(productoListNewProductoToAttach.getClass(), productoListNewProductoToAttach.getIdproducto());
                attachedProductoListNew.add(productoListNewProductoToAttach);
            }
            productoListNew = attachedProductoListNew;
            procedencia.setProductoList(productoListNew);
            List<Muebles> attachedMueblesListNew = new ArrayList<Muebles>();
            for (Muebles mueblesListNewMueblesToAttach : mueblesListNew) {
                mueblesListNewMueblesToAttach = em.getReference(mueblesListNewMueblesToAttach.getClass(), mueblesListNewMueblesToAttach.getIdmuebles());
                attachedMueblesListNew.add(mueblesListNewMueblesToAttach);
            }
            mueblesListNew = attachedMueblesListNew;
            procedencia.setMueblesList(mueblesListNew);
            procedencia = em.merge(procedencia);
            for (Producto productoListNewProducto : productoListNew) {
                if (!productoListOld.contains(productoListNewProducto)) {
                    Procedencia oldProcedenciaOfProductoListNewProducto = productoListNewProducto.getProcedencia();
                    productoListNewProducto.setProcedencia(procedencia);
                    productoListNewProducto = em.merge(productoListNewProducto);
                    if (oldProcedenciaOfProductoListNewProducto != null && !oldProcedenciaOfProductoListNewProducto.equals(procedencia)) {
                        oldProcedenciaOfProductoListNewProducto.getProductoList().remove(productoListNewProducto);
                        oldProcedenciaOfProductoListNewProducto = em.merge(oldProcedenciaOfProductoListNewProducto);
                    }
                }
            }
            for (Muebles mueblesListNewMuebles : mueblesListNew) {
                if (!mueblesListOld.contains(mueblesListNewMuebles)) {
                    Procedencia oldProcedenciaOfMueblesListNewMuebles = mueblesListNewMuebles.getProcedencia();
                    mueblesListNewMuebles.setProcedencia(procedencia);
                    mueblesListNewMuebles = em.merge(mueblesListNewMuebles);
                    if (oldProcedenciaOfMueblesListNewMuebles != null && !oldProcedenciaOfMueblesListNewMuebles.equals(procedencia)) {
                        oldProcedenciaOfMueblesListNewMuebles.getMueblesList().remove(mueblesListNewMuebles);
                        oldProcedenciaOfMueblesListNewMuebles = em.merge(oldProcedenciaOfMueblesListNewMuebles);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = procedencia.getIdprocedencia();
                if (findProcedencia(id) == null) {
                    throw new NonexistentEntityException("The procedencia with id " + id + " no longer exists.");
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
            Procedencia procedencia;
            try {
                procedencia = em.getReference(Procedencia.class, id);
                procedencia.getIdprocedencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The procedencia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Producto> productoListOrphanCheck = procedencia.getProductoList();
            for (Producto productoListOrphanCheckProducto : productoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Procedencia (" + procedencia + ") cannot be destroyed since the Producto " + productoListOrphanCheckProducto + " in its productoList field has a non-nullable procedencia field.");
            }
            List<Muebles> mueblesListOrphanCheck = procedencia.getMueblesList();
            for (Muebles mueblesListOrphanCheckMuebles : mueblesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Procedencia (" + procedencia + ") cannot be destroyed since the Muebles " + mueblesListOrphanCheckMuebles + " in its mueblesList field has a non-nullable procedencia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(procedencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Procedencia> findProcedenciaEntities() {
        return findProcedenciaEntities(true, -1, -1);
    }

    public List<Procedencia> findProcedenciaEntities(int maxResults, int firstResult) {
        return findProcedenciaEntities(false, maxResults, firstResult);
    }

    private List<Procedencia> findProcedenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Procedencia.class));
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

    public Procedencia findProcedencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Procedencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getProcedenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Procedencia> rt = cq.from(Procedencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
