/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author oscar
 */
@Entity
@Table(name = "acceso")
@NamedQueries({
    @NamedQuery(name = "Acceso.findAll", query = "SELECT a FROM Acceso a"),
    @NamedQuery(name = "Acceso.findByIdacceso", query = "SELECT a FROM Acceso a WHERE a.idacceso = :idacceso"),
    @NamedQuery(name = "Acceso.findByFechayhoraingreso", query = "SELECT a FROM Acceso a WHERE a.fechayhoraingreso = :fechayhoraingreso"),
    @NamedQuery(name = "Acceso.findByFechayhorasalida", query = "SELECT a FROM Acceso a WHERE a.fechayhorasalida = :fechayhorasalida")})
public class Acceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idacceso")
    private Integer idacceso;
    @Basic(optional = false)
    @Column(name = "fechayhoraingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechayhoraingreso;
    @Basic(optional = false)
    @Column(name = "fechayhorasalida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechayhorasalida;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Login usuario;

    public Acceso() {
    }

    public Acceso(Integer idacceso) {
        this.idacceso = idacceso;
    }

    public Acceso(Integer idacceso, Date fechayhoraingreso, Date fechayhorasalida) {
        this.idacceso = idacceso;
        this.fechayhoraingreso = fechayhoraingreso;
        this.fechayhorasalida = fechayhorasalida;
    }

    public Integer getIdacceso() {
        return idacceso;
    }

    public void setIdacceso(Integer idacceso) {
        this.idacceso = idacceso;
    }

    public Date getFechayhoraingreso() {
        return fechayhoraingreso;
    }

    public void setFechayhoraingreso(Date fechayhoraingreso) {
        this.fechayhoraingreso = fechayhoraingreso;
    }

    public Date getFechayhorasalida() {
        return fechayhorasalida;
    }

    public void setFechayhorasalida(Date fechayhorasalida) {
        this.fechayhorasalida = fechayhorasalida;
    }

    public Login getUsuario() {
        return usuario;
    }

    public void setUsuario(Login usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idacceso != null ? idacceso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Acceso)) {
            return false;
        }
        Acceso other = (Acceso) object;
        if ((this.idacceso == null && other.idacceso != null) || (this.idacceso != null && !this.idacceso.equals(other.idacceso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Acceso[ idacceso=" + idacceso + " ]";
    }
    
}
