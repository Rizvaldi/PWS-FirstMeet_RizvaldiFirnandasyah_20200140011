/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sembako.sembako;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MSI 65 SERIES
 */
@Entity
@Table(name = "transaksi")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transaksi.findAll", query = "SELECT t FROM Transaksi t"),
    @NamedQuery(name = "Transaksi.findByIdtransaksi", query = "SELECT t FROM Transaksi t WHERE t.idtransaksi = :idtransaksi"),
    @NamedQuery(name = "Transaksi.findByIdbarang", query = "SELECT t FROM Transaksi t WHERE t.idbarang = :idbarang"),
    @NamedQuery(name = "Transaksi.findByIdpembeli", query = "SELECT t FROM Transaksi t WHERE t.idpembeli = :idpembeli"),
    @NamedQuery(name = "Transaksi.findByIdpenjual", query = "SELECT t FROM Transaksi t WHERE t.idpenjual = :idpenjual"),
    @NamedQuery(name = "Transaksi.findByTanggal", query = "SELECT t FROM Transaksi t WHERE t.tanggal = :tanggal"),
    @NamedQuery(name = "Transaksi.findByJenistransaksi", query = "SELECT t FROM Transaksi t WHERE t.jenistransaksi = :jenistransaksi")})
public class Transaksi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_transaksi")
    private Integer idtransaksi;
    @Basic(optional = false)
    @Column(name = "Id_barang")
    private int idbarang;
    @Basic(optional = false)
    @Column(name = "Id_pembeli")
    private int idpembeli;
    @Basic(optional = false)
    @Column(name = "Id_penjual")
    private int idpenjual;
    @Basic(optional = false)
    @Column(name = "Tanggal")
    @Temporal(TemporalType.DATE)
    private Date tanggal;
    @Basic(optional = false)
    @Column(name = "Jenis_transaksi")
    private String jenistransaksi;
    @JoinColumn(name = "Id_transaksi", referencedColumnName = "Id_pembeli", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pembeli pembeli;

    public Transaksi() {
    }

    public Transaksi(Integer idtransaksi) {
        this.idtransaksi = idtransaksi;
    }

    public Transaksi(Integer idtransaksi, int idbarang, int idpembeli, int idpenjual, Date tanggal, String jenistransaksi) {
        this.idtransaksi = idtransaksi;
        this.idbarang = idbarang;
        this.idpembeli = idpembeli;
        this.idpenjual = idpenjual;
        this.tanggal = tanggal;
        this.jenistransaksi = jenistransaksi;
    }

    public Integer getIdtransaksi() {
        return idtransaksi;
    }

    public void setIdtransaksi(Integer idtransaksi) {
        this.idtransaksi = idtransaksi;
    }

    public int getIdbarang() {
        return idbarang;
    }

    public void setIdbarang(int idbarang) {
        this.idbarang = idbarang;
    }

    public int getIdpembeli() {
        return idpembeli;
    }

    public void setIdpembeli(int idpembeli) {
        this.idpembeli = idpembeli;
    }

    public int getIdpenjual() {
        return idpenjual;
    }

    public void setIdpenjual(int idpenjual) {
        this.idpenjual = idpenjual;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getJenistransaksi() {
        return jenistransaksi;
    }

    public void setJenistransaksi(String jenistransaksi) {
        this.jenistransaksi = jenistransaksi;
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
        hash += (idtransaksi != null ? idtransaksi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaksi)) {
            return false;
        }
        Transaksi other = (Transaksi) object;
        if ((this.idtransaksi == null && other.idtransaksi != null) || (this.idtransaksi != null && !this.idtransaksi.equals(other.idtransaksi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sembako.sembako.Transaksi[ idtransaksi=" + idtransaksi + " ]";
    }
    
}
