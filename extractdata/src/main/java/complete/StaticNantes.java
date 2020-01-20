package complete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Process the Nantes JSON data
 * @return Nantes stations array
 * @param url
 */

public class StaticNantes {
	static String url = "https://api.jcdecaux.com/vls/v1/stations?contract=nantes&&apiKey=b5c059fa1b8e115f157e20cfa797e01b7650f0a7";
	static List<Station> stationsList = new ArrayList<Station>();

	public List<Station> processData() throws JSONException, IOException {
		JSONArray stations = readJsonFromUrl(url);
		processStations(stations);
		return stationsList;		
	}
	
	public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray stations = new JSONArray(jsonText);
			return stations;
		} finally {
			is.close();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	private static void processStations(JSONArray stations) {
		for (Object station : stations) {
			JSONObject stationJson = (JSONObject) station;
			Object ID =  stationJson.get("number");
			String name = (String) stationJson.get("name");
			JSONObject position = stationJson.getJSONObject("position");
			
			double lat = (Double) position.get("lat");
			double lon = (Double) position.get("lng");
			int capacity =  (Integer) stationJson.get("bike_stands");

			stationsList.add(new Station(ID.toString(), name, lat, lon, capacity));
		}
	}
}
