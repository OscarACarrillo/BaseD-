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
@Table(name = "destino")
@NamedQueries({
    @NamedQuery(name = "Destino.findAll", query = "SELECT d FROM Destino d"),
    @NamedQuery(name = "Destino.findByIddestino", query = "SELECT d FROM Destino d WHERE d.iddestino = :iddestino"),
    @NamedQuery(name = "Destino.findByNombre", query = "SELECT d FROM Destino d WHERE d.nombre = :nombre")})
public class Destino implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddestino")
    private Integer iddestino;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destino")
    private List<Prestamo> prestamoList;

    public Destino() {
    }

    public Destino(Integer iddestino) {
        this.iddestino = iddestino;
    }

    public Destino(Integer iddestino, String nombre) {
        this.iddestino = iddestino;
        this.nombre = nombre;
    }

    public Integer getIddestino() {
        return iddestino;
    }

    public void setIddestino(Integer iddestino) {
        this.iddestino = iddestino;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Prestamo> getPrestamoList() {
        return prestamoList;
    }

    public void setPrestamoList(List<Prestamo> prestamoList) {
        this.prestamoList = prestamoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddestino != null ? iddestino.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Destino)) {
            return false;
        }
        Destino other = (Destino) object;
        if ((this.iddestino == null && other.iddestino != null) || (this.iddestino != null && !this.iddestino.equals(other.iddestino))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Destino[ iddestino=" + iddestino + " ]";
    }
    
}
