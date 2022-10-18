/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sembako.sembako;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "pembeli")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pembeli.findAll", query = "SELECT p FROM Pembeli p"),
    @NamedQuery(name = "Pembeli.findByIdpembeli", query = "SELECT p FROM Pembeli p WHERE p.idpembeli = :idpembeli"),
    @NamedQuery(name = "Pembeli.findByNamapembeli", query = "SELECT p FROM Pembeli p WHERE p.namapembeli = :namapembeli"),
    @NamedQuery(name = "Pembeli.findByNOtelp", query = "SELECT p FROM Pembeli p WHERE p.nOtelp = :nOtelp"),
    @NamedQuery(name = "Pembeli.findByAlamat", query = "SELECT p FROM Pembeli p WHERE p.alamat = :alamat")})
public class Pembeli implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_pembeli")
    private Integer idpembeli;
    @Basic(optional = false)
    @Column(name = "Nama_pembeli")
    private String namapembeli;
    @Basic(optional = false)
    @Column(name = "NO_telp")
    private String nOtelp;
    @Basic(optional = false)
    @Column(name = "Alamat")
    private String alamat;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pembeli")
    private Barang barang;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pembeli")
    private Transaksi transaksi;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pembeli")
    private Penjual penjual;

    public Pembeli() {
    }

    public Pembeli(Integer idpembeli) {
        this.idpembeli = idpembeli;
    }

    public Pembeli(Integer idpembeli, String namapembeli, String nOtelp, String alamat) {
        this.idpembeli = idpembeli;
        this.namapembeli = namapembeli;
        this.nOtelp = nOtelp;
        this.alamat = alamat;
    }

    public Integer getIdpembeli() {
        return idpembeli;
    }

    public void setIdpembeli(Integer idpembeli) {
        this.idpembeli = idpembeli;
    }

    public String getNamapembeli() {
        return namapembeli;
    }

    public void setNamapembeli(String namapembeli) {
        this.namapembeli = namapembeli;
    }

    public String getNOtelp() {
        return nOtelp;
    }

    public void setNOtelp(String nOtelp) {
        this.nOtelp = nOtelp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }

    public Transaksi getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    public Penjual getPenjual() {
        return penjual;
    }

    public void setPenjual(Penjual penjual) {
        this.penjual = penjual;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpembeli != null ? idpembeli.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pembeli)) {
            return false;
        }
        Pembeli other = (Pembeli) object;
        if ((this.idpembeli == null && other.idpembeli != null) || (this.idpembeli != null && !this.idpembeli.equals(other.idpembeli))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sembako.sembako.Pembeli[ idpembeli=" + idpembeli + " ]";
    }
    
}
