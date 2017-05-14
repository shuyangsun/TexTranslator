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
			String message = "You already have an account =D";
			
			if(!existingAccount.getPreferredLanguage().getLanguageCode().equals("en")){
				JsonObject body = new JsonObject();
				
				body.addProperty("src", "en");
				body.addProperty("dst", existingAccount.getPreferredLanguage().getLanguageCode());
				body.addProperty("text",message);
				message = WebUtils.getTranslation(body);
			}
			WebUtils.sendMessage(adminNumber, realNumber, message);
			return;
		}
		
		else{
			Account newNumber = Account.createNewAccount(requestBody, realNumber, null);
			
			if(newNumber != null){
				String message = "Hi! Your new phone number is " + newNumber.getProxyPhoneNumber();
			
				if(!newNumber.getPreferredLanguage().getLanguageCode().equals("en")){
					JsonObject body = new JsonObject();
					
					body.addProperty("src", "en");
					body.addProperty("dst", newNumber.getPreferredLanguage().getLanguageCode());
					body.addProperty("text",message);
					message = WebUtils.getTranslation(body);
				}
				
				WebUtils.sendMessage(adminNumber, realNumber, message);
			}
			
		}
		
		
	}


}
