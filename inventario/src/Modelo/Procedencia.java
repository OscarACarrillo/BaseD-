/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author oscar
 */
@Entity
@Table(name = "procedencia")
@NamedQueries({
    @NamedQuery(name = "Procedencia.findAll", query = "SELECT p FROM Procedencia p"),
    @NamedQuery(name = "Procedencia.findByIdprocedencia", query = "SELECT p FROM Procedencia p WHERE p.idprocedencia = :idprocedencia"),
    @NamedQuery(name = "Procedencia.findByProcedencia", query = "SELECT p FROM Procedencia p WHERE p.procedencia = :procedencia")})
public class Procedencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idprocedencia")
    private Integer idprocedencia;
    @Basic(optional = false)
    @Column(name = "procedencia")
    private String procedencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procedencia")
    private List<Producto> productoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procedencia")
    private List<Muebles> mueblesList;

    public Procedencia() {
    }

    public Procedencia(Integer idprocedencia) {
        this.idprocedencia = idprocedencia;
    }

    public Procedencia(Integer idprocedencia, String procedencia) {
        this.idprocedencia = idprocedencia;
        this.procedencia = procedencia;
    }

    public Integer getIdprocedencia() {
        return idprocedencia;
    }

    public void setIdprocedencia(Integer idprocedencia) {
        this.idprocedencia = idprocedencia;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    public List<Muebles> getMueblesList() {
        return mueblesList;
    }

    public void setMueblesList(List<Muebles> mueblesList) {
        this.mueblesList = mueblesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idprocedencia != null ? idprocedencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Procedencia)) {
            return false;
        }
        Procedencia other = (Procedencia) object;
        if ((this.idprocedencia == null && other.idprocedencia != null) || (this.idprocedencia != null && !this.idprocedencia.equals(other.idprocedencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Procedencia[ idprocedencia=" + idprocedencia + " ]";
    }
    
}
