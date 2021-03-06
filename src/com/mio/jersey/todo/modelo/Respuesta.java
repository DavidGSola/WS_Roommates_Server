package com.mio.jersey.todo.modelo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Respuesta {
	boolean error;
	String mensaje;
	
	public Respuesta() { }
	
	public Respuesta(boolean error, String mensaje) {
		this.error = error;
		this.mensaje = mensaje;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
}
