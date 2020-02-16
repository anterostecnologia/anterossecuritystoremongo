package br.com.anteros.security.store.mongo.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.stereotype.Service;

import br.com.anteros.core.utils.SerializationUtils;
import br.com.anteros.nosql.persistence.mongodb.query.MongoCriteria;
import br.com.anteros.nosql.persistence.mongodb.query.MongoQuery;
import br.com.anteros.security.store.SecurityDataStore;
import br.com.anteros.security.store.domain.IAction;
import br.com.anteros.security.store.domain.IResource;
import br.com.anteros.security.store.domain.ISystem;
import br.com.anteros.security.store.domain.IUser;
import br.com.anteros.security.store.exception.AnterosSecurityStoreException;
import br.com.anteros.security.store.mongo.domain.AccessToken;
import br.com.anteros.security.store.mongo.domain.Action;
import br.com.anteros.security.store.mongo.domain.Approval;
import br.com.anteros.security.store.mongo.domain.Client;
import br.com.anteros.security.store.mongo.domain.RefreshToken;
import br.com.anteros.security.store.mongo.domain.System;
import br.com.anteros.security.store.mongo.domain.User;
import br.com.anteros.security.store.mongo.exception.NoSQLStoreException;
import br.com.anteros.security.store.mongo.repository.AccessTokenRepository;
import br.com.anteros.security.store.mongo.repository.ActionRepository;
import br.com.anteros.security.store.mongo.repository.ApprovalRepository;
import br.com.anteros.security.store.mongo.repository.ClientRepository;
import br.com.anteros.security.store.mongo.repository.RefreshTokenRepository;
import br.com.anteros.security.store.mongo.repository.ResourceRepository;
import br.com.anteros.security.store.mongo.repository.SecurityRepository;
import br.com.anteros.security.store.mongo.repository.SystemRepository;

@Service("securityDataStore")
@Scope("prototype")
public class MongoSecurityDataStore implements SecurityDataStore {

	@Autowired
	protected SecurityRepository securityRepositoryMongo;

	@Autowired
	protected SystemRepository systemRepositoryMongo;

	@Autowired
	protected ResourceRepository resourceRepositoryMongo;

	@Autowired
	protected ActionRepository actionRepositoryMongo;

	@Autowired
	protected ApprovalRepository approvalRepositoryMongo;

	@Autowired
	protected AccessTokenRepository accessTokenRepositoryMongo;

	@Autowired
	protected RefreshTokenRepository refreshTokenRepositoryMongo;

	@Autowired
	protected ClientRepository clientRepository;

	private boolean handleRevocationsAsExpiry = false;

	public IResource getResourceByName(String systemName, String resourceName) {
		ISystem system = this.getSystemByName(systemName);
		return resourceRepositoryMongo.getResourceByName(system, resourceName);
	}

	public ISystem getSystemByName(String systemName) {
		return systemRepositoryMongo.getSystemByName(systemName);
	}

	public ISystem addSystem(String systemName, String description) {
		return systemRepositoryMongo.addSystem(systemName, description);
	}

	public IResource addResource(ISystem system, String resourceName, String description) {
		return resourceRepositoryMongo.addResource(system, resourceName, description);
	}

	public IAction addAction(ISystem system, IResource resource, String actionName, String category, String description,
			String version) throws Exception {
		return actionRepositoryMongo.addAction(system, resource, actionName, category, description, version);
	}

	public IAction saveAction(IAction action) throws Exception {
		return actionRepositoryMongo.saveAction((Action) action);
	}

	public IResource refreshResource(IResource resource) throws Exception {
		return resourceRepositoryMongo.refreshResource(resource);
	}

	public void removeActionByAllUsers(IAction act) throws Exception {
		actionRepositoryMongo.removeActionByAllUsers(act);
	}

	public IUser getUserByUserName(String username) {
		return securityRepositoryMongo.getUserByUserName(username);
	}

	@Override
	public void addApprovals(Collection<org.springframework.security.oauth2.provider.approval.Approval> approvals) {
		final Collection<Approval> mongoApprovals = transformToApproval(approvals);
		approvalRepositoryMongo.updateOrCreate(mongoApprovals);
	}

