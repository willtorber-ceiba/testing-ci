package com.ceiba.tiendatecnologica.infraestructura.persistencia.builder;

import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.infraestructura.persistencia.entidad.GarantiaExtendidaEntity;
import com.ceiba.tiendatecnologica.infraestructura.persistencia.entidad.ProductoEntity;

public class GarantiaExtendidaBuilder {

    private GarantiaExtendidaBuilder() { }

    public static GarantiaExtendida convertirADominio(GarantiaExtendidaEntity garantiaExtendidaEntity) {
        GarantiaExtendida garantiaExtendida = null;
        if(garantiaExtendidaEntity != null){
            garantiaExtendida = new GarantiaExtendida(ProductoBuilder.convertirADominio(garantiaExtendidaEntity.getProducto()),
                    garantiaExtendidaEntity.getFechaSolicitudGarantia(), garantiaExtendidaEntity.getFechaFinGarantia(),
                    garantiaExtendidaEntity.getPrecio(), garantiaExtendidaEntity.getNombreCliente()
            );
        }
        return garantiaExtendida;
    }

    public static GarantiaExtendidaEntity convertirAEntity(GarantiaExtendida garantiaExtendida, ProductoEntity producto) {
        GarantiaExtendidaEntity garantiaEntity = new GarantiaExtendidaEntity();
        garantiaEntity.setProducto(producto);
        garantiaEntity.setPrecio(garantiaExtendida.getPrecioGarantia());
        garantiaEntity.setNombreCliente(garantiaExtendida.getNombreCliente());
        garantiaEntity.setFechaFinGarantia(garantiaExtendida.getFechaFinGarantia());
        garantiaEntity.setFechaSolicitudGarantia(garantiaExtendida.getFechaSolicitudGarantia());
        return garantiaEntity;
    }

}
