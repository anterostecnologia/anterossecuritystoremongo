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
import br.com.anteros.nosql.persistence.metadata.annotations.Reference;

/**
 * HorarioAcessoIntervalo
 * 
 * Classe que representa o intervalo de Horário de Acesso que será permitido ao Usuário utilizar o sistema.
 * 
 * @author Edson Martins edsonmartins2005@gmail.com
 */
@Entity(value="SEGURANCAHORARIOINTERVALO")
public class AccessTimeInterval implements Serializable {

	
	/*
	 * Identificação
	 */
	@Id
	private String id;
	
	/*
	 * Horário de acesso a qual pertence o intervalo
	 */
	@Reference(lazy=true, value="accessTime")
	private AccessTime accessTime;

	/*
	 * Dia da semana
	 */
	@Property(value = "dayOfWeek")
	private Long dayOfWeek;

	/*
	 * Hora Inicial
	 */
	@Property(value = "startTime")
	private String startTime;

	/*
	 * Hora Final
	 */
	@Property(value = "endTime")
	private String endTime;


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

	public Long getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Long dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


}
