package com.ceiba.tiendatecnologica.aplicacion.fabrica;

import com.ceiba.tiendatecnologica.aplicacion.comando.ComandoGarantia;
import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import org.springframework.stereotype.Component;

@Component
public class FabricaGarantia {

    private final FabricaProducto fabricaProducto;

    public FabricaGarantia(FabricaProducto fabricaProducto) {
        this.fabricaProducto = fabricaProducto;
    }

    public GarantiaExtendida crearGarantia(ComandoGarantia comandoGarantia) {
        return new GarantiaExtendida(
                fabricaProducto.crearProducto(comandoGarantia.getProducto()),
                comandoGarantia.getFechaSolicitudGarantia(),
                comandoGarantia.getFechaFinGarantia(),
                comandoGarantia.getPrecioGarantia(),
                comandoGarantia.getNombreCliente()
        );
    }

}
