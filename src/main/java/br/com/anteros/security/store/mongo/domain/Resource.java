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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.anteros.nosql.persistence.metadata.annotations.Cascade;
import br.com.anteros.nosql.persistence.metadata.annotations.Entity;
import br.com.anteros.nosql.persistence.metadata.annotations.Id;
import br.com.anteros.nosql.persistence.metadata.annotations.Index;
import br.com.anteros.nosql.persistence.metadata.annotations.IndexField;
import br.com.anteros.nosql.persistence.metadata.annotations.IndexOptions;
import br.com.anteros.nosql.persistence.metadata.annotations.Indexes;
import br.com.anteros.nosql.persistence.metadata.annotations.Property;
import br.com.anteros.nosql.persistence.metadata.annotations.Reference;
import br.com.anteros.nosql.persistence.metadata.annotations.type.CascadeType;
import br.com.anteros.security.store.domain.IAction;
import br.com.anteros.security.store.domain.IResource;
import br.com.anteros.security.store.domain.ISystem;

/**
 * Recurso
 * 
 * Classe que representa o objeto que será controlado o acesso pelo usuário
 * dentro de um sistema. Ex: Formulário, relatório, etc. Terá uma lista de ações
 * específicas que serão atribuídas posteriormente a qualquer objeto que extenda
 * Seguranca como um Papel, um Usuario ou um Grupo.
 * 
 * @author Edson Martins edsonmartins2005@gmail.com
 */
@Entity(value = "SEGURANCARECURSO")
@Indexes(value = { @Index(name = "UK_SEGURANCARECURSO_NOME_RECUR", options = @IndexOptions(unique = true), fields = {
		@IndexField(value = "sistema"), @IndexField(value = "nomeRecurso") }) })
public class Resource implements Serializable, IResource {

	public static final String SYSTEM = "system";

	public static final String DESCRIPTION = "description";

	public static final String NAME = "name";

	/*
	 * Identificação do Recurso
	 */
	@Id
	private String id;

	/*
	 * Nome do Recurso
	 */
	@Property(value = NAME)
	private String name;

	/*
	 * Descrição do Recurso
	 */
	@Property(value = DESCRIPTION)
	private String description;

	/*
	 * Lista de Ações que serão controladas acesso para um Recurso.
	 */
	@Reference(mappedBy = "resource", lazy = true, value = "actions")
	@Cascade(values = CascadeType.DELETE_ORPHAN)
	private Set<Action> actions;

	/*
	 * Sistema a qual pertence o Recurso.
	 */
	@Reference(lazy = true, value = SYSTEM)
	private System system;

	public Resource() {

	}

	public Resource(String resourceName, String description, System system) {
		this.name = resourceName;
		this.description = description;
		this.system = system;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((system == null) ? 0 : system.hashCode());
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
		Resource other = (Resource) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (system == null) {
			if (other.system != null)
				return false;
		} else if (!system.equals(other.system))
			return false;
		return true;
	}

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

	public Set<Action> getActions() {
		return actions;
	}

	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}

	@Override
	public String getResourceName() {
		return name;
	}

	public List<IAction> getActionList() {
		List<IAction> result = new ArrayList<IAction>();
		if (actions != null) {
			for (Action action : actions) {
				result.add(action);
			}
		}
		return result;
	}

	public static Resource of(String resourceName, String description, System system) {
		return new Resource(resourceName, description, system);
	}

	public static Resource of(String resourceName, String description, ISystem system) {
		return new Resource(resourceName, description, (System) system);
	}

	@Override
	public IResource addAction(IAction action) {
		if (actions==null) {
			actions= new HashSet<>();
		}
		this.actions.add((Action) action);
		return this;
	}

	@Override
	public String getResourceId() {
		return this.getId();
	}

}
