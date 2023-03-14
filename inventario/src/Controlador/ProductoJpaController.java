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
import Modelo.Estado;
import Modelo.Procedencia;
import Modelo.Productosprestamo;
import java.util.ArrayList;
import java.util.List;
import Modelo.Bajas;
import Modelo.Producto;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) {
        if (producto.getProductosprestamoList() == null) {
            producto.setProductosprestamoList(new ArrayList<Productosprestamo>());
        }
        if (producto.getBajasList() == null) {
            producto.setBajasList(new ArrayList<Bajas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado estado = producto.getEstado();
            if (estado != null) {
                estado = em.getReference(estado.getClass(), estado.getIdestado());
                producto.setEstado(estado);
            }
            Procedencia procedencia = producto.getProcedencia();
            if (procedencia != null) {
                procedencia = em.getReference(procedencia.getClass(), procedencia.getIdprocedencia());
                producto.setProcedencia(procedencia);
            }
            List<Productosprestamo> attachedProductosprestamoList = new ArrayList<Productosprestamo>();
            for (Productosprestamo productosprestamoListProductosprestamoToAttach : producto.getProductosprestamoList()) {
                productosprestamoListProductosprestamoToAttach = em.getReference(productosprestamoListProductosprestamoToAttach.getClass(), productosprestamoListProductosprestamoToAttach.getIdproductosprestamo());
                attachedProductosprestamoList.add(productosprestamoListProductosprestamoToAttach);
            }
            producto.setProductosprestamoList(attachedProductosprestamoList);
            List<Bajas> attachedBajasList = new ArrayList<Bajas>();
            for (Bajas bajasListBajasToAttach : producto.getBajasList()) {
                bajasListBajasToAttach = em.getReference(bajasListBajasToAttach.getClass(), bajasListBajasToAttach.getIdbajas());
                attachedBajasList.add(bajasListBajasToAttach);
            }
            producto.setBajasList(attachedBajasList);
            em.persist(producto);
            if (estado != null) {
                estado.getProductoList().add(producto);
                estado = em.merge(estado);
            }
            if (procedencia != null) {
                procedencia.getProductoList().add(producto);
                procedencia = em.merge(procedencia);
            }
            for (Productosprestamo productosprestamoListProductosprestamo : producto.getProductosprestamoList()) {
                Producto oldProductoOfProductosprestamoListProductosprestamo = productosprestamoListProductosprestamo.getProducto();
                productosprestamoListProductosprestamo.setProducto(producto);
                productosprestamoListProductosprestamo = em.merge(productosprestamoListProductosprestamo);
                if (oldProductoOfProductosprestamoListProductosprestamo != null) {
                    oldProductoOfProductosprestamoListProductosprestamo.getProductosprestamoList().remove(productosprestamoListProductosprestamo);
                    oldProductoOfProductosprestamoListProductosprestamo = em.merge(oldProductoOfProductosprestamoListProductosprestamo);
                }
            }
            for (Bajas bajasListBajas : producto.getBajasList()) {
                Producto oldProductoOfBajasListBajas = bajasListBajas.getProducto();
                bajasListBajas.setProducto(producto);
                bajasListBajas = em.merge(bajasListBajas);
                if (oldProductoOfBajasListBajas != null) {
                    oldProductoOfBajasListBajas.getBajasList().remove(bajasListBajas);
                    oldProductoOfBajasListBajas = em.merge(oldProductoOfBajasListBajas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getIdproducto());
            Estado estadoOld = persistentProducto.getEstado();
            Estado estadoNew = producto.getEstado();
            Procedencia procedenciaOld = persistentProducto.getProcedencia();
            Procedencia procedenciaNew = producto.getProcedencia();
            List<Productosprestamo> productosprestamoListOld = persistentProducto.getProductosprestamoList();
            List<Productosprestamo> productosprestamoListNew = producto.getProductosprestamoList();
            List<Bajas> bajasListOld = persistentProducto.getBajasList();
            List<Bajas> bajasListNew = producto.getBajasList();
            List<String> illegalOrphanMessages = null;
            for (Productosprestamo productosprestamoListOldProductosprestamo : productosprestamoListOld) {
                if (!productosprestamoListNew.contains(productosprestamoListOldProductosprestamo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Productosprestamo " + productosprestamoListOldProductosprestamo + " since its producto field is not nullable.");
                }
            }
            for (Bajas bajasListOldBajas : bajasListOld) {
                if (!bajasListNew.contains(bajasListOldBajas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Bajas " + bajasListOldBajas + " since its producto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (estadoNew != null) {
                estadoNew = em.getReference(estadoNew.getClass(), estadoNew.getIdestado());
                producto.setEstado(estadoNew);
            }
            if (procedenciaNew != null) {
                procedenciaNew = em.getReference(procedenciaNew.getClass(), procedenciaNew.getIdprocedencia());
                producto.setProcedencia(procedenciaNew);
            }
            List<Productosprestamo> attachedProductosprestamoListNew = new ArrayList<Productosprestamo>();
            for (Productosprestamo productosprestamoListNewProductosprestamoToAttach : productosprestamoListNew) {
                productosprestamoListNewProductosprestamoToAttach = em.getReference(productosprestamoListNewProductosprestamoToAttach.getClass(), productosprestamoListNewProductosprestamoToAttach.getIdproductosprestamo());
                attachedProductosprestamoListNew.add(productosprestamoListNewProductosprestamoToAttach);
            }
            productosprestamoListNew = attachedProductosprestamoListNew;
            producto.setProductosprestamoList(productosprestamoListNew);
            List<Bajas> attachedBajasListNew = new ArrayList<Bajas>();
            for (Bajas bajasListNewBajasToAttach : bajasListNew) {
                bajasListNewBajasToAttach = em.getReference(bajasListNewBajasToAttach.getClass(), bajasListNewBajasToAttach.getIdbajas());
                attachedBajasListNew.add(bajasListNewBajasToAttach);
            }
            bajasListNew = attachedBajasListNew;
            producto.setBajasList(bajasListNew);
            producto = em.merge(producto);
            if (estadoOld != null && !estadoOld.equals(estadoNew)) {
                estadoOld.getProductoList().remove(producto);
                estadoOld = em.merge(estadoOld);
            }
            if (estadoNew != null && !estadoNew.equals(estadoOld)) {
                estadoNew.getProductoList().add(producto);
                estadoNew = em.merge(estadoNew);
            }
            if (procedenciaOld != null && !procedenciaOld.equals(procedenciaNew)) {
                procedenciaOld.getProductoList().remove(producto);
                procedenciaOld = em.merge(procedenciaOld);
            }
            if (procedenciaNew != null && !procedenciaNew.equals(procedenciaOld)) {
                procedenciaNew.getProductoList().add(producto);
                procedenciaNew = em.merge(procedenciaNew);
            }
            for (Productosprestamo productosprestamoListNewProductosprestamo : productosprestamoListNew) {
                if (!productosprestamoListOld.contains(productosprestamoListNewProductosprestamo)) {
                    Producto oldProductoOfProductosprestamoListNewProductosprestamo = productosprestamoListNewProductosprestamo.getProducto();
                    productosprestamoListNewProductosprestamo.setProducto(producto);
                    productosprestamoListNewProductosprestamo = em.merge(productosprestamoListNewProductosprestamo);
                    if (oldProductoOfProductosprestamoListNewProductosprestamo != null && !oldProductoOfProductosprestamoListNewProductosprestamo.equals(producto)) {
                        oldProductoOfProductosprestamoListNewProductosprestamo.getProductosprestamoList().remove(productosprestamoListNewProductosprestamo);
                        oldProductoOfProductosprestamoListNewProductosprestamo = em.merge(oldProductoOfProductosprestamoListNewProductosprestamo);
                    }
                }
            }
            for (Bajas bajasListNewBajas : bajasListNew) {
                if (!bajasListOld.contains(bajasListNewBajas)) {
                    Producto oldProductoOfBajasListNewBajas = bajasListNewBajas.getProducto();
                    bajasListNewBajas.setProducto(producto);
                    bajasListNewBajas = em.merge(bajasListNewBajas);
                    if (oldProductoOfBajasListNewBajas != null && !oldProductoOfBajasListNewBajas.equals(producto)) {
                        oldProductoOfBajasListNewBajas.getBajasList().remove(bajasListNewBajas);
                        oldProductoOfBajasListNewBajas = em.merge(oldProductoOfBajasListNewBajas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getIdproducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdproducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Productosprestamo> productosprestamoListOrphanCheck = producto.getProductosprestamoList();
            for (Productosprestamo productosprestamoListOrphanCheckProductosprestamo : productosprestamoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Productosprestamo " + productosprestamoListOrphanCheckProductosprestamo + " in its productosprestamoList field has a non-nullable producto field.");
            }
            List<Bajas> bajasListOrphanCheck = producto.getBajasList();
            for (Bajas bajasListOrphanCheckBajas : bajasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Bajas " + bajasListOrphanCheckBajas + " in its bajasList field has a non-nullable producto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estado estado = producto.getEstado();
            if (estado != null) {
                estado.getProductoList().remove(producto);
                estado = em.merge(estado);
            }
            Procedencia procedencia = producto.getProcedencia();
            if (procedencia != null) {
                procedencia.getProductoList().remove(producto);
                procedencia = em.merge(procedencia);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
