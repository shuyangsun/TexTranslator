package utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
}