	private Collection<Approval> transformToApproval(
			Collection<org.springframework.security.oauth2.provider.approval.Approval> approvals) {
		Collection<Approval> result = new ArrayList<>();
		for (org.springframework.security.oauth2.provider.approval.Approval app : approvals) {
			result.add(new Approval(app.getExpiresAt(), app.getStatus() + "", app.getLastUpdatedAt(), app.getUserId(),
					app.getClientId(), app.getScope()));
		}
		return result;
	}

	@Override
	public void revokeApprovals(Collection<org.springframework.security.oauth2.provider.approval.Approval> approvals) {
		final Collection<Approval> tmpApprovals = transformToApproval(approvals);

		for (final Approval mongoApproval : tmpApprovals) {
			if (handleRevocationsAsExpiry) {
				approvalRepositoryMongo.updateExpiresAt(new Date(), mongoApproval);
			} else {
				approvalRepositoryMongo.deleteByUserIdAndClientIdAndScope(mongoApproval);
			}
		}
	}

	@Override
	public Collection<org.springframework.security.oauth2.provider.approval.Approval> getApprovals(String userId,
			String clientId) {
		final List<Approval> approvals = approvalRepositoryMongo.findByUserIdAndClientId(userId, clientId);
		return transformToSecurityApprovals(approvals);
	}

	private Collection<org.springframework.security.oauth2.provider.approval.Approval> transformToSecurityApprovals(
			List<Approval> approvals) {
		Collection<org.springframework.security.oauth2.provider.approval.Approval> result = new ArrayList<>();

		for (Approval app : approvals) {
			org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus status = app.getStatus()
					.equals(org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus.APPROVED
							.toString())
									? org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus.APPROVED
									: org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus.DENIED;
			result.add(new org.springframework.security.oauth2.provider.approval.Approval(app.getUserId(),
					app.getClientId(), app.getScope(), app.getExpiresAt(), status, app.getLastModifiedAt()));
		}
		return result;
	}

	@Override
	public void setHandleRevocationsAsExpiry(boolean handleRevocationsAsExpiry) {
		this.handleRevocationsAsExpiry = handleRevocationsAsExpiry;
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication,
			AuthenticationKeyGenerator authenticationKeyGenerator) {
		String refreshToken = null;
		if (Objects.nonNull(token.getRefreshToken())) {
			refreshToken = token.getRefreshToken().getValue();
		}

		if (Objects.nonNull(readAccessToken(token.getValue()))) {
			removeAccessToken(token.getValue());
		}

		final String tokenKey = extractTokenKey(token.getValue());

		final AccessToken oAuth2AccessToken = new AccessToken(tokenKey, serializeAccessToken(token),
				authenticationKeyGenerator.extractKey(authentication),
				authentication.isClientOnly() ? null : authentication.getName(),
				authentication.getOAuth2Request().getClientId(), serializeAuthentication(authentication),
				extractTokenKey(refreshToken));

		accessTokenRepositoryMongo.save(oAuth2AccessToken);
	}

	@Override
	public OAuth2Authentication readAuthentication(String token) {
		final String tokenId = extractTokenKey(token);

		final AccessToken accessToken = accessTokenRepositoryMongo.findByTokenId(tokenId);

		if (Objects.nonNull(accessToken)) {
			try {
				return deserializeAuthentication(accessToken.getAuthentication());
			} catch (IllegalArgumentException e) {
				removeAccessToken(token);
			}
		}

		return null;
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		final String tokenKey = extractTokenKey(tokenValue);
		final AccessToken AccessToken = accessTokenRepositoryMongo.findByToken(tokenKey);
		if (Objects.nonNull(AccessToken)) {
			try {
				return deserializeAccessToken(AccessToken.getToken());
			} catch (IllegalArgumentException e) {
				removeAccessToken(tokenValue);
			}
		}
		return null;
	}

	private void removeAccessToken(final String tokenValue) {
		final String tokenKey = extractTokenKey(tokenValue);
		accessTokenRepositoryMongo.deleteByTokenId(tokenKey);
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken token) {
		removeAccessToken(token.getValue());
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication oAuth2Authentication) {
		final String tokenKey = extractTokenKey(refreshToken.getValue());
		final byte[] token = serializeRefreshToken(refreshToken);
		final byte[] authentication = serializeAuthentication(oAuth2Authentication);

		final RefreshToken oAuth2RefreshToken = new RefreshToken(tokenKey, token, authentication);

		refreshTokenRepositoryMongo.save(oAuth2RefreshToken);
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
		final String tokenKey = extractTokenKey(tokenValue);
		final RefreshToken refreshToken = refreshTokenRepositoryMongo.findByTokenId(tokenKey);

		if (Objects.nonNull(refreshToken)) {
			try {
				return deserializeRefreshToken(refreshToken.getToken());
			} catch (IllegalArgumentException e) {
				removeRefreshToken(tokenValue);
			}
		}

		return null;
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
		return readAuthenticationForRefreshToken(token.getValue());
	}

