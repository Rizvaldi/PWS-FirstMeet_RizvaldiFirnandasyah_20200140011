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
public class TransaksiJpaController implements Serializable {

    public TransaksiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("sembako_sembako_jar_0.0.1-SNAPSHOTPU");

    public TransaksiJpaController() {
    }
    
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaksi transaksi) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pembeli pembeliOrphanCheck = transaksi.getPembeli();
        if (pembeliOrphanCheck != null) {
            Transaksi oldTransaksiOfPembeli = pembeliOrphanCheck.getTransaksi();
            if (oldTransaksiOfPembeli != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pembeli " + pembeliOrphanCheck + " already has an item of type Transaksi whose pembeli column cannot be null. Please make another selection for the pembeli field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembeli pembeli = transaksi.getPembeli();
            if (pembeli != null) {
                pembeli = em.getReference(pembeli.getClass(), pembeli.getIdpembeli());
                transaksi.setPembeli(pembeli);
            }
            em.persist(transaksi);
            if (pembeli != null) {
                pembeli.setTransaksi(transaksi);
                pembeli = em.merge(pembeli);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTransaksi(transaksi.getIdtransaksi()) != null) {
                throw new PreexistingEntityException("Transaksi " + transaksi + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaksi transaksi) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaksi persistentTransaksi = em.find(Transaksi.class, transaksi.getIdtransaksi());
            Pembeli pembeliOld = persistentTransaksi.getPembeli();
            Pembeli pembeliNew = transaksi.getPembeli();
            List<String> illegalOrphanMessages = null;
            if (pembeliNew != null && !pembeliNew.equals(pembeliOld)) {
                Transaksi oldTransaksiOfPembeli = pembeliNew.getTransaksi();
                if (oldTransaksiOfPembeli != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pembeli " + pembeliNew + " already has an item of type Transaksi whose pembeli column cannot be null. Please make another selection for the pembeli field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pembeliNew != null) {
                pembeliNew = em.getReference(pembeliNew.getClass(), pembeliNew.getIdpembeli());
                transaksi.setPembeli(pembeliNew);
            }
            transaksi = em.merge(transaksi);
            if (pembeliOld != null && !pembeliOld.equals(pembeliNew)) {
                pembeliOld.setTransaksi(null);
                pembeliOld = em.merge(pembeliOld);
            }
            if (pembeliNew != null && !pembeliNew.equals(pembeliOld)) {
                pembeliNew.setTransaksi(transaksi);
                pembeliNew = em.merge(pembeliNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = transaksi.getIdtransaksi();
                if (findTransaksi(id) == null) {
                    throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.");
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
            Transaksi transaksi;
            try {
                transaksi = em.getReference(Transaksi.class, id);
                transaksi.getIdtransaksi();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.", enfe);
            }
            Pembeli pembeli = transaksi.getPembeli();
            if (pembeli != null) {
                pembeli.setTransaksi(null);
                pembeli = em.merge(pembeli);
            }
            em.remove(transaksi);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaksi> findTransaksiEntities() {
        return findTransaksiEntities(true, -1, -1);
    }

    public List<Transaksi> findTransaksiEntities(int maxResults, int firstResult) {
        return findTransaksiEntities(false, maxResults, firstResult);
    }

    private List<Transaksi> findTransaksiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaksi.class));
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

    public Transaksi findTransaksi(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaksi.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransaksiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaksi> rt = cq.from(Transaksi.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
