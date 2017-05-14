package models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Account extends Model{
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "real_number")
	private String realPhoneNumber;
	
	@Column(name = "proxy_number")
	private String proxyPhoneNumber;
	
	@ManyToOne
	@JoinColumn(name="language_id")
	private Language preferredLanguage;
	
	@ManyToOne
	@JoinColumn(name="linked_account_id")
	private Account linked_account;
	
	
	public Account(String realPhoneNumber, String proxyPhoneNumber, Language preferredLanguage, Account linked_account) {
		super();
		
		this.realPhoneNumber = realPhoneNumber;
		this.proxyPhoneNumber = proxyPhoneNumber;
		this.preferredLanguage = preferredLanguage;
		this.linked_account = linked_account;
		this.uuid = UUID.randomUUID().toString();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRealPhoneNumber() {
		return realPhoneNumber;
	}

	public void setRealPhoneNumber(String realPhoneNumber) {
		this.realPhoneNumber = realPhoneNumber;
	}

	public String getProxyPhoneNumber() {
		return proxyPhoneNumber;
	}

	public void setProxyPhoneNumber(String proxyPhoneNumber) {
		this.proxyPhoneNumber = proxyPhoneNumber;
	}

	public Language getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(Language preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public Account getLinked_account() {
		return linked_account;
	}

	public void setLinked_account(Account linked_account) {
		this.linked_account = linked_account;
	}
	
	
}
