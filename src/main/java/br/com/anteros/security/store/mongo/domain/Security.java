/*******************************************************************************
 * Copyright 2012 Anteros Tecnologia
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package br.com.anteros.security.store.mongo.domain;

import java.io.Serializable;
import java.util.Set;

import br.com.anteros.nosql.persistence.metadata.annotations.Cascade;
import br.com.anteros.nosql.persistence.metadata.annotations.Embedded;
import br.com.anteros.nosql.persistence.metadata.annotations.Entity;
import br.com.anteros.nosql.persistence.metadata.annotations.Id;
import br.com.anteros.nosql.persistence.metadata.annotations.Index;
import br.com.anteros.nosql.persistence.metadata.annotations.IndexField;
import br.com.anteros.nosql.persistence.metadata.annotations.Indexes;
import br.com.anteros.nosql.persistence.metadata.annotations.Property;
import br.com.anteros.nosql.persistence.metadata.annotations.Reference;
import br.com.anteros.nosql.persistence.metadata.annotations.type.CascadeType;
import br.com.anteros.security.store.domain.IAction;

/**
 * Seguranca
 * 
 * Classe abstrata que vai representar qualquer objeto que necessite de controle
 * de acesso a determinados Recursos/Ações dentro de um Sistema.
 * 
 * @author Edson Martins edsonmartins2005@gmail.com
 */
@Entity(value = "SEGURANCA")
@Indexes({ @Index(name = "IX_SEGURANCA_ID_HORARIO", fields = { @IndexField("horario") }),
		@Index(name = "SEGURANCA_PERFIL", fields = { @IndexField("perfil") }),
		@Index(name = "UK_SEGURANCA_LOGIN", fields = { @IndexField("login") }) })
public abstract class Security implements Serializable {

	public static final String NAME = "name";
	public static final String SECURITY_PACKAGE = "br.com.anteros.security.model";
	/*
	 * Identificação do Objeto de Segurança
	 */
	@Id
	private String id;

	@Property(value = NAME, required = true)
	private String name;

	@Property(value = "description", required = true)
	private String description;

	@Property(value = "securityType", required = true)
	private String securityType;

	/*
	 * Lista de Ações permitidas para um determinado objeto de Segurança.
	 */
	@Embedded(value = "actions")
	private Set<Action> actions;

	/*
	 * Lista de horários de acesso permitidos para um determinado objeto de
	 * Segurança.
	 */
	@Reference(lazy = true, mappedBy = "security", value = "securityAccessTime")
	@Cascade(values = { CascadeType.DELETE_ORPHAN })
	private Set<SecurityAccess> securityAccess;

	/*
	 * Email do usuário
	 */
	@Property(value = "email")
	private String email;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSecurityType() {
		return securityType;
	}

	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}

	public Set<Action> getActions() {
		return actions;
	}

	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}

	public Set<SecurityAccess> getSecurityAccess() {
		return securityAccess;
	}

	public void setSecurityAccessTime(Set<SecurityAccess> securityAccess) {
		this.securityAccess = securityAccess;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSecurityAccess(Set<SecurityAccess> securityAccess) {
		this.securityAccess = securityAccess;
	}
}