	public OAuth2Authentication readAuthenticationForRefreshToken(final String value) {
		final String tokenId = extractTokenKey(value);

		final RefreshToken mongoOAuth2RefreshToken = refreshTokenRepositoryMongo.findByTokenId(tokenId);

		if (Objects.nonNull(mongoOAuth2RefreshToken)) {
			try {
				return deserializeAuthentication(mongoOAuth2RefreshToken.getAuthentication());
			} catch (IllegalArgumentException e) {
				removeRefreshToken(value);
			}
		}
		return null;
	}

	private void removeRefreshToken(final String token) {
		final String tokenId = extractTokenKey(token);
		refreshTokenRepositoryMongo.deleteByTokenId(tokenId);
	}

	private void removeAccessTokenUsingRefreshToken(final String refreshToken) {
		final String tokenId = extractTokenKey(refreshToken);
		accessTokenRepositoryMongo.deleteByRefreshTokenId(tokenId);
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken token) {
		removeRefreshToken(token.getValue());
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		removeAccessTokenUsingRefreshToken(refreshToken.getValue());
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication,
			AuthenticationKeyGenerator authenticationKeyGenerator) {
		OAuth2AccessToken accessToken = null;

		String key = authenticationKeyGenerator.extractKey(authentication);
		final AccessToken oAuth2AccessToken = accessTokenRepositoryMongo.findByAuthenticationId(key);
		if (oAuth2AccessToken != null) {
			accessToken = deserializeAccessToken(oAuth2AccessToken.getToken());
		}
		if (accessToken != null
				&& !key.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
			removeAccessToken(accessToken.getValue());
			storeAccessToken(accessToken, authentication, authenticationKeyGenerator);
		}
		return accessToken;
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
		final List<AccessToken> oAuth2AccessTokens = accessTokenRepositoryMongo.findByUsernameAndClientId(userName,
				clientId);
		return transformToOAuth2AccessTokens(oAuth2AccessTokens);
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		final List<AccessToken> oAuth2AccessTokens = accessTokenRepositoryMongo.findByClientId(clientId);
		return transformToOAuth2AccessTokens(oAuth2AccessTokens);
	}

	protected String extractTokenKey(final String value) {
		if (Objects.isNull(value)) {
			return null;
		}
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
		}

