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
import br.com.anteros.security.store.mongo.domain.AccessToken;

@Repository("accessTokenRepositoryMongo")
@Scope("prototype")
public class AccessTokenRepositoryImpl extends MongoSimpleRepository<AccessToken, Long>
		implements AccessTokenRepository {

	public static final String ID = "_id";

	@Autowired
	public AccessTokenRepositoryImpl(@Qualifier("securitySessionFactory") NoSQLSessionFactory sessionFactory)
			throws Exception {
		super(sessionFactory);
	}

	@Override
	public AccessToken findByToken(String tokenKey) {
		final MongoQuery query = MongoQuery.of(MongoCriteria.where(AccessToken.TOKEN_ID).is(tokenKey));
		List<AccessToken> result = this.find(query);
		if (result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public AccessToken findByAuthenticationId(String authenticationId) {
		final MongoQuery query = MongoQuery.of(MongoCriteria.where(AccessToken.AUTHENTICATION_ID).is(authenticationId));
		List<AccessToken> result = this.find(query);
		if (result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public List<AccessToken> findByUsernameAndClientId(String userName, String clientId) {
		final MongoQuery query = MongoQuery
				.of(MongoCriteria.where(AccessToken.USERNAME).is(userName).and(AccessToken.CLIENT_ID).is(clientId));
		return this.find(query);
	}

	@Override
	public List<AccessToken> findByClientId(String clientId) {
		final MongoQuery query = MongoQuery.of(MongoCriteria.where(AccessToken.CLIENT_ID).is(clientId));
		return this.find(query);
	}

	@Override
	public void deleteByRefreshTokenId(String refreshTokenId) {
		try {
			this.getSession().getTransaction().begin();
			final MongoQuery query = MongoQuery.of(MongoCriteria.where(AccessToken.REFRESH_TOKEN).is(refreshTokenId));
			List<AccessToken> result = this.find(query);
			if (result.size() > 0) {
				this.remove(result);
			}
			this.getSession().getTransaction().commit();
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			throw new AnterosSecurityStoreException(e);
		}
	}

	@Override
	public void deleteByTokenId(String tokenId) {
		try {
			this.getSession().getTransaction().begin();
			final MongoQuery query = MongoQuery.of(MongoCriteria.where(AccessToken.TOKEN_ID).is(tokenId));
			List<AccessToken> result = this.find(query);
			if (result.size() > 0) {
				this.remove(result);
			}
			this.getSession().getTransaction().commit();
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			throw new AnterosSecurityStoreException(e);
		}
	}

	@Override
	public AccessToken findByTokenId(String tokenId) {
		final MongoQuery query = MongoQuery.of(MongoCriteria.where(AccessToken.TOKEN_ID).is(tokenId));
		List<AccessToken> result = this.find(query);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

}
