package br.com.anteros.security.store.mongo.domain;

import java.io.Serializable;
import java.util.Date;

import br.com.anteros.nosql.persistence.metadata.annotations.Entity;
import br.com.anteros.nosql.persistence.metadata.annotations.Id;
import br.com.anteros.nosql.persistence.metadata.annotations.Property;
import br.com.anteros.nosql.persistence.metadata.annotations.Reference;
import br.com.anteros.nosql.persistence.metadata.annotations.Temporal;
import br.com.anteros.nosql.persistence.metadata.annotations.type.TemporalType;

/**
 * 
 * @author eduardogreco
 *
 */

@Entity(value = "SEGURANCASESSAO")
public class SecuritySession implements Serializable {

	/*
	 * Identificação do Objeto de sessão
	 */
	@Id
	private String id;

	/*
	 * Id. da sessão no banco de dados
	 */
	@Property(value = "databaseSessionId", required = true)
	private Long databaseSessionId;

	/*
	 * Usuário da sessão
	 */
	@Reference(lazy=true, required=true, value="user")
	private Security user;

	/*
	 * Data/hora do login da sessão
	 */
	@Temporal(type=TemporalType.DATE_TIME, value="dtLoginSession", required=true)
	private Date dtLoginSession;

	/*
	 * Data/hora do logout da sessão
	 */
	@Temporal(type=TemporalType.DATE_TIME, value="dtLogoutSession", required=true)
	private Date dtLogoutSession;

	/*
	 * Endereço IP
	 */
	@Property(value = "ipAddress", required = true)
	private String ipAddress;

	/*
	 * Nome do sistema
	 */
	@Property(value = "system", required = true)
	private String system;

	/*
	 * Versão do sistema
	 */
	@Property(value = "version", required = true)
	private String version;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getDatabaseSessionId() {
		return databaseSessionId;
	}

	public void setDatabaseSessionId(Long databaseSessionId) {
		this.databaseSessionId = databaseSessionId;
	}

	public Security getUser() {
		return user;
	}

	public void setUser(Security user) {
		this.user = user;
	}

	public Date getDtLoginSession() {
		return dtLoginSession;
	}

	public void setDtLoginSession(Date dtLoginSession) {
		this.dtLoginSession = dtLoginSession;
	}

	public Date getDtLogoutSession() {
		return dtLogoutSession;
	}

	public void setDtLogoutSession(Date dtLogoutSession) {
		this.dtLogoutSession = dtLogoutSession;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


}
