package br.com.anteros.security.store.mongo.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import br.com.anteros.nosql.persistence.session.repository.NoSQLRepository;
import br.com.anteros.security.store.mongo.domain.Approval;


public interface ApprovalRepository  extends NoSQLRepository<Approval, Long> {
	

	public void updateOrCreate(Collection<Approval> approvals) ;

	public void updateExpiresAt(Date now, Approval approval);

	public void deleteByUserIdAndClientIdAndScope(Approval approval) ;

	public List<Approval> findByUserIdAndClientId(String userId, String clientId);


}
