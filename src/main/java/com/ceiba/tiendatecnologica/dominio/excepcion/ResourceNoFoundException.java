package com.ceiba.tiendatecnologica.dominio.excepcion;

public class ResourceNoFoundException extends RuntimeException {

	private static final long serialVersionUID = 5632297850596829248L;

	public ResourceNoFoundException(String message) {
		super(message);
	}
}
