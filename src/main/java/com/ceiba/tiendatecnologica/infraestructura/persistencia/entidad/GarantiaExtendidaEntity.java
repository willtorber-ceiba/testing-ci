package com.ceiba.tiendatecnologica.infraestructura.persistencia.entidad;

import javax.persistence.*;
import java.util.Date;


@Entity(name = "GarantiaExtendida")
@NamedQuery(name = "GarantiaExtendida.findByCodigo", query = "SELECT garantia from GarantiaExtendida garantia where garantia.producto.codigo = :codigo")
public class GarantiaExtendidaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private double precio;
	private String nombreCliente;
	private Date fechaFinGarantia;
	private Date fechaSolicitudGarantia;
    @ManyToOne
    @JoinColumn(name = "ID_PRODUCTO", referencedColumnName = "id")
    private ProductoEntity producto;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public Date getFechaSolicitudGarantia() {
        return fechaSolicitudGarantia;
    }

    public void setFechaSolicitudGarantia(Date fechaInicioGarantia) {
        this.fechaSolicitudGarantia = fechaInicioGarantia;
    }

    public Date getFechaFinGarantia() {
        return fechaFinGarantia;
    }

    public void setFechaFinGarantia(Date fechaFinGarantia) {
        this.fechaFinGarantia = fechaFinGarantia;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

}
