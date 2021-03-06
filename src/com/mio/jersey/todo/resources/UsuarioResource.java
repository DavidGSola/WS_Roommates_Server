package com.mio.jersey.todo.resources;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.mio.jersey.todo.dao.BDCompra;
import com.mio.jersey.todo.dao.BDFactura;
import com.mio.jersey.todo.dao.BDUsuario;
import com.mio.jersey.todo.modelo.Compra;
import com.mio.jersey.todo.modelo.Factura;
import com.mio.jersey.todo.modelo.Respuesta;
import com.mio.jersey.todo.modelo.Usuario;

public class UsuarioResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String email;
	
	public UsuarioResource(UriInfo uriInfo, Request request, String email) 
	{
		this.uriInfo = uriInfo;
		this.request = request;
		this.email = email;
	}

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	public Usuario getUsuario() 
	{
		Usuario usr = BDUsuario.seleccionarUsuario(email);
		if(usr==null)
			throw new RuntimeException("Get: Usuario con " + email + " no se ha encontrado");
	
		return usr;
	}

//	@PUT
//	@Consumes(MediaType.APPLICATION_XML)
//	public Response put(JAXBElement<Todo> todo) 
//	{
//		Todo c = todo.getValue();
//		
//		return putAndGetResponse(c);
//	}

	@DELETE
	public Respuesta delete() 
	{
		Usuario usr = BDUsuario.seleccionarUsuario(email);
		if(usr!=null) {
			List<Usuario> usuarios = BDUsuario.listarUsuarios();
			if(usuarios.size() > 1)
			{
				for (Compra c : BDCompra.listarCompras()) 
					if (c.getUsuario().getEmail().equals(usr.getEmail())) 
						BDCompra.marcarComprado(c);
				
				for (Factura f : BDFactura.listarFacturas()) 
					BDFactura.marcarPagada(f, usr);
	
			}else
			{
				for (Compra c : BDCompra.listarCompras()) 
					if (c.getUsuario().getEmail().equals(usr.getEmail())) 
						BDCompra.eliminar(c);
				
				for (Factura f : BDFactura.listarFacturas()) 
					BDFactura.eliminar(f);
			}
			
			BDUsuario.eliminar(usr);
			
			return new Respuesta(false, "Delete: Usuario " + email + " eliminada");
		}
		else 
			return new Respuesta(true, "Delete: Usuario " + email + " no encontrado");
	}

//	private Response putAndGetResponse(Todo todo) 
//	{
//		Response res;
//		if(TodoDao.instance.getModel().containsKey(todo.getId()))
//			res = Response.noContent().build();
//		else 
//			res = Response.created(uriInfo.getAbsolutePath()).build();
//		
//		TodoDao.instance.getModel().put(todo.getId()+"", todo);
//		
//		return res;
//	}
} 