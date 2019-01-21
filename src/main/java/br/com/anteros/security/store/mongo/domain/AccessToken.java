package br.com.anteros.security.store.mongo.domain;

import java.io.Serializable;

import br.com.anteros.nosql.persistence.metadata.annotations.Entity;
import br.com.anteros.nosql.persistence.metadata.annotations.Id;
import br.com.anteros.nosql.persistence.metadata.annotations.Lob;
import br.com.anteros.nosql.persistence.metadata.annotations.Property;

/**
 * Token de acesso
 * 
 * @author Edson Martins edsonmartins2005@gmail.com
 */
@Entity(value = "TOKEN_ACESSO")
public class AccessToken implements Serializable {

	public static final String TOKEN_ID = "tokenId";
	public static final String REFRESH_TOKEN = "refreshToken";
	public static final String AUTHENTICATION_ID = "authenticationId";
	public static final String CLIENT_ID = "clientId";
	public static final String USERNAME = "username";

	/*
	 * Identificador do token de acesso
	 */
	@Id
	private String id;

	@Property(value = TOKEN_ID, required = true)
	private String tokenId;

	@Lob(value = "token")
	private byte[] token;

	@Property(value = AUTHENTICATION_ID)
	private String authenticationId;

	@Property(value = USERNAME)
	private String username;

	@Property(value = CLIENT_ID)
	private String clientId;

	@Lob(value = "authentication")
	private byte[] authentication;

	@Property(value = REFRESH_TOKEN)
	private String refreshToken;

	public AccessToken() {

	}

	public AccessToken(String tokenId, byte[] token, String authenticationId, String username, String clientId,
			byte[] authentication, String refreshToken) {
		super();
		this.tokenId = tokenId;
		this.token = token;
		this.authenticationId = authenticationId;
		this.username = username;
		this.clientId = clientId;
		this.authentication = authentication;
		this.refreshToken = refreshToken;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public byte[] getToken() {
		return token;
	}

	public void setToken(byte[] token) {
		this.token = token;
	}

	public String getAuthenticationId() {
		return authenticationId;
	}

	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public byte[] getAuthentication() {
		return authentication;
	}

	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokenId == null) ? 0 : tokenId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccessToken other = (AccessToken) obj;
		if (tokenId == null) {
			if (other.tokenId != null)
				return false;
		} else if (!tokenId.equals(other.tokenId))
			return false;
		return true;
	}

}
