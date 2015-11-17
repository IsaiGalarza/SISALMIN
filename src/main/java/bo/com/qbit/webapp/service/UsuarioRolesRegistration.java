/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import bo.com.qbit.webapp.model.UsuarioRol;

import java.util.logging.Level;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class UsuarioRolesRegistration {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<UsuarioRol> rolesEventSrc;

	public void register(UsuarioRol usuarioRol) throws Exception {
		log.info("Registering UsuarioRol: " + usuarioRol.getId());
		em.persist(usuarioRol);
		rolesEventSrc.fire(usuarioRol);
	}

	public void update(UsuarioRol usuarioRol) throws Exception {
		log.info("Registering UsuarioRol: " + usuarioRol.getId());
		usuarioRol.setId(obtenerSiExiste(usuarioRol).getId());
		em.merge(usuarioRol);
		rolesEventSrc.fire(usuarioRol);
	}

	public void remove(UsuarioRol usuarioRol){
		log.info("Remover UsuarioRol: " + usuarioRol.getId());
		em.merge(usuarioRol);
		rolesEventSrc.fire(usuarioRol);
	}

	public UsuarioRol obtenerSiExiste(UsuarioRol usuarioRol){
		try{
			String query = "select ur from UsuarioRol ur where ur.usuario.id="+usuarioRol.getUsuario().getId()+" and ur.roles.id="+usuarioRol.getRoles().getId();
			return (UsuarioRol) em.createQuery(query).getSingleResult();
		}catch(EntityNotFoundException enfe){
			log.log(Level.SEVERE, "error :"+enfe.getMessage());
			return new UsuarioRol();
		}
	}
}
