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

import java.util.List;

import br.com.anteros.nosql.persistence.metadata.annotations.DiscriminatorValue;
import br.com.anteros.nosql.persistence.metadata.annotations.Entity;
import br.com.anteros.nosql.persistence.metadata.annotations.Reference;

/**
 * Grupo
 * 
 * Classe que representa um Grupo de Usuários no sistema. Possui uma lista de Ações as quais 
 * também serão atribuídas aos membros do Grupo.
 * 
 * @author Edson Martins edsonmartins2005@gmail.com
 */

@Entity(value="SEGURANCA")
@DiscriminatorValue(value = "GRUPO")
public class Group extends Security {

	/*
	 * Lista de membros do Grupo (Usuários)
	 */
	@Reference(lazy=true, value="members")
	private List<User> members;

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}


}
