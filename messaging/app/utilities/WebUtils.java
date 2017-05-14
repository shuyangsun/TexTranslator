package utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;

public class WebUtils {

	private final static String nexmoApiKey = "ff176b14"; 
	private final static String nexmoApiSecret = "fd306fedca51b730"; 
	
	
	public static void createNewPhoneNumber(String msisdn) {
		String url = String.format("https://rest.nexmo.com/number/buy/%s/%s/US/%s", nexmoApiKey, nexmoApiSecret, msisdn);
		
		WSRequest request = WS.url(url);
		
		request.setHeader("Accept", "application/json");
		request.post();
		
		//TODO check the response header to make sure that the creation went through
	}
	
	public static JsonObject getAvailableNumbers(){
		String url = "https://rest.nexmo.com/number/search/" + nexmoApiKey + "/" + nexmoApiSecret + "/us";
		
		WSRequest request = WS.url(url);
		
		HttpResponse response = request.get();
		JsonObject listOfNumbers = response.getJson().getAsJsonObject();
		
		return listOfNumbers;	
	}
	
	public static void sendMessage(String sendingNumber, String receivingNumber, String message){
		message = message.replaceAll(" ","+");
		String url = String.format("https://rest.nexmo.com/sms/json?api_key=%s&api_secret=%s&to=%s&from=%s&text=%s&type=unicode", nexmoApiKey,nexmoApiSecret,receivingNumber,sendingNumber,message);
		
		WSRequest request = WS.url(url);
		HttpResponse response = request.post();
	}

	public static String getTranslation(JsonObject body) {
		String translationHost = "http://d46321ae.ngrok.io";
		String url = translationHost + "/translate";
		
		WSRequest request = WS.url(url);
		request.setHeader("Accept", "application/json");
		request.body = body;
		
		HttpResponse response = request.post();
		
		String responseString = response.getString();
		JsonParser parser = new JsonParser();
		
    	JsonObject responseJsonObject =  parser.parse(responseString).getAsJsonObject();
		
		return responseJsonObject.get("text").getAsString();
	}

	public static String detectLanguage(String langString) {
		JsonObject body = new JsonObject();
		
		body.addProperty("text", langString);
		
		String translationHost = "http://d46321ae.ngrok.io";
		String url = translationHost + "/detect";
		
		WSRequest request = WS.url(url);
		request.setHeader("Accept", "application/json");
		request.body = body;
		
		HttpResponse response = request.post();
		
		String responseString = response.getString();
		JsonParser parser = new JsonParser();
		
    	JsonObject responseJsonObject =  parser.parse(responseString).getAsJsonObject();
		System.out.println(responseString);
		return (responseJsonObject.has("text") && responseJsonObject.get("text").isJsonPrimitive()) ? 
				responseJsonObject.get("text").getAsString() : null;
	}
}
