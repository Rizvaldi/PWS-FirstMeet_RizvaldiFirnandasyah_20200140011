/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sembako.sembako;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sembako.sembako.exceptions.IllegalOrphanException;
import sembako.sembako.exceptions.NonexistentEntityException;
import sembako.sembako.exceptions.PreexistingEntityException;

/**
 *
 * @author MSI 65 SERIES
 */
public class PenjualJpaController implements Serializable {

    public PenjualJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("sembako_sembako_jar_0.0.1-SNAPSHOTPU");

    public PenjualJpaController() {
    }
    
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Penjual penjual) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pembeli pembeliOrphanCheck = penjual.getPembeli();
        if (pembeliOrphanCheck != null) {
            Penjual oldPenjualOfPembeli = pembeliOrphanCheck.getPenjual();
            if (oldPenjualOfPembeli != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pembeli " + pembeliOrphanCheck + " already has an item of type Penjual whose pembeli column cannot be null. Please make another selection for the pembeli field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembeli pembeli = penjual.getPembeli();
            if (pembeli != null) {
                pembeli = em.getReference(pembeli.getClass(), pembeli.getIdpembeli());
                penjual.setPembeli(pembeli);
            }
            em.persist(penjual);
            if (pembeli != null) {
                pembeli.setPenjual(penjual);
                pembeli = em.merge(pembeli);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPenjual(penjual.getIdpenjual()) != null) {
                throw new PreexistingEntityException("Penjual " + penjual + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Penjual penjual) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Penjual persistentPenjual = em.find(Penjual.class, penjual.getIdpenjual());
            Pembeli pembeliOld = persistentPenjual.getPembeli();
            Pembeli pembeliNew = penjual.getPembeli();
            List<String> illegalOrphanMessages = null;
            if (pembeliNew != null && !pembeliNew.equals(pembeliOld)) {
                Penjual oldPenjualOfPembeli = pembeliNew.getPenjual();
                if (oldPenjualOfPembeli != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pembeli " + pembeliNew + " already has an item of type Penjual whose pembeli column cannot be null. Please make another selection for the pembeli field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pembeliNew != null) {
                pembeliNew = em.getReference(pembeliNew.getClass(), pembeliNew.getIdpembeli());
                penjual.setPembeli(pembeliNew);
            }
            penjual = em.merge(penjual);
            if (pembeliOld != null && !pembeliOld.equals(pembeliNew)) {
                pembeliOld.setPenjual(null);
                pembeliOld = em.merge(pembeliOld);
            }
            if (pembeliNew != null && !pembeliNew.equals(pembeliOld)) {
                pembeliNew.setPenjual(penjual);
                pembeliNew = em.merge(pembeliNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = penjual.getIdpenjual();
                if (findPenjual(id) == null) {
                    throw new NonexistentEntityException("The penjual with id " + id + " no longer exists.");
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
            Penjual penjual;
            try {
                penjual = em.getReference(Penjual.class, id);
                penjual.getIdpenjual();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The penjual with id " + id + " no longer exists.", enfe);
            }
            Pembeli pembeli = penjual.getPembeli();
            if (pembeli != null) {
                pembeli.setPenjual(null);
                pembeli = em.merge(pembeli);
            }
            em.remove(penjual);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Penjual> findPenjualEntities() {
        return findPenjualEntities(true, -1, -1);
    }

    public List<Penjual> findPenjualEntities(int maxResults, int firstResult) {
        return findPenjualEntities(false, maxResults, firstResult);
    }

    private List<Penjual> findPenjualEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Penjual.class));
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

    public Penjual findPenjual(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Penjual.class, id);
        } finally {
            em.close();
        }
    }

    public int getPenjualCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Penjual> rt = cq.from(Penjual.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
