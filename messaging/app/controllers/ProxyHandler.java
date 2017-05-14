package controllers;

import com.google.gson.JsonObject;

import models.Account;
import utilities.WebUtils;

public class ProxyHandler {

	public static void process(JsonObject requestBody, String senderNumber, String receivingNumber) {
		Account sendingAccount = Account.find("real_number = ?1", senderNumber).first();
		Account receivingAccount = Account.find("proxy_number = ?1", receivingNumber).first();
		
		if(receivingAccount == null){
			return;
		}
		
		if(sendingAccount == null){
			sendingAccount = Account.createNewAccount(requestBody, senderNumber, receivingAccount);

		}
		
		String message = requestBody.get("text").getAsString();
		
		System.out.println(message);
		
		WebUtils.sendMessage(sendingAccount.getProxyPhoneNumber(), receivingAccount.getRealPhoneNumber(), message);
		
		
	}

}
