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
public class PembeliJpaController implements Serializable {

    public PembeliJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pembeli pembeli) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barang barang = pembeli.getBarang();
            if (barang != null) {
                barang = em.getReference(barang.getClass(), barang.getIdbarang());
                pembeli.setBarang(barang);
            }
            Transaksi transaksi = pembeli.getTransaksi();
            if (transaksi != null) {
                transaksi = em.getReference(transaksi.getClass(), transaksi.getIdtransaksi());
                pembeli.setTransaksi(transaksi);
            }
            Penjual penjual = pembeli.getPenjual();
            if (penjual != null) {
                penjual = em.getReference(penjual.getClass(), penjual.getIdpenjual());
                pembeli.setPenjual(penjual);
            }
            em.persist(pembeli);
            if (barang != null) {
                Pembeli oldPembeliOfBarang = barang.getPembeli();
                if (oldPembeliOfBarang != null) {
                    oldPembeliOfBarang.setBarang(null);
                    oldPembeliOfBarang = em.merge(oldPembeliOfBarang);
                }
                barang.setPembeli(pembeli);
                barang = em.merge(barang);
            }
            if (transaksi != null) {
                Pembeli oldPembeliOfTransaksi = transaksi.getPembeli();
                if (oldPembeliOfTransaksi != null) {
                    oldPembeliOfTransaksi.setTransaksi(null);
                    oldPembeliOfTransaksi = em.merge(oldPembeliOfTransaksi);
                }
                transaksi.setPembeli(pembeli);
                transaksi = em.merge(transaksi);
            }
            if (penjual != null) {
                Pembeli oldPembeliOfPenjual = penjual.getPembeli();
                if (oldPembeliOfPenjual != null) {
                    oldPembeliOfPenjual.setPenjual(null);
                    oldPembeliOfPenjual = em.merge(oldPembeliOfPenjual);
                }
                penjual.setPembeli(pembeli);
                penjual = em.merge(penjual);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPembeli(pembeli.getIdpembeli()) != null) {
                throw new PreexistingEntityException("Pembeli " + pembeli + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pembeli pembeli) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembeli persistentPembeli = em.find(Pembeli.class, pembeli.getIdpembeli());
            Barang barangOld = persistentPembeli.getBarang();
            Barang barangNew = pembeli.getBarang();
            Transaksi transaksiOld = persistentPembeli.getTransaksi();
            Transaksi transaksiNew = pembeli.getTransaksi();
            Penjual penjualOld = persistentPembeli.getPenjual();
            Penjual penjualNew = pembeli.getPenjual();
            List<String> illegalOrphanMessages = null;
            if (barangOld != null && !barangOld.equals(barangNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Barang " + barangOld + " since its pembeli field is not nullable.");
            }
            if (transaksiOld != null && !transaksiOld.equals(transaksiNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Transaksi " + transaksiOld + " since its pembeli field is not nullable.");
            }
            if (penjualOld != null && !penjualOld.equals(penjualNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Penjual " + penjualOld + " since its pembeli field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (barangNew != null) {
                barangNew = em.getReference(barangNew.getClass(), barangNew.getIdbarang());
                pembeli.setBarang(barangNew);
            }
            if (transaksiNew != null) {
                transaksiNew = em.getReference(transaksiNew.getClass(), transaksiNew.getIdtransaksi());
                pembeli.setTransaksi(transaksiNew);
            }
            if (penjualNew != null) {
                penjualNew = em.getReference(penjualNew.getClass(), penjualNew.getIdpenjual());
                pembeli.setPenjual(penjualNew);
            }
            pembeli = em.merge(pembeli);
            if (barangNew != null && !barangNew.equals(barangOld)) {
                Pembeli oldPembeliOfBarang = barangNew.getPembeli();
                if (oldPembeliOfBarang != null) {
                    oldPembeliOfBarang.setBarang(null);
                    oldPembeliOfBarang = em.merge(oldPembeliOfBarang);
                }
                barangNew.setPembeli(pembeli);
                barangNew = em.merge(barangNew);
            }
            if (transaksiNew != null && !transaksiNew.equals(transaksiOld)) {
                Pembeli oldPembeliOfTransaksi = transaksiNew.getPembeli();
                if (oldPembeliOfTransaksi != null) {
                    oldPembeliOfTransaksi.setTransaksi(null);
                    oldPembeliOfTransaksi = em.merge(oldPembeliOfTransaksi);
                }
                transaksiNew.setPembeli(pembeli);
                transaksiNew = em.merge(transaksiNew);
            }
            if (penjualNew != null && !penjualNew.equals(penjualOld)) {
                Pembeli oldPembeliOfPenjual = penjualNew.getPembeli();
                if (oldPembeliOfPenjual != null) {
                    oldPembeliOfPenjual.setPenjual(null);
                    oldPembeliOfPenjual = em.merge(oldPembeliOfPenjual);
                }
                penjualNew.setPembeli(pembeli);
                penjualNew = em.merge(penjualNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pembeli.getIdpembeli();
                if (findPembeli(id) == null) {
                    throw new NonexistentEntityException("The pembeli with id " + id + " no longer exists.");
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
            Pembeli pembeli;
            try {
                pembeli = em.getReference(Pembeli.class, id);
                pembeli.getIdpembeli();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pembeli with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Barang barangOrphanCheck = pembeli.getBarang();
            if (barangOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pembeli (" + pembeli + ") cannot be destroyed since the Barang " + barangOrphanCheck + " in its barang field has a non-nullable pembeli field.");
            }
            Transaksi transaksiOrphanCheck = pembeli.getTransaksi();
            if (transaksiOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pembeli (" + pembeli + ") cannot be destroyed since the Transaksi " + transaksiOrphanCheck + " in its transaksi field has a non-nullable pembeli field.");
            }
            Penjual penjualOrphanCheck = pembeli.getPenjual();
            if (penjualOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pembeli (" + pembeli + ") cannot be destroyed since the Penjual " + penjualOrphanCheck + " in its penjual field has a non-nullable pembeli field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pembeli);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pembeli> findPembeliEntities() {
        return findPembeliEntities(true, -1, -1);
    }

    public List<Pembeli> findPembeliEntities(int maxResults, int firstResult) {
        return findPembeliEntities(false, maxResults, firstResult);
    }

    private List<Pembeli> findPembeliEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pembeli.class));
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

    public Pembeli findPembeli(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pembeli.class, id);
        } finally {
            em.close();
        }
    }

    public int getPembeliCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pembeli> rt = cq.from(Pembeli.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
