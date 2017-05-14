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
			Account newNumber = Account.createNewAccount(requestBody, realNumber, null);
			if(newNumber != null){
				WebUtils.sendMessage(adminNumber, realNumber,"Hi! Your new number is " + newNumber.getProxyPhoneNumber());
			}
			
		}
		
		
	}


}
