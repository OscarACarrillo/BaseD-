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
@Table(name = "muebles")
@NamedQueries({
    @NamedQuery(name = "Muebles.findAll", query = "SELECT m FROM Muebles m"),
    @NamedQuery(name = "Muebles.findByIdmuebles", query = "SELECT m FROM Muebles m WHERE m.idmuebles = :idmuebles"),
    @NamedQuery(name = "Muebles.findByFechaentrega", query = "SELECT m FROM Muebles m WHERE m.fechaentrega = :fechaentrega"),
    @NamedQuery(name = "Muebles.findByFactura", query = "SELECT m FROM Muebles m WHERE m.factura = :factura"),
    @NamedQuery(name = "Muebles.findByDescripcion", query = "SELECT m FROM Muebles m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "Muebles.findByMarca", query = "SELECT m FROM Muebles m WHERE m.marca = :marca"),
    @NamedQuery(name = "Muebles.findBySerie", query = "SELECT m FROM Muebles m WHERE m.serie = :serie"),
    @NamedQuery(name = "Muebles.findByImporte", query = "SELECT m FROM Muebles m WHERE m.importe = :importe"),
    @NamedQuery(name = "Muebles.findByTotal", query = "SELECT m FROM Muebles m WHERE m.total = :total"),
    @NamedQuery(name = "Muebles.findByDepartamento", query = "SELECT m FROM Muebles m WHERE m.departamento = :departamento"),
    @NamedQuery(name = "Muebles.findByResguardante", query = "SELECT m FROM Muebles m WHERE m.resguardante = :resguardante")})
public class Muebles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmuebles")
    private Integer idmuebles;
    @Basic(optional = false)
    @Column(name = "fechaentrega")
    @Temporal(TemporalType.DATE)
    private Date fechaentrega;
    @Column(name = "factura")
    private String factura;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "marca")
    private String marca;
    @Column(name = "serie")
    private String serie;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "importe")
    private Float importe;
    @Column(name = "total")
    private Float total;
    @Column(name = "departamento")
    private String departamento;
    @Column(name = "resguardante")
    private String resguardante;
    @JoinColumn(name = "procedencia", referencedColumnName = "idprocedencia")
    @ManyToOne(optional = false)
    private Procedencia procedencia;

    public Muebles() {
    }

    public Muebles(Integer idmuebles) {
        this.idmuebles = idmuebles;
    }

    public Muebles(Integer idmuebles, Date fechaentrega, String descripcion) {
        this.idmuebles = idmuebles;
        this.fechaentrega = fechaentrega;
        this.descripcion = descripcion;
    }

    public Integer getIdmuebles() {
        return idmuebles;
    }

    public void setIdmuebles(Integer idmuebles) {
        this.idmuebles = idmuebles;
    }

    public Date getFechaentrega() {
        return fechaentrega;
    }

    public void setFechaentrega(Date fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Float getImporte() {
        return importe;
    }

    public void setImporte(Float importe) {
        this.importe = importe;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getResguardante() {
        return resguardante;
    }

    public void setResguardante(String resguardante) {
        this.resguardante = resguardante;
    }

    public Procedencia getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(Procedencia procedencia) {
        this.procedencia = procedencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmuebles != null ? idmuebles.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Muebles)) {
            return false;
        }
        Muebles other = (Muebles) object;
        if ((this.idmuebles == null && other.idmuebles != null) || (this.idmuebles != null && !this.idmuebles.equals(other.idmuebles))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Muebles[ idmuebles=" + idmuebles + " ]";
    }
    
}
