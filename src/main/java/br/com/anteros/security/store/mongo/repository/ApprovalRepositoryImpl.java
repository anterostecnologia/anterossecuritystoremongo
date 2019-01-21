package br.com.anteros.security.store.mongo.repository;

import java.util.Collection;
import java.util.Date;
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
import br.com.anteros.security.store.mongo.domain.Approval;
import br.com.anteros.security.store.mongo.exception.NoSQLStoreException;

@Repository("approvalRepositoryMongo")
@Scope("prototype")
public class ApprovalRepositoryImpl extends MongoSimpleRepository<Approval, Long> implements ApprovalRepository {

	@Autowired
	public ApprovalRepositoryImpl(@Qualifier("securitySessionFactory") NoSQLSessionFactory sessionFactory)
			throws Exception {
		super(sessionFactory);
	}

	@Override
	public void updateOrCreate(Collection<Approval> approvals) {
		try {
			this.getSession().getTransaction().begin();
			save(approvals);
			this.getSession().getTransaction().commit();
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			throw new AnterosSecurityStoreException(e);
		}		
	}

	@Override
	public void updateExpiresAt(Date now, Approval approval) {
		try {
			this.getSession().getTransaction().begin();
			MongoQuery query = MongoQuery.of(MongoCriteria.where(Approval.USER_ID).is(approval.getUserId())
					.and(Approval.CLIENT_ID).is(approval.getClientId()).and(Approval.SCOPE).is(approval.getScope()));

			List<Approval> resultList = this.find(query);
			for (Approval app : resultList) {
				app.setExpiresAt(now);
			}
			save(resultList);
			this.getSession().getTransaction().commit();
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			throw new AnterosSecurityStoreException(e);
		}		
	}

	@Override
	public void deleteByUserIdAndClientIdAndScope(Approval approval) {
		try {
			this.getSession().getTransaction().begin();
			MongoQuery query = MongoQuery.of(MongoCriteria.where(Approval.USER_ID).is(approval.getUserId())
					.and(Approval.CLIENT_ID).is(approval.getClientId()).and(Approval.SCOPE).is(approval.getScope()));
			List<Approval> resultList = this.find(query);
			remove(resultList);
			this.getSession().getTransaction().commit();
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			throw new AnterosSecurityStoreException(e);
		}
	}

	@Override
	public List<Approval> findByUserIdAndClientId(String userId, String clientId) {
		try {
			MongoQuery query = MongoQuery.of(MongoCriteria.where(Approval.USER_ID).is(userId)
					.and(Approval.CLIENT_ID).is(clientId));
			return this.find(query);
		} catch (Exception e) {
			throw new NoSQLStoreException("Erro criando consulta para remove approvals. ", e);
		}
	}

}
