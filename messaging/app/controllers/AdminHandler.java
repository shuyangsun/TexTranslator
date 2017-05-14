package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import models.Account;
import models.Language;
import utilities.WebUtils;

public class AdminHandler {
	final private static String adminNumber = "12035338290";

	public static void process(JsonObject requestBody, String realNumber, String proxyNumber) {
		Account existingAccount = Account.find("real_number = ?1", realNumber).first();
		
		if(existingAccount != null){
			WebUtils.sendMessage(adminNumber, realNumber,"You already have an account =D");
			return;
		}
		
		else{
			String newNumber = createNewAccount(requestBody, realNumber, proxyNumber);
			if(newNumber != null){
				WebUtils.sendMessage(adminNumber, realNumber,"Hi! Your new number is " + newNumber);
			}
			
		}
		
		
	}

	
	private static String createNewAccount(JsonObject requestBody, String realNumber, String proxyNumber){
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
		
		Account account = new Account(realNumber, fakeNumber, language, null);
		account.save();
		
		return fakeNumber;
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
