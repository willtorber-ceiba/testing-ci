package com.ceiba.tiendatecnologica.dominio.servicio.producto;

import com.ceiba.tiendatecnologica.dominio.Producto;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioProducto;
import org.springframework.stereotype.Component;

@Component
public class ServicioObtenerProducto {

	public static final String PRODUCTO_NO_ENCONTRADO = "No se encontró un producto. Por favor verifique el código ingresado";

	private final RepositorioProducto repositorioProducto;

	public ServicioObtenerProducto(RepositorioProducto repositorioProducto) {
		this.repositorioProducto = repositorioProducto;
	}

	public Producto ejecutar(String codigo) {
		return this.repositorioProducto.obtenerPorCodigo(codigo);
	}

}
