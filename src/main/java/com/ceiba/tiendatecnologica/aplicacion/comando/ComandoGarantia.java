package com.ceiba.tiendatecnologica.aplicacion.comando;


import java.util.Date;

public class ComandoGarantia {

    private ComandoProducto producto;
    private String nombreCliente;
    private double precioGarantia;
    private Date fechaFinGarantia;
    private Date fechaSolicitudGarantia;

    public ComandoGarantia(ComandoProducto producto) {
        this.fechaSolicitudGarantia = new Date();
        this.producto = producto;
    }

    public ComandoGarantia(ComandoProducto producto, Date fechaSolicitudGarantia, Date fechaFinGarantia, double precioGarantia,
                           String nombreCliente) {
        this.producto = producto;
        this.fechaSolicitudGarantia = fechaSolicitudGarantia;
        this.fechaFinGarantia = fechaFinGarantia;
        this.precioGarantia = precioGarantia;
        this.nombreCliente = nombreCliente;
    }

    public ComandoProducto getProducto() {
        return producto;
    }

    public Date getFechaSolicitudGarantia() {
        return fechaSolicitudGarantia;
    }

    public Date getFechaFinGarantia() {
        return fechaFinGarantia;
    }

    public double getPrecioGarantia() {
        return precioGarantia;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }
}

