package com.mio.jersey.todo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.mio.jersey.todo.modelo.Usuario;

public class BDUsuario {
	private static final String PERSISTENCE_UNIT_NAME = "antoniotoro.davidgonzalez";
	private static EntityManagerFactory factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

	/**
	 * Inserta un usuario en la BD siempre que no exista ya un usuario con
	 * ese mismo email.
	 * @param usuario Usuario a insertar
	 */
	public static void insertar(Usuario usuario) {
		if (!existeEmail(usuario.getEmail())) {
			EntityManager em = factoria.createEntityManager();
			em.getTransaction().begin();

			em.persist(usuario);
			
			em.getTransaction().commit();
			em.close();
		}
	}

	/**
	 * Inserta un usuario en la BD siempre que no exista ya un usuario con
	 * ese mismo email.
	 * @param usuario Usuario a insertar
	 */
	public static void insertarTodo(String tt, String cnt) {
//			EntityManager em = factoria.createEntityManager();
//			Todo todo = new Todo(tt, cnt);
//			em.getTransaction().begin();
//
//			em.persist(todo);
//			
//			em.getTransaction().commit();
//			em.close();
	}
	
	/**
	 * Actualiza los datos de un usuario en la BD.
	 * @param usuario Usuario con los cambios ya realizados
	 */
	public static void actualizar(Usuario usuario) {
		if (existeEmail(usuario.getEmail())) {
			EntityManager em = factoria.createEntityManager();
			
			Query q = em.createQuery(
					"SELECT u from Usuario u WHERE u.email LIKE :emailUsuario")
					.setParameter("emailUsuario", usuario.getEmail());
			
			Usuario antiguo = (Usuario) q.getSingleResult();

			em.getTransaction().begin();
			
			antiguo.setNombre(usuario.getNombre());
			
			em.getTransaction().commit();
			em.close();
		}
	}
	
	/**
	 * Elimina un usuario de la BD.
	 * @param usuario Usuario a eliminar
	 */
	public static void eliminar(Usuario usuario) {
		if (existeEmail(usuario.getEmail())) {
			EntityManager em = factoria.createEntityManager();

			em.getTransaction().begin();

			Query q = em.createQuery(
					"SELECT u from Usuario u WHERE u.email LIKE :emailUsuario")
					.setParameter("emailUsuario", usuario.getEmail());
			
			Usuario almacenado = (Usuario) q.getSingleResult();
			
			em.remove(almacenado);
			
			em.getTransaction().commit();
			em.close();
		}
	}
	
	/**
	 * Recupera un usuario de la BD.
	 * @param email Email del usuario que se quiere recuperar
	 * @return El Usuario si se ha encontrado, <tt>null</tt> en caso contrario.
	 */
	public static Usuario seleccionarUsuario(String email) {
		if (existeEmail(email)) {
			EntityManager em = factoria.createEntityManager();
			Query q = em.createQuery(
					"SELECT u from Usuario u WHERE u.email LIKE :emailUsuario")
					.setParameter("emailUsuario", email);
			
			Usuario usuario = (Usuario) q.getSingleResult();
			
			em.close();
			
			return usuario;
		}
		return null;
	}
	
	/**
	 * Recupera un usuario de la BD mediante el ID.
	 * @param id ID del usuario que se quiere recuperar
	 * @return El Usuario si se ha encontrado, <tt>null</tt> en caso contrario.
	 */
	public static Usuario seleccionarUsuarioPorId(long id) {
		EntityManager em = factoria.createEntityManager();
		Query q = em.createQuery(
				"SELECT u from Usuario u WHERE u.id = :idUsuario")
				.setParameter("idUsuario", id);
		
		Usuario usuario;
		try {
			usuario = (Usuario) q.getSingleResult();
		} catch (NoResultException e) {
			usuario = null;
		} finally {
			em.close();
		}
		return usuario;
	}
	
	/**
	 * Comprueba que exista un usuario con un email.
	 * @param email Email que queremos comprobar
	 * @return <tt>true</tt> si existe, <tt>false</tt> si no
	 */
	public static boolean existeEmail(String email) {
		EntityManager em = factoria.createEntityManager();
		Query q = em.createQuery(
				"SELECT u from Usuario u WHERE u.email LIKE :emailUsuario")
				.setParameter("emailUsuario", email);
		
		try {
			q.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		} finally {
			em.close();
		}
	}
	
	/**
	 * Lista los usuarios de la BD.
	 * @return Lista que contiene los usuarios
	 */
	public static List<Usuario> listarUsuarios() {
		EntityManager em = factoria.createEntityManager();
		Query q = em.createQuery("SELECT u from Usuario u");
		
		@SuppressWarnings("unchecked")
		List<Usuario> lista = q.getResultList();

		em.close();
		
		return lista;
	}
	
}
