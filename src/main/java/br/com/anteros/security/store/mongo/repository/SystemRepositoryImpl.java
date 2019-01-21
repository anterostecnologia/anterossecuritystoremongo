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
package br.com.anteros.security.store.mongo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import br.com.anteros.nosql.persistence.mongodb.query.MongoCriteria;
import br.com.anteros.nosql.persistence.mongodb.query.MongoQuery;
import br.com.anteros.nosql.persistence.mongodb.session.repository.MongoSimpleRepository;
import br.com.anteros.nosql.persistence.session.NoSQLSessionFactory;
import br.com.anteros.security.store.exception.AnterosSecurityStoreException;
import br.com.anteros.security.store.mongo.domain.System;

@Repository("systemRepositoryMongo")
@Scope("prototype")
public class SystemRepositoryImpl extends MongoSimpleRepository<System, Long> implements SystemRepository {

	@Autowired
	public SystemRepositoryImpl(@Qualifier("securitySessionFactory") NoSQLSessionFactory sessionFactory)
			throws Exception {
		super(sessionFactory);
	}

	@Override
	public System getSystemByName(String systemName) {
		MongoQuery query = MongoQuery.of(MongoCriteria.where(System.NAME).is(systemName));
		List<System> result = this.find(query);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public System addSystem(String systemName, String description) {
		try {
			this.getSession().getTransaction().begin();
			System newSystem = System.of(systemName, description);
			this.getSession().save(newSystem);
			this.getSession().getTransaction().commit();
			return newSystem;
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			throw new AnterosSecurityStoreException(e);
		}
	}

}
