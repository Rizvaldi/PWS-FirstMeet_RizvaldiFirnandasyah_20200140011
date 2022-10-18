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
import sembako.sembako.exceptions.IllegalOrphanException;
import sembako.sembako.exceptions.NonexistentEntityException;
import sembako.sembako.exceptions.PreexistingEntityException;

/**
 *
 * @author MSI 65 SERIES
 */
public class BarangJpaController implements Serializable {

    public BarangJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Barang barang) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pembeli pembeliOrphanCheck = barang.getPembeli();
        if (pembeliOrphanCheck != null) {
            Barang oldBarangOfPembeli = pembeliOrphanCheck.getBarang();
            if (oldBarangOfPembeli != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pembeli " + pembeliOrphanCheck + " already has an item of type Barang whose pembeli column cannot be null. Please make another selection for the pembeli field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembeli pembeli = barang.getPembeli();
            if (pembeli != null) {
                pembeli = em.getReference(pembeli.getClass(), pembeli.getIdpembeli());
                barang.setPembeli(pembeli);
            }
            em.persist(barang);
            if (pembeli != null) {
                pembeli.setBarang(barang);
                pembeli = em.merge(pembeli);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBarang(barang.getIdbarang()) != null) {
                throw new PreexistingEntityException("Barang " + barang + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Barang barang) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barang persistentBarang = em.find(Barang.class, barang.getIdbarang());
            Pembeli pembeliOld = persistentBarang.getPembeli();
            Pembeli pembeliNew = barang.getPembeli();
            List<String> illegalOrphanMessages = null;
            if (pembeliNew != null && !pembeliNew.equals(pembeliOld)) {
                Barang oldBarangOfPembeli = pembeliNew.getBarang();
                if (oldBarangOfPembeli != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pembeli " + pembeliNew + " already has an item of type Barang whose pembeli column cannot be null. Please make another selection for the pembeli field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pembeliNew != null) {
                pembeliNew = em.getReference(pembeliNew.getClass(), pembeliNew.getIdpembeli());
                barang.setPembeli(pembeliNew);
            }
            barang = em.merge(barang);
            if (pembeliOld != null && !pembeliOld.equals(pembeliNew)) {
                pembeliOld.setBarang(null);
                pembeliOld = em.merge(pembeliOld);
            }
            if (pembeliNew != null && !pembeliNew.equals(pembeliOld)) {
                pembeliNew.setBarang(barang);
                pembeliNew = em.merge(pembeliNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = barang.getIdbarang();
                if (findBarang(id) == null) {
                    throw new NonexistentEntityException("The barang with id " + id + " no longer exists.");
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
            Barang barang;
            try {
                barang = em.getReference(Barang.class, id);
                barang.getIdbarang();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The barang with id " + id + " no longer exists.", enfe);
            }
            Pembeli pembeli = barang.getPembeli();
            if (pembeli != null) {
                pembeli.setBarang(null);
                pembeli = em.merge(pembeli);
            }
            em.remove(barang);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Barang> findBarangEntities() {
        return findBarangEntities(true, -1, -1);
    }

    public List<Barang> findBarangEntities(int maxResults, int firstResult) {
        return findBarangEntities(false, maxResults, firstResult);
    }

    private List<Barang> findBarangEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Barang.class));
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

    public Barang findBarang(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Barang.class, id);
        } finally {
            em.close();
        }
    }

    public int getBarangCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Barang> rt = cq.from(Barang.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
