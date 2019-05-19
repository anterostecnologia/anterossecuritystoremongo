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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import br.com.anteros.nosql.persistence.mongodb.query.MongoCriteria;
import br.com.anteros.nosql.persistence.mongodb.query.MongoQuery;
import br.com.anteros.nosql.persistence.mongodb.session.repository.MongoSimpleRepository;
import br.com.anteros.nosql.persistence.session.NoSQLSessionFactory;
import br.com.anteros.security.store.domain.IAction;
import br.com.anteros.security.store.domain.IResource;
import br.com.anteros.security.store.domain.ISystem;
import br.com.anteros.security.store.exception.AnterosSecurityStoreException;
import br.com.anteros.security.store.mongo.domain.Action;
import br.com.anteros.security.store.mongo.domain.Resource;
import br.com.anteros.security.store.mongo.domain.Security;

@Repository("actionRepositoryMongo")
@Scope("prototype")
public class ActionRepositoryImpl extends MongoSimpleRepository<Action, Long> implements ActionRepository {

	@Autowired
	protected SecurityRepository securityRepositoryNoSql;

	@Autowired
	public ActionRepositoryImpl(@Qualifier("securitySessionFactory") NoSQLSessionFactory sessionFactory)
			throws Exception {
		super(sessionFactory);
	}

	@Override
	public Action addAction(ISystem system, IResource resource, String actionName, String category, String description,
			String version) {
		try {
			this.getSession().getTransaction().begin();
			Action action = Action.of(actionName, description, category, resource, version);
			this.getSession().save(action);
			((Resource)resource).addAction(action);
			this.getSession().save(resource);			
			this.getSession().getTransaction().commit();
			return action;
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			throw new AnterosSecurityStoreException(e);
		}
	}

	@Override
	public void removeActionByAllUsers(IAction act) {
		try {
			this.getSession().getTransaction().begin();
			MongoQuery query = MongoQuery.of(MongoCriteria.where("actions._id").is(act.getActionId()));
			Iterable<Security> result = securityRepositoryNoSql.find(query);
			for (Security s : result) {
				s.getActions().clear();
				securityRepositoryNoSql.save(result);
			}
			this.getSession().getTransaction().commit();
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			throw new AnterosSecurityStoreException(e);
		}

	}

	@Override
	public IAction saveAction(Action action) {
		try {
			this.getSession().getTransaction().begin();
			this.save(action);
			this.getSession().getTransaction().commit();
			return action;
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			throw new AnterosSecurityStoreException(e);
		}
	}

}
