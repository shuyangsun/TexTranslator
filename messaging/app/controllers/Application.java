package controllers;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.Account;
import models.Language;
import utilities.WebUtils;
import play.mvc.Controller;

public class Application extends Controller {

	final private static String adminNumber = "12035338290";
	
    public static void index() {
        render();
    }
    
    
    public static void getLanguages(){
    	List<Language> languages = Language.findAll();
    	
    	renderJSON(Language.toJsonArray(languages));
    }
    
    public static void getNumbers(){
    	renderJSON(WebUtils.getAvailableNumbers());
    	
    }

    public static void createLanguage(){
		JsonParser parser = new JsonParser();
		
    	String requestBodyString = params.get("body");
		
		JsonObject requestBody = (requestBodyString != null) ? parser.parse(requestBodyString).getAsJsonObject() : null;
		
		if(requestBody == null){
			JsonObject output = new JsonObject();
			output.addProperty("error", "Invalid Request Body");
			renderJSON(output.toString());
		}
    	
		String language = (requestBody.has("language")) ? requestBody.get("language").getAsString() : null;
		String languageCode = (requestBody.has("language_code")) ? requestBody.get("language_code").getAsString() : null;
		
		if(language != null && languageCode != null){
			Language lang = new Language(languageCode, language);
			
			lang.save();
			renderJSON(lang.toJsonObject());
		}
		
		JsonObject output = new JsonObject();
		output.addProperty("error", "Invalid Request Body");
		renderJSON(output.toString());
    }
    
    
    public static void receiveWebhookRequest(){
    	JsonParser parser = new JsonParser();
		
    	String requestBodyString = params.get("body");
    	System.out.println(requestBodyString);
    	JsonObject requestBody = (requestBodyString != null) ? parser.parse(requestBodyString).getAsJsonObject() : null;
		
    	String senderNumber = requestBody.get("msisdn").getAsString();
    	String receivingNumber = requestBody.get("to").getAsString();
    	
    	if(receivingNumber.equals(adminNumber)){
    		AdminHandler.process(requestBody, senderNumber, receivingNumber);
    	}
    	else{
    		ProxyHandler.process(requestBody,senderNumber,receivingNumber);
    	}
    	
    	renderJSON(requestBody);
    	
    }
    
    
    
    public static void createAccount(){
    	JsonParser parser = new JsonParser();
		
    	String requestBodyString = params.get("body");
		
		JsonObject requestBody = (requestBodyString != null) ? parser.parse(requestBodyString).getAsJsonObject() : null;
		
		if(requestBody == null){
			JsonObject output = new JsonObject();
			output.addProperty("error", "Invalid Request Body");
			renderJSON(output.toString());
		}
    	
		String languageString = (requestBody.has("language")) ? requestBody.get("language").getAsString() : null;
		Language language = Language.find("name =?1", languageString.toLowerCase()).first();
		
		if (language == null){
			//TODO handle this case
			return;
		}
    }
}