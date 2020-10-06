package com.ceiba.tiendatecnologica.testdatabuilder;

import com.ceiba.tiendatecnologica.aplicacion.comando.ComandoGarantia;
import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.Producto;

import java.util.Date;

public class GarantiaTestDataBuilder {

    private final ProductoTestDataBuilder productoTestDataBuilder;

    private Producto producto;
    private String nombreCliente;
    private double precioGarantia;
    private Date fechaFinGarantia;
    private Date fechaSolicitudGarantia;

    public GarantiaTestDataBuilder(ProductoTestDataBuilder productoTestDataBuilder) {
        this.productoTestDataBuilder = productoTestDataBuilder;
    }

    public GarantiaTestDataBuilder setProducto(Producto producto) {
        this.producto = producto;
        return this;
    }

    public GarantiaTestDataBuilder setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
        return this;
    }

    public GarantiaTestDataBuilder setPrecioGarantia(double precioGarantia) {
        this.precioGarantia = precioGarantia;
        return this;
    }

    public GarantiaTestDataBuilder setFechaFinGarantia(Date fechaFinGarantia) {
        this.fechaFinGarantia = fechaFinGarantia;
        return this;
    }

    public GarantiaTestDataBuilder setFechaSolicitudGarantia(Date fechaSolicitudGarantia) {
        this.fechaSolicitudGarantia = fechaSolicitudGarantia;
        return this;
    }

    public GarantiaExtendida build() {
        return new GarantiaExtendida(productoTestDataBuilder.build(), this.fechaSolicitudGarantia,
                this.fechaFinGarantia, this.precioGarantia, this.nombreCliente);
    }

    public ComandoGarantia buildComando() {
        return new ComandoGarantia(productoTestDataBuilder.buildComando(), this.fechaSolicitudGarantia,
                this.fechaFinGarantia, this.precioGarantia, this.nombreCliente);
    }

}
