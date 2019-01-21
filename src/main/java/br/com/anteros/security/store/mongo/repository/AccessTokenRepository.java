package br.com.anteros.security.store.mongo.repository;

import java.util.List;

import br.com.anteros.nosql.persistence.session.repository.NoSQLRepository;
import br.com.anteros.security.store.mongo.domain.AccessToken;


public interface AccessTokenRepository  extends NoSQLRepository<AccessToken, Long> {

	AccessToken findByToken(String tokenKey);

	AccessToken findByAuthenticationId(String key);

	List<AccessToken> findByUsernameAndClientId(String userName, String clientId);

	List<AccessToken> findByClientId(String clientId);

	void deleteByRefreshTokenId(String refreshTokenId);

	void deleteByTokenId(String tokenId);

	AccessToken findByTokenId(String tokenId);
	



}
