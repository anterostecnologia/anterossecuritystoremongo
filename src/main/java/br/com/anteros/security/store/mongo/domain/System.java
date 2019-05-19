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

import br.com.anteros.nosql.persistence.metadata.annotations.Entity;
import br.com.anteros.nosql.persistence.metadata.annotations.Id;
import br.com.anteros.nosql.persistence.metadata.annotations.Index;
import br.com.anteros.nosql.persistence.metadata.annotations.IndexField;
import br.com.anteros.nosql.persistence.metadata.annotations.IndexOptions;
import br.com.anteros.nosql.persistence.metadata.annotations.Indexes;
import br.com.anteros.nosql.persistence.metadata.annotations.Property;
import br.com.anteros.nosql.persistence.metadata.annotations.Reference;
import br.com.anteros.security.store.domain.ISystem;

/**
 * Sistema
 * 
 * Classe que representa um Sistema que necessite de controle de acesso de
 * Recursos/Ações.
 * 
 * @author Edson Martins edsonmartins2005@gmail.com
 */
@Entity(value="SEGURANCASISTEMA")
@Indexes(value = {
		@Index(name = "UK_SEGURANCASISTEMA_NOME_SIST", fields = {@IndexField("nomeSistema")}, options=@IndexOptions(unique = true)) })
public class System implements Serializable, ISystem {

	public static final String RESOURCES = "resources";

	public static final String MINIMAL_VERSION = "minimalVersion";

	public static final String DESCRIPTION = "description";

	public static final String NAME = "name";

	/*
	 * Identificação do Sistema
	 */
	@Id
	private String systemId;

	/*
	 * Nome do Sistema
	 */
	@Property(value = NAME,  required = true)
	private String name;

	/*
	 * Descrição do Sistema
	 */
	@Property(value = DESCRIPTION, required = true)
	private String description;

	/*
	 * Versão mínima do Sistema
	 */
	@Property(value = MINIMAL_VERSION)
	private String minimalVersion;

	/*
	 * Lista de Recursos que pertencem a um Sistema.
	 */
	@Reference(lazy=true,mappedBy = "system", value=RESOURCES)
	private Set<Resource> resources;

	public System() {
		
	}

	public System(String systemName, String description) {
		this.name = systemName;
		this.description = description;
	}


	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String id) {
		this.systemId = id;
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

	public String getMinimalVersion() {
		return minimalVersion;
	}

	public void setMinimalVersion(String minimalVersion) {
		this.minimalVersion = minimalVersion;
	}

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

	@Override
	public String getSystemName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((minimalVersion == null) ? 0 : minimalVersion.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((resources == null) ? 0 : resources.hashCode());
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
		System other = (System) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (minimalVersion == null) {
			if (other.minimalVersion != null)
				return false;
		} else if (!minimalVersion.equals(other.minimalVersion))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		return true;
	}

	public static System of(String systemName, String description) {
		return new System(systemName,description);
	}


}
