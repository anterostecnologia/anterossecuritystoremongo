package br.com.anteros.security.store.mongo.repository;

import br.com.anteros.nosql.persistence.session.repository.NoSQLRepository;
import br.com.anteros.security.store.mongo.domain.RefreshToken;

public interface RefreshTokenRepository  extends NoSQLRepository<RefreshToken, Long> {
	
	RefreshToken findByTokenId(String tokenId);

    boolean deleteByTokenId(String tokenId);


}
