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
 * Process the Saint-Etienne JSON data
 * @return Saint-Etienne stations array
 * @param url
 */

public class StaticSaintEtienne {
	static String url = "https://saint-etienne-gbfs.klervi.net/gbfs/en/station_information.json";
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
			JSONObject json = new JSONObject(jsonText);
			JSONObject data = (JSONObject) json.get("data");
			JSONArray stations = (JSONArray) data.get("stations");
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
			String ID = (String) stationJson.get("station_id");
			String name = (String) stationJson.get("name");
			double lat = (Double) stationJson.get("lat");
			double lon = (Double) stationJson.get("lon");
			int capacity =  (Integer) stationJson.get("capacity");

			stationsList.add(new Station(ID, name, lat, lon, capacity));
		}
	}
}
