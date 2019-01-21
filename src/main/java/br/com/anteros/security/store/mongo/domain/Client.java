package br.com.anteros.security.store.mongo.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.anteros.nosql.persistence.metadata.annotations.Embedded;
import br.com.anteros.nosql.persistence.metadata.annotations.Entity;
import br.com.anteros.nosql.persistence.metadata.annotations.Id;
import br.com.anteros.nosql.persistence.metadata.annotations.Property;
import br.com.anteros.security.store.domain.IClient;

@Entity(value = "CLIENTE")
public class Client implements Serializable, IClient {

	public static final String CLIENT_ID = "clientId";

	@Id
	private String id;
	
	@Property(value=CLIENT_ID)
	private String clientId;
	
	@Property(value="clientSecret")
    private String clientSecret;
	
	@Property(value="clientDescription")
    private String clientDescription;
	
	@Embedded(value="scope")
    private Set<String> scope;
	
	@Embedded(value="resourceIds")
    private Set<String> resourceIds;
	
	@Embedded(value="authorizedGrantTypes")
    private Set<String> authorizedGrantTypes;
	
	@Embedded(value="registeredRedirectUris")
    private Set<String> registeredRedirectUris;
	
	@Embedded(value="authorities")
    private List<String> authorities;
	
	@Property(value="accessTokenValiditySeconds")
    private Integer accessTokenValiditySeconds;
	
	@Property(value="refreshTokenValiditySeconds")
    private Integer refreshTokenValiditySeconds;
	
	@Embedded
    private Map<String, Object> additionalInformation;
	
	@Embedded
    private Set<String> autoApproveScopes;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getClientDescription() {
		return clientDescription;
	}

	public void setClientDescription(String clientDescription) {
		this.clientDescription = clientDescription;
	}

	public Set<String> getScope() {
		return scope;
	}

	public void setScope(Set<String> scope) {
		this.scope = scope;
	}

	public Set<String> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(Set<String> resourceIds) {
		this.resourceIds = resourceIds;
	}

	public Set<String> getAuthorizedGrantTypes() {
		return authorizedGrantTypes;
	}

	public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
		this.authorizedGrantTypes = authorizedGrantTypes;
	}

	public Set<String> getRegisteredRedirectUris() {
		return registeredRedirectUris;
	}

	public void setRegisteredRedirectUris(Set<String> registeredRedirectUris) {
		this.registeredRedirectUris = registeredRedirectUris;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public Integer getAccessTokenValiditySeconds() {
		return accessTokenValiditySeconds;
	}

	public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	public Integer getRefreshTokenValiditySeconds() {
		return refreshTokenValiditySeconds;
	}

	public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}

	public Map<String, Object> getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(Map<String, Object> additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public Set<String> getAutoApproveScopes() {
		return autoApproveScopes;
	}

	public void setAutoApproveScopes(Set<String> autoApproveScopes) {
		this.autoApproveScopes = autoApproveScopes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
