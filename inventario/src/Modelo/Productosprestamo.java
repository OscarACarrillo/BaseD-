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
@Table(name = "productosprestamo")
@NamedQueries({
    @NamedQuery(name = "Productosprestamo.findAll", query = "SELECT p FROM Productosprestamo p"),
    @NamedQuery(name = "Productosprestamo.findByIdproductosprestamo", query = "SELECT p FROM Productosprestamo p WHERE p.idproductosprestamo = :idproductosprestamo"),
    @NamedQuery(name = "Productosprestamo.findByCantidad", query = "SELECT p FROM Productosprestamo p WHERE p.cantidad = :cantidad")})
public class Productosprestamo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idproductosprestamo")
    private Integer idproductosprestamo;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @JoinColumn(name = "producto", referencedColumnName = "idproducto")
    @ManyToOne(optional = false)
    private Producto producto;

    public Productosprestamo() {
    }

    public Productosprestamo(Integer idproductosprestamo) {
        this.idproductosprestamo = idproductosprestamo;
    }

    public Productosprestamo(Integer idproductosprestamo, int cantidad) {
        this.idproductosprestamo = idproductosprestamo;
        this.cantidad = cantidad;
    }

    public Integer getIdproductosprestamo() {
        return idproductosprestamo;
    }

    public void setIdproductosprestamo(Integer idproductosprestamo) {
        this.idproductosprestamo = idproductosprestamo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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
        hash += (idproductosprestamo != null ? idproductosprestamo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productosprestamo)) {
            return false;
        }
        Productosprestamo other = (Productosprestamo) object;
        if ((this.idproductosprestamo == null && other.idproductosprestamo != null) || (this.idproductosprestamo != null && !this.idproductosprestamo.equals(other.idproductosprestamo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Productosprestamo[ idproductosprestamo=" + idproductosprestamo + " ]";
    }
    
}
