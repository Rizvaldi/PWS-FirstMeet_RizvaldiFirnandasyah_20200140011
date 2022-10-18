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
@Table(name = "penjual")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Penjual.findAll", query = "SELECT p FROM Penjual p"),
    @NamedQuery(name = "Penjual.findByIdpenjual", query = "SELECT p FROM Penjual p WHERE p.idpenjual = :idpenjual"),
    @NamedQuery(name = "Penjual.findByNamapenjual", query = "SELECT p FROM Penjual p WHERE p.namapenjual = :namapenjual"),
    @NamedQuery(name = "Penjual.findByNotelp", query = "SELECT p FROM Penjual p WHERE p.notelp = :notelp"),
    @NamedQuery(name = "Penjual.findByAlamat", query = "SELECT p FROM Penjual p WHERE p.alamat = :alamat")})
public class Penjual implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_penjual")
    private Integer idpenjual;
    @Basic(optional = false)
    @Column(name = "Nama_penjual")
    private String namapenjual;
    @Basic(optional = false)
    @Column(name = "No_telp")
    private String notelp;
    @Basic(optional = false)
    @Column(name = "Alamat")
    private String alamat;
    @JoinColumn(name = "Id_penjual", referencedColumnName = "Id_pembeli", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pembeli pembeli;

    public Penjual() {
    }

    public Penjual(Integer idpenjual) {
        this.idpenjual = idpenjual;
    }

    public Penjual(Integer idpenjual, String namapenjual, String notelp, String alamat) {
        this.idpenjual = idpenjual;
        this.namapenjual = namapenjual;
        this.notelp = notelp;
        this.alamat = alamat;
    }

    public Integer getIdpenjual() {
        return idpenjual;
    }

    public void setIdpenjual(Integer idpenjual) {
        this.idpenjual = idpenjual;
    }

    public String getNamapenjual() {
        return namapenjual;
    }

    public void setNamapenjual(String namapenjual) {
        this.namapenjual = namapenjual;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
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
        hash += (idpenjual != null ? idpenjual.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Penjual)) {
            return false;
        }
        Penjual other = (Penjual) object;
        if ((this.idpenjual == null && other.idpenjual != null) || (this.idpenjual != null && !this.idpenjual.equals(other.idpenjual))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sembako.sembako.Penjual[ idpenjual=" + idpenjual + " ]";
    }
    
}
