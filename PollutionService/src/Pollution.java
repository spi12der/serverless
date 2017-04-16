import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Pollution {
	
	public static JSONObject callServiceURL(String urlString, String type)
	{
		JSONObject message=new JSONObject();
		try
		{
			//If the we are connect a PC in other network
			System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
		    System.setProperty("http.proxyPort", "8080");
		    System.setProperty("http.proxyPort", "8080");
		    System.setProperty("https.proxyHost", "proxy.iiit.ac.in");
		    System.setProperty("https.proxyPort", "8080");
		    System.setProperty("https.proxyPort", "8080");
			URL url = new URL(urlString);
			HttpURLConnection h = (HttpURLConnection)url.openConnection();
			h.setRequestMethod(type);
			h.setDoOutput(true);
			BufferedReader reader = new BufferedReader( new InputStreamReader(h.getInputStream() ) );
			String response = reader.readLine();
			if(response!=null)
			{
				JSONParser parser=new JSONParser();
				message=(JSONObject)parser.parse(response);
			}	
		}
		catch (Exception e) 
		{
			e.printStackTrace();
//			logMessage("ERROR", "Error in call any service URL =>"+e.getLocalizedMessage());
		}
		return message;
	}
	public static String getEmailURL(String message, String receiver, String subject){
		String url = "http://localhost:8114/Serverless/UserServlet?service_name=email&&type=send&&smtp_host=students.iiit.ac.in&&username=rohit.dayama@students.iiit.ac.in&&password=d671f8cf&&sender=rohit.dayama@students.iiit.ac.in";
		url += "&&receiver="+receiver;
		url += "&&subject=" + subject;
		url += "&&message=" + message;
		return url;
	}
	
	public static String getPollutionLevel(String cityname, int years_ago){
		return "Low";
		/*//JSONObject ll = callServiceURL("https://maps.googleapis.com/maps/api/geocode/json?address="+ cityname +"&key=AIzaSyD8ZJMvtdv4pWN0oD3gqnYgoyh86K8lH14");
		JSONObject ll = callServiceURL("https://maps.googleapis.com/maps/api/geocode/json?address=hyderabad&key=AIzaSyD8ZJMvtdv4pWN0oD3gqnYgoyh86K8lH14");
		return ll.toString();
		JSONObject location = (JSONObject)((JSONObject)ll.get("geometry")).get("location");
		JSONObject finalreply = callServiceURL("https://api.breezometer.com/baqi/?lat="+ location.get("lat")+"&lon="+ location.get("lng")+"&key=2725b65bf7bf42fd92e84899d4a101bf");
		
		return (String)finalreply.get("breezometer_description");*/
	}
	public static String getTopPollutedCity(int top){
		String[] cities = {"Delhi", "Chennai", "Hyderabad", "Banglore", "Kolkata", "Pune", "Jaipur", "Patna", "Indore", "Lucknow"};
		String res = "";
		for(int i=0; i<10 && i<top; i++){
			res += cities[i] + " ";
		}
		return res;
	}
	
	public static String getPollutionLevel(String cityname){
		return "Moderate";
	}
	
	public static String alertForCity(String cityname, int thres, String emailID){
		String message = cityname + " has reached the maximum threshold pollution level of " + String.valueOf(thres);
		String urlString = getEmailURL(message, emailID, "ALERT: Pollution");
		JSONObject response = callServiceURL(urlString, "POST");
		if(((String)response.get("status")).equals("1"))
			return String.valueOf(true);
		else
			return String.valueOf(false);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(getPollutionLevel("hyderabad", 10));
	}

}
