


	import java.io.BufferedReader;
	import java.io.InputStreamReader;
	import java.net.HttpURLConnection;
	import java.net.URL;

	import org.json.simple.JSONObject;
	import org.json.simple.parser.JSONParser;

	public class Climate {
		public static JSONObject callServiceURL(String urlString)
		{
			JSONObject message=new JSONObject();
			try
			{
				//If the we are connect a PC in other network
				System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
			    System.setProperty("http.proxyPort", "8080");
			    System.setProperty("http.proxyPort", "8080");
				URL url = new URL(urlString);
				HttpURLConnection h = (HttpURLConnection)url.openConnection();
				h.setRequestMethod("GET");
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
//				logMessage("ERROR", "Error in call any service URL =>"+e.getLocalizedMessage());
			}
			return message;
		}
		public static String getWeather(boolean inCel){
			String url = "http://api.openweathermap.org/data/2.5/weather?q=London%2Cuk&APPID=278f6597e7402cdb8f11c5cfd8acc4c9";
			JSONObject message = (JSONObject)callServiceURL(url).get("main");
			double res = (double)message.get("temp");
			res = res - 273.15;
			if (!inCel){
				res = res*1.8 + 32;
			}
			return String.valueOf(res);
		}
		public static String getWeather(){
			String url = "http://samples.openweathermap.org/data/2.5/weather?q=Hyderabad,india&appid=278f6597e7402cdb8f11c5cfd8acc4c9";
			JSONObject message = (JSONObject)callServiceURL(url).get("main");
			double res = (double)message.get("temp");
			return String.valueOf(res);
		}
		public static String getHumidity(){
			String url = "http://samples.openweathermap.org/data/2.5/weather?q=Hyderabad,india&appid=278f6597e7402cdb8f11c5cfd8acc4c9";
			JSONObject message = (JSONObject)callServiceURL(url).get("main");
			long res = (long)message.get("humidity");
			return String.valueOf(res);
		}
		public static String getWindSpeed(){
			String url = "http://samples.openweathermap.org/data/2.5/weather?q=Hyderabad,india&appid=278f6597e7402cdb8f11c5cfd8acc4c9";
			JSONObject message = (JSONObject)callServiceURL(url).get("wind");
			double res = (double)message.get("speed");
			return String.valueOf(res);
		}
		public static void main(String[] args) {
			//String url = "http://samples.openweathermap.org/data/2.5/weather?q=Hyderabad,india&appid=278f6597e7402cdb8f11c5cfd8acc4c9";
			
			/*System.out.println(getWeather(false));
			System.out.println(getHumidity());
			System.out.println(getWeather());
			System.out.println(getWindSpeed());*/
			// TODO Auto-generated method stub

		}

	}
