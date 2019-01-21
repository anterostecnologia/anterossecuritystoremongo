package br.com.anteros.security.store.mongo.domain;

import java.io.Serializable;
import java.util.Arrays;

import br.com.anteros.nosql.persistence.metadata.annotations.Entity;
import br.com.anteros.nosql.persistence.metadata.annotations.Id;
import br.com.anteros.nosql.persistence.metadata.annotations.Lob;
import br.com.anteros.nosql.persistence.metadata.annotations.Property;

/**
 * RefreshToken de acesso
 * 
 * @author Edson Martins edsonmartins2005@gmail.com
 */
@Entity(value = "REFRESH_TOKEN_ACESSO")
public class RefreshToken implements Serializable {

	public static final String AUTHENTICATION = "authentication";

	public static final String TOKEN = "token";

	public static final String TOKEN_ID = "tokenId";

	/*
	 * Identificador do token de acesso
	 */
	@Id
	private String id;

	@Property(value = TOKEN_ID)
	private String tokenId;

	@Lob(value = TOKEN)
	private byte[] token;

	@Lob(value = AUTHENTICATION)
	private byte[] authentication;

	public RefreshToken() {

	}

	public RefreshToken(String tokenId, byte[] token, byte[] authentication) {
		super();
		this.tokenId = tokenId;
		this.token = token;
		this.authentication = authentication;
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

	public byte[] getAuthentication() {
		return authentication;
	}

	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(authentication);
		result = prime * result + Arrays.hashCode(token);
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
		RefreshToken other = (RefreshToken) obj;
		if (!Arrays.equals(authentication, other.authentication))
			return false;
		if (!Arrays.equals(token, other.token))
			return false;
		if (tokenId == null) {
			if (other.tokenId != null)
				return false;
		} else if (!tokenId.equals(other.tokenId))
			return false;
		return true;
	}

}
