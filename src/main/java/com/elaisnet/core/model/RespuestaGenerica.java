package com.elaisnet.core.model;

public class RespuestaGenerica {
	
	public RespuestaGenerica(String error, String mensaje) {
		
		this.error = error;
		
		this.mensaje = mensaje;
	}
	
	public String getError() {
		
		return error;
	}
	
	public String getMensaje() {
		
		return mensaje;
	}
	
	private String error;
	
	private String mensaje;

}
