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

import br.com.anteros.nosql.persistence.metadata.annotations.Entity;
import br.com.anteros.nosql.persistence.metadata.annotations.Id;
import br.com.anteros.nosql.persistence.metadata.annotations.Reference;


/**
 *SegurancaHorarioAcesso
 *
 * Classe que representa os terminais e sistemas que um usuário pode acessar em um de
 * determinado horário de acesso
 * 
 * @author Edson Martins edsonmartins2005@gmail.com
 */

@Entity(value = "SEGURANCAACESSO")
public class SecurityAccess implements Serializable {

	/*
	 * Identificador do segurança horário de acesso
	 */
	@Id
	private String id;

	/*
	 * Identificador do horário de acesso
	 */
	@Reference(lazy=true, required=true, value="accessTime")
	private AccessTime accessTime;

	/*
	 * Identificador do segurança
	 */
	@Reference(lazy=true, required=true, value="security")
	private Security security;

	/*
	 * Terminal de acesso
	 */
	@Reference(lazy=true, required=true, value="terminal")
	private Terminal terminal;

	/*
	 * Identificador do sistema
	 */
	@Reference(lazy=true, required=true, value="system")
	private System system;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AccessTime getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(AccessTime accessTime) {
		this.accessTime = accessTime;
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}


}
