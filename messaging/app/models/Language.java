package models;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import play.db.jpa.Model;

@Entity
public class Language extends Model{
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "language_code")
	private String languageCode;
	
	@Column(name = "name")
	private String name;
	
	
	

	public Language(String languageCode, String name) {
		super();
		
		this.uuid = UUID.randomUUID().toString();
		this.languageCode = languageCode;
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static JsonObject toJsonArray(List<Language> languages) {
		JsonObject output = new JsonObject();
		
		JsonArray array = new JsonArray();
		
		for (Language lang: languages){
			array.add(lang.toJsonObject());	
		}
		
		output.add("languages", array);
		return output;
	}

	public JsonObject toJsonObject() {
		JsonObject output = new JsonObject();
		
		output.addProperty("name", this.name);
		output.addProperty("language_code", this.languageCode);
		output.addProperty("uuid",this.uuid);
		
		return output;
	}

}
