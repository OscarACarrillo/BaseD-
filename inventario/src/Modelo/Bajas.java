/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
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

/**
 *
 * @author oscar
 */
@Entity
@Table(name = "bajas")
@NamedQueries({
    @NamedQuery(name = "Bajas.findAll", query = "SELECT b FROM Bajas b"),
    @NamedQuery(name = "Bajas.findByIdbajas", query = "SELECT b FROM Bajas b WHERE b.idbajas = :idbajas"),
    @NamedQuery(name = "Bajas.findByCantidad", query = "SELECT b FROM Bajas b WHERE b.cantidad = :cantidad"),
    @NamedQuery(name = "Bajas.findByMotivo", query = "SELECT b FROM Bajas b WHERE b.motivo = :motivo")})
public class Bajas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idbajas")
    private Integer idbajas;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @Column(name = "motivo")
    private String motivo;
    @JoinColumn(name = "producto", referencedColumnName = "idproducto")
    @ManyToOne(optional = false)
    private Producto producto;

    public Bajas() {
    }

    public Bajas(Integer idbajas) {
        this.idbajas = idbajas;
    }

    public Bajas(Integer idbajas, int cantidad, String motivo) {
        this.idbajas = idbajas;
        this.cantidad = cantidad;
        this.motivo = motivo;
    }

    public Integer getIdbajas() {
        return idbajas;
    }

    public void setIdbajas(Integer idbajas) {
        this.idbajas = idbajas;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idbajas != null ? idbajas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bajas)) {
            return false;
        }
        Bajas other = (Bajas) object;
        if ((this.idbajas == null && other.idbajas != null) || (this.idbajas != null && !this.idbajas.equals(other.idbajas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Bajas[ idbajas=" + idbajas + " ]";
    }
    
}