		try {
			byte[] bytes = digest.digest(value.getBytes("UTF-8"));
			return String.format("%032x", new BigInteger(1, bytes));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
		}
	}

	protected byte[] serializeAccessToken(OAuth2AccessToken token) {
		return SerializationUtils.serialize(token);
	}

	protected byte[] serializeRefreshToken(OAuth2RefreshToken token) {
		return SerializationUtils.serialize(token);
	}

	protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
		return SerializationUtils.serialize(authentication);
	}

	protected OAuth2AccessToken deserializeAccessToken(final byte[] token) {
		return SerializationUtils.deserialize(token);
	}

	protected OAuth2RefreshToken deserializeRefreshToken(final byte[] token) {
		return SerializationUtils.deserialize(token);
	}

	protected OAuth2Authentication deserializeAuthentication(final byte[] authentication) {
		return SerializationUtils.deserialize(authentication);
	}

	private Collection<OAuth2AccessToken> transformToOAuth2AccessTokens(final List<AccessToken> oAuth2AccessTokens) {
		return oAuth2AccessTokens.stream().filter(Objects::nonNull)
				.map(token -> SerializationUtils.<OAuth2AccessToken>deserialize(token.getToken()))
				.collect(Collectors.toList());
	}

	@Override
	public void addClientDetails(ClientDetails clientDetails) {
		Client client = new Client();
		client.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
		client.setAdditionalInformation(clientDetails.getAdditionalInformation());
		client.setAuthorities(
				clientDetails.getAuthorities().stream().map(i -> i.getAuthority()).collect(Collectors.toList()));
		client.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes());
		client.setClientId(clientDetails.getClientId());
		client.setClientSecret(clientDetails.getClientSecret());
		client.setRegisteredRedirectUris(clientDetails.getRegisteredRedirectUri());
		client.setResourceIds(clientDetails.getResourceIds());
		client.setScope(clientDetails.getScope());
		clientRepository.saveClient(client);
	}

	@Override
	public void updateClientDetails(ClientDetails clientDetails) {
		Optional<Client> client = clientRepository.findById(clientDetails.getClientId());
		if (!client.isPresent()) {
			throw new NoSQLStoreException("Client " + clientDetails.getClientId() + " não encontrado.");
		}
		Client clientToSave = client.get();
		clientToSave.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
		clientToSave.setAdditionalInformation(clientDetails.getAdditionalInformation());
		clientToSave.setAuthorities(
				clientDetails.getAuthorities().stream().map(i -> i.getAuthority()).collect(Collectors.toList()));
		clientToSave.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes());
		clientToSave.setClientSecret(clientDetails.getClientSecret());
		clientToSave.setRegisteredRedirectUris(clientDetails.getRegisteredRedirectUri());
		clientToSave.setResourceIds(clientDetails.getResourceIds());
		clientToSave.setScope(clientDetails.getScope());
		clientRepository.saveClient(clientToSave);
	}

	@Override
	public void updateClientSecret(String clientId, String secret) {
		Optional<Client> client = clientRepository.findById(clientId);
		if (!client.isPresent()) {
			throw new NoSQLStoreException("Client " + clientId + " não encontrado.");
		}
		Client clientToSave = client.get();
		clientToSave.setClientSecret(secret);
		clientRepository.saveClient(clientToSave);

	}

	@Override
	public void removeClientDetails(String clientId) {
		clientRepository.removeById(clientId);
	}

	@Override
	public List<ClientDetails> listClientDetails() {
		Iterable<Client> iterable = clientRepository.findAll();
		List<ClientDetails> result = new ArrayList<>();
		for (Client client : iterable) {
			BaseClientDetails bc = new BaseClientDetails();
			bc.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
			bc.setAdditionalInformation(client.getAdditionalInformation());
			bc.setAuthorities(client.getAuthorities().stream().map(item -> new SimpleGrantedAuthority(item))
					.collect(Collectors.toList()));
			bc.setAuthorizedGrantTypes(client.getAuthorizedGrantTypes());
			bc.setAutoApproveScopes(client.getAutoApproveScopes());
			bc.setClientSecret(client.getClientSecret());
			bc.setClientId(client.getClientId());
			bc.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
			bc.setRegisteredRedirectUri(client.getRegisteredRedirectUris());
			bc.setResourceIds(client.getResourceIds());
			bc.setScope(client.getScope());
			result.add(bc);
		}
		return result;
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) {
		MongoQuery query = MongoQuery.of(MongoCriteria.where(Client.CLIENT_ID).is(clientId));
		Iterable<Client> result = clientRepository.find(query);
		if (result.iterator().hasNext()) {
			Client client = result.iterator().next();
			BaseClientDetails bc = new BaseClientDetails();
			bc.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
			if (client.getAdditionalInformation() != null) {
				bc.setAdditionalInformation(client.getAdditionalInformation());
			}
			if (client.getAuthorities() != null) {
				bc.setAuthorities(client.getAuthorities().stream().map(item -> new SimpleGrantedAuthority(item))
						.collect(Collectors.toList()));
			}
			if (client.getAuthorizedGrantTypes() != null) {
				bc.setAuthorizedGrantTypes(client.getAuthorizedGrantTypes());
			}
			if (client.getAutoApproveScopes() != null) {
				bc.setAutoApproveScopes(client.getAutoApproveScopes());
			}
			bc.setClientSecret(client.getClientSecret());
			bc.setClientId(client.getClientId());
			bc.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
			if (client.getRegisteredRedirectUris() != null) {
				bc.setRegisteredRedirectUri(client.getRegisteredRedirectUris());
			}
			if (client.getResourceIds() != null) {
				bc.setResourceIds(client.getResourceIds());
			}
			bc.setScope(client.getScope());
			return bc;
		}
		return null;
	}

	@Override
	public void initializeCurrentSession() throws Exception {
		
	}

	@Override
	public void clearCurrentSession() throws Exception {
		
		
	}


}
