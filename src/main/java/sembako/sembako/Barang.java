/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sembako.sembako;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MSI 65 SERIES
 */
@Entity
@Table(name = "barang")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Barang.findAll", query = "SELECT b FROM Barang b"),
    @NamedQuery(name = "Barang.findByIdbarang", query = "SELECT b FROM Barang b WHERE b.idbarang = :idbarang"),
    @NamedQuery(name = "Barang.findByNamabarang", query = "SELECT b FROM Barang b WHERE b.namabarang = :namabarang"),
    @NamedQuery(name = "Barang.findByHarga", query = "SELECT b FROM Barang b WHERE b.harga = :harga"),
    @NamedQuery(name = "Barang.findByJenisbarang", query = "SELECT b FROM Barang b WHERE b.jenisbarang = :jenisbarang"),
    @NamedQuery(name = "Barang.findByJumlah", query = "SELECT b FROM Barang b WHERE b.jumlah = :jumlah")})
public class Barang implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_barang")
    private Integer idbarang;
    @Basic(optional = false)
    @Column(name = "Nama_barang")
    private String namabarang;
    @Basic(optional = false)
    @Column(name = "Harga")
    private int harga;
    @Basic(optional = false)
    @Column(name = "Jenis_barang")
    private String jenisbarang;
    @Basic(optional = false)
    @Column(name = "Jumlah")
    private int jumlah;
    @JoinColumn(name = "Id_barang", referencedColumnName = "Id_pembeli", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pembeli pembeli;

    public Barang() {
    }

    public Barang(Integer idbarang) {
        this.idbarang = idbarang;
    }

    public Barang(Integer idbarang, String namabarang, int harga, String jenisbarang, int jumlah) {
        this.idbarang = idbarang;
        this.namabarang = namabarang;
        this.harga = harga;
        this.jenisbarang = jenisbarang;
        this.jumlah = jumlah;
    }

    public Integer getIdbarang() {
        return idbarang;
    }

    public void setIdbarang(Integer idbarang) {
        this.idbarang = idbarang;
    }

    public String getNamabarang() {
        return namabarang;
    }

    public void setNamabarang(String namabarang) {
        this.namabarang = namabarang;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getJenisbarang() {
        return jenisbarang;
    }

    public void setJenisbarang(String jenisbarang) {
        this.jenisbarang = jenisbarang;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public Pembeli getPembeli() {
        return pembeli;
    }

    public void setPembeli(Pembeli pembeli) {
        this.pembeli = pembeli;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idbarang != null ? idbarang.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Barang)) {
            return false;
        }
        Barang other = (Barang) object;
        if ((this.idbarang == null && other.idbarang != null) || (this.idbarang != null && !this.idbarang.equals(other.idbarang))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sembako.sembako.Barang[ idbarang=" + idbarang + " ]";
    }
    
}
