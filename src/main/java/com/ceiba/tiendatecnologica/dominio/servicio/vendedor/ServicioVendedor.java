package com.ceiba.tiendatecnologica.dominio.servicio.vendedor;

import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.Producto;
import com.ceiba.tiendatecnologica.dominio.excepcion.GarantiaExtendidaAsignadaException;
import com.ceiba.tiendatecnologica.dominio.excepcion.GarantiaExtendidaException;
import com.ceiba.tiendatecnologica.dominio.excepcion.ResourceNoFoundException;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioGarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioProducto;
import com.ceiba.tiendatecnologica.dominio.servicio.producto.ServicioObtenerProducto;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * Contiene los métodos asociados a la generación de garantias por parte de un vendedor.
 *
 * @author William Torres
 * @since 2020
 * */
public class ServicioVendedor {

    /**
     * Cantidad de vocales
     * */
    public static final short LIMITE_VOCALES_CODIGO = 3;
    public static final int PRECIO_MINIMO_GARANTIA_EXTENDIDA = 500_000;
    public static final double PORCENTAJE_MAYOR_PRECIO_GARANTIA = 0.2d;
    public static final double PORCENTAJE_MENOR_PRECIO_GARANTIA = 0.1d;
    public static final short CANTIDAD_MAYOR_DIAS_EXTENSION_GARANTIA = 200;
    public static final short CANTIDAD_MENOR_DIAS_EXTENSION_GARANTIA = 100;
    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya tiene asignada una garantía extendida";
    public static final String EL_PRODUCTO_NO_CUENTA_CON_GARANTIA = "Este producto no cuenta con garantía extendida";

    private final RepositorioProducto repositorioProducto;
    private final RepositorioGarantiaExtendida repositorioGarantia;

    public ServicioVendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioGarantia = repositorioGarantia;
    }

    /**
     * Genera y asocia una garantia extendida a un producto existente.
     *
     * @param codigo Código del producto al que se le asignará la garantia extendida.
     * @param nombreCliente Nombre de cliente que solicita la garantia extendida.
     * */
    public void generarGarantia(String codigo, String nombreCliente) {

        // Validar existencia del producto
        Optional<Producto> optionalProducto = Optional.ofNullable(repositorioProducto.obtenerPorCodigo(codigo));
        if (!optionalProducto.isPresent()) {
            throw new ResourceNoFoundException(ServicioObtenerProducto.PRODUCTO_NO_ENCONTRADO);
        }
        Producto producto = optionalProducto.get();

        // Validar si al producto se le puede licencia extendida (número de vocales != 3)
        validarDisponibilidadGarantia(codigo);

        // Validar si el producto ya cuenta con una licencia extendida
        boolean tieneLicencia = tieneGarantia(codigo);
        if (tieneLicencia) {
            throw new GarantiaExtendidaAsignadaException(EL_PRODUCTO_TIENE_GARANTIA);
        }

        // Definir la fecha de finalización y precio de la garantia extendida
        Date fechaSolicitud = new Date();
        double precioGarantia = calcularPrecioGarantia(producto.getPrecio());
        Date fechaFinalizacion = definirFechaFinGarantia(producto.getPrecio(), fechaSolicitud);

        // Guardar garantia extendida
        GarantiaExtendida garantiaExtendida = new GarantiaExtendida(producto, fechaSolicitud,
                fechaFinalizacion, precioGarantia, nombreCliente
        );
        repositorioGarantia.agregar(garantiaExtendida);
    }

    /**
     * Valida si un producto tiene asignada una garantia extendida.
     *
     * @param codigo Código del producto.
     * @return Si el producto tiene una garantia asignada {@code True}, en caso contrario {@code False}.
     * */
    public boolean tieneGarantia(String codigo) {
        Optional<Producto> optionalProducto = Optional.ofNullable(
                repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo)
        );
        return optionalProducto.isPresent();
    }

    /**
     * Valida si a un producto se le puede asignar una garantia extendida en base a la cantidad
     * de vocales encontradas en el código.
     *
     * @param codigo Código del producto.
     * @throws GarantiaExtendidaException Si el código del producto tiene la cantidad de
     * vocales establecida en {@code LIMITE_VOCALES_CODIGO }.
     * */
    private void validarDisponibilidadGarantia(String codigo){
        int cantidadVocales = 0;
        codigo = codigo.toLowerCase();
        for (int i = 0; i < codigo.length(); i++) {
            char ch = codigo.charAt(i);
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                cantidadVocales++;
            }
        }
        if (LIMITE_VOCALES_CODIGO == cantidadVocales) {
            throw new GarantiaExtendidaException(EL_PRODUCTO_NO_CUENTA_CON_GARANTIA);
        }
    }

    /**
     * Calcula el precio de la garantia extendida en base al precio del producto.
     *
     * @param precioProducto Precio del producto.
     * @return El precio de la garantia extendida.
     * */
    private double calcularPrecioGarantia(double precioProducto) {
        double precioGarantia;
        if (precioProducto > PRECIO_MINIMO_GARANTIA_EXTENDIDA) {
            precioGarantia = precioProducto * PORCENTAJE_MAYOR_PRECIO_GARANTIA;
        } else {
            precioGarantia = precioProducto * PORCENTAJE_MENOR_PRECIO_GARANTIA;
        }
        return precioGarantia;
    }

    /**
     * Calcula la fecha de finalización de la garantia extendida a partir del precio del producto
     * y otras reglas definidas por el negocio.
     *
     * @param precioProducto Precio del producto.
     * @param fechaSolicitud Fecha en la que se realiza la solicitud de la garantia extendida.
     * @return Fecha de finalización de la garantia extendida.
     * */
    private Date definirFechaFinGarantia(double precioProducto, Date fechaSolicitud) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaSolicitud);
        if (precioProducto > PRECIO_MINIMO_GARANTIA_EXTENDIDA) {
            // Se agregan 200 días omitiendo el día lunes.
            int addedDays = 0;
            while (addedDays < CANTIDAD_MAYOR_DIAS_EXTENSION_GARANTIA) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    addedDays++;
                }
            }
            boolean esDomingo = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
            if (esDomingo) {
                // En caso de ser domingo, la fecha se traslada al siguiente día hábil (por defecto será el lunes)
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
        } else {
            calendar.add(Calendar.DAY_OF_YEAR, CANTIDAD_MENOR_DIAS_EXTENSION_GARANTIA);
        }
        return calendar.getTime();
    }

}
