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
import br.com.anteros.security.store.domain.IResource;
import br.com.anteros.security.store.domain.ISystem;
import br.com.anteros.security.store.exception.AnterosSecurityStoreException;
import br.com.anteros.security.store.mongo.domain.Resource;

@Repository("resourceRepositoryMongo")
@Scope("prototype")
public class ResourceRepositoryImpl extends MongoSimpleRepository<Resource, Long> implements ResourceRepository {

	@Autowired
	public ResourceRepositoryImpl(@Qualifier("securitySessionFactory") NoSQLSessionFactory sessionFactory)
			throws Exception {
		super(sessionFactory);
	}

	@Override
	public Resource getResourceByName(ISystem system, String resourceName) {
		MongoQuery query = MongoQuery
				.of(MongoCriteria.where(Resource.NAME).is(resourceName).and("system.$id").is(system.getId()));
		List<Resource> result = this.find(query);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public Resource addResource(ISystem system, String resourceName, String description) {
		try {
			this.getSession().getTransaction().begin();
			Resource resource = Resource.of(resourceName, description, system);
			this.getSession().save(resource);
			this.getSession().getTransaction().commit();
			return resource;
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			throw new AnterosSecurityStoreException(
					"Não foi possível salvar o recurso " + resourceName + ". " + e.getMessage(), e);
		}
	}

	@Override
	public Resource refreshResource(IResource resource) {
//		this.refresh((Resource) resource);
		return (Resource) resource;
	}

}
