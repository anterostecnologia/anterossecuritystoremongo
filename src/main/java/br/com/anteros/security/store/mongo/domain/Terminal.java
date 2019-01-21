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
import br.com.anteros.nosql.persistence.metadata.annotations.Property;

/**
 * Terminal
 * 
 * Classe que representa o cadastro dos terminais de Acesso ex: computador, coletor etc
 * 
 * @author Edson Martins edsonmartins2005@gmail.com
 */

@Entity(value="SEGURANCATERMINALACESSO")
public class Terminal implements Serializable {
	/*
	 * Identificador do terminal de acesso
	 */
	@Id
	private String id;
	
	/*
	 * Nome do terminal de acesso
	 */
	@Property(value="name", required=true)
	private String name;
	
	/*
	 * Descrição do terminal de acesso
	 */
	@Property(value="description", required=true)
	private String description;
	
	/*
	 * Endereço IP do terminal de acesso
	 */
	@Property(value="ipAddress", required=true)
	private String ipAddress;


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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Terminal other = (Terminal) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


}
