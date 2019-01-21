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
package br.com.anteros.security.store.mongo.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.anteros.nosql.persistence.metadata.annotations.DiscriminatorValue;
import br.com.anteros.nosql.persistence.metadata.annotations.Entity;
import br.com.anteros.nosql.persistence.metadata.annotations.Property;
import br.com.anteros.nosql.persistence.metadata.annotations.Reference;
import br.com.anteros.security.store.domain.IAction;
import br.com.anteros.security.store.domain.IProfile;
import br.com.anteros.security.store.domain.IUser;

/**
 * Usuario
 * 
 * Classe que representa um usuário dentro de um Sistema. Pode ser uma Pessoa ou até mesmo um usuário virtual como
 * o próprio Sistema.
 * 
 * @author Edson Martins edsonmartins2005@gmail.com
 */

@Entity(value="SEGURANCA")
@DiscriminatorValue(value = "USUARIO")
public class User extends Security implements IUser {

	public static final String SENHA = "senha";

	public static final String LOGIN = "login";

	/*
	 * Login do usuário
	 */
	@Property(value = LOGIN)
	private String login;

	/*
	 * Senha do usuário
	 */
	@Property(value = SENHA)
	private String password;

	/*
	 * O usuário deve alterar a senha no próximo Login?
	 */
	@Property(value = "changePasswordOnNextLogin", required = true, defaultValue = "false")
	private Boolean changePasswordOnNextLogin;

	/*
	 * O usuário pode alterar a senha?
	 */
	@Property(value = "allowChangePassword", required = true, defaultValue = "false")
	private Boolean allowChangePassword;

	/*
	 * Permite o usuário efetuar vários logins em um mesmo sistema?
	 */
	@Property(value = "allowMultipleLogins", required = true, defaultValue = "false")
	private Boolean allowMultipleLogins;

	/*
	 * A senha do usuário nunca expira?
	 */
	@Property(value = "passwordNeverExpire", required = true, defaultValue = "false")
	private Boolean passwordNeverExpire;

	/*
	 * Conta do usuário está desativada?
	 */
	@Property(value = "inactiveAccount", required = true, defaultValue = "false")
	private Boolean inactiveAccount=Boolean.FALSE;

	/*
	 * Conta do usuário está bloqueada?
	 */
	@Property(value = "blockedAccount", required = true, defaultValue = "false")
	private Boolean blockedAccount=Boolean.FALSE;
	
	
	@Property(value = "freeAccessTime", required = true, defaultValue = "false")
	private Boolean boFreeAccessTime;

	@Property(value = "administrator", required = true, defaultValue = "false")
	private Boolean boAdministrator;

	/*
	 * Horário de acesso do usuário
	 */
	@Reference(lazy=true, value="accessTime")
	private AccessTime accessTime;

	/*
	 * Grupos que o usuário é membro
	 */
	@Reference(lazy=true, value="groups")
	private List<Group> groups;

	/*
	 * Perfil (papel) do usuário dentro do sistema
	 */
	@Reference(lazy=true, value="profile")
	private Profile profile;

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
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
		User other = (User) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}

	public boolean isExpired() {
		return getPasswordNeverExpire();
	}

	public boolean isInactiveAccount() {
		return getInactiveAccount();
	}

	public boolean isBlockedAccount() {
		return getBlockedAccount();
	}

	public boolean isAdministrator() {
		return getBoAdministrator();
	}

	public Set<IAction> getActionList() {
		if (getActions()==null) {
			return new HashSet<>();
		}
		return getActions().stream().map(item -> (IAction)item).collect(Collectors.toSet());
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getChangePasswordOnNextLogin() {
		return changePasswordOnNextLogin;
	}

	public void setChangePasswordOnNextLogin(Boolean changePasswordOnNextLogin) {
		this.changePasswordOnNextLogin = changePasswordOnNextLogin;
	}

	public Boolean getAllowChangePassword() {
		return allowChangePassword;
	}

	public void setAllowChangePassword(Boolean allowChangePassword) {
		this.allowChangePassword = allowChangePassword;
	}

	public Boolean getAllowMultipleLogins() {
		return allowMultipleLogins;
	}

	public void setAllowMultipleLogins(Boolean allowMultipleLogins) {
		this.allowMultipleLogins = allowMultipleLogins;
	}

	public Boolean getPasswordNeverExpire() {
		return passwordNeverExpire;
	}

	public void setPasswordNeverExpire(Boolean passwordNeverExpire) {
		this.passwordNeverExpire = passwordNeverExpire;
	}

	public Boolean getInactiveAccount() {
		return inactiveAccount;
	}

	public void setInactiveAccount(Boolean inactiveAccount) {
		this.inactiveAccount = inactiveAccount;
	}

	public Boolean getBlockedAccount() {
		return blockedAccount;
	}

	public void setBlockedAccount(Boolean blockedAccount) {
		this.blockedAccount = blockedAccount;
	}

	public Boolean getBoFreeAccessTime() {
		return boFreeAccessTime;
	}

	public void setBoFreeAccessTime(Boolean boFreeAccessTime) {
		this.boFreeAccessTime = boFreeAccessTime;
	}

	public Boolean getBoAdministrator() {
		return boAdministrator;
	}

	public void setBoAdministrator(Boolean boAdministrator) {
		this.boAdministrator = boAdministrator;
	}

	public AccessTime getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(AccessTime accessTime) {
		this.accessTime = accessTime;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	@Override
	public boolean isPasswordNeverExpire() {
		if (passwordNeverExpire==null)
			return false;
		return passwordNeverExpire;
	}

	@Override
	public IProfile getUserProfile() {
		return (IProfile) profile;
	}


}