package models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import play.db.jpa.Model;
import utilities.WebUtils;

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
	
	public static Account createNewAccount(JsonObject requestBody, String realNumber, Account linkedAccount){
		String fakeNumber = getFakeNumber();
		
		
		String languageString = "en";
		Language language = Language.find("name = ?1 OR language_code = ?2", languageString.toLowerCase(), languageString.toLowerCase()).first();
		
		if (language == null){
			//TODO handle this case
			return null;
		}
		
		if(fakeNumber == null){
			return null;
		}
		
		WebUtils.createNewPhoneNumber(fakeNumber);
		
		Account account = new Account(realNumber, fakeNumber, language, linkedAccount);
		account.save();
		
		return account;
	}
	
	private static String getFakeNumber() {
		JsonObject availableNumbers = WebUtils.getAvailableNumbers();
		
		JsonArray numbers = availableNumbers.get("numbers").getAsJsonArray();
		
		for(JsonElement numberJsonElement: numbers){
			JsonObject numberJsonObject = numberJsonElement.getAsJsonObject();
			
			String msisdn = numberJsonObject.get("msisdn").getAsString();
			return msisdn;
		}
		
		return null;
	}
	
	
}
