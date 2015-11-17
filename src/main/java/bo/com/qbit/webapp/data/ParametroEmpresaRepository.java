/*
 * ServicioRepository.java	1.0 2014/09/19
 *
 * Copyright (c) 2014 by QBIT SRL, Inc. All Rights Reserved.
 * 
 * QBIT SRL grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to QBIT SRL.
 * 
 * Autor: Isai Galarza
 * 
 */

package bo.com.qbit.webapp.data;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.ParametroEmpresa;

@Stateless
public class ParametroEmpresaRepository {

	@Inject
	private EntityManager em;

	public ParametroEmpresa findById(int id) {
		return em.find(ParametroEmpresa.class, id);
	}

	public ParametroEmpresa findByEmpresa(Empresa empresa) {
		String query = "select em from ParametroEmpresa em  where em.empresa.id="+empresa.getId();
		System.out.println("Query ParametroEmpresa: "+query);
		return (ParametroEmpresa) em.createQuery(query).getSingleResult();
	}




}
