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
import br.com.anteros.security.store.mongo.domain.RefreshToken;

@Repository("refreshTokenRepositoryMongo")
@Scope("prototype")
public class RefreshTokenRepositoryImpl extends MongoSimpleRepository<RefreshToken, Long> implements RefreshTokenRepository {

	@Autowired
	public RefreshTokenRepositoryImpl(@Qualifier("securitySessionFactory") NoSQLSessionFactory sessionFactory)
			throws Exception {
		super(sessionFactory);
	}

	@Override
	public RefreshToken findByTokenId(String tokenId) {
		MongoQuery query = MongoQuery.of(MongoCriteria.where(RefreshToken.TOKEN_ID).is(tokenId));
		List<RefreshToken> result = this.find(query);
		if (result.size()>0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public boolean deleteByTokenId(String tokenId) {
		try {
			this.getSession().getTransaction().begin();
			RefreshToken refreshToken = findByTokenId(tokenId);
			if (refreshToken!=null) {
				this.remove(refreshToken);
			}
			this.getSession().getTransaction().commit();
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			throw new AnterosSecurityStoreException(e);
		}		
		return true;
	}

}
