package complete;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Extract the dynamic data and save it in Fueski. 
 * code to be re-factor
 */


public class DynamicSaintEtienne {
	private static final String FUESKI_LOCAL_ENDPOINT = "http://localhost:3030/bicycle_stations/update";

	public static void main(String args[]) throws IOException, JSONException {
		sslResolve();
		String url = "https://saint-etienne-gbfs.klervi.net/gbfs/en/station_status.json";
		JSONObject json = readJsonFromUrl(url);
		JSONObject data = (JSONObject) json.get("data");
		JSONArray stations = (JSONArray) data.get("stations");
		processStationDyna(stations);
	}

	public static void sslResolve() {

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (GeneralSecurityException e) {
		}

	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
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

	private static void processStationDyna(JSONArray stations) {
		updateModel(stations);
	}

	public static void updateModel(JSONArray stations) {
		String stationURIPrefix = NsPrefix.getOntoNS() + "Station";
		String city = "SAINT-ETIENNE";
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date today = new Date();
		String todayDate = formatter.format(today);
		
		for (Object station : stations) {

			JSONObject stationJson = (JSONObject) station;
			String ID = (String) stationJson.get("station_id");
			int nava = (Integer) stationJson.get("num_bikes_available");
//			int ndisa = (Integer) stationJson.get("num_bikes_disabled");
//			int ndocava = (Integer) stationJson.get("num_docks_available");
//			int ninstall = (Integer) stationJson.get("is_installed");
//			int nrenting = (Integer) stationJson.get("is_renting");
//			int nreturn = (Integer) stationJson.get("is_returning");
//			String nupdatetime = String.valueOf((Integer) stationJson.get("last_reported"));

			String iri = stationURIPrefix + ":" + city +":" + ID;
			
			String query = "PREFIX onto: <http://www.semanticweb.org/emse/ontologies/2019/11/bicycle_stations.owl#>\r\n"
					+ "PREFIX geo: <https://www.w3.org/2003/01/geo/wgs84_pos#>\r\n"
					+ "PREFIX schema: <http://schema.org/>\r\n" 
					+ "prefix xsd: <http://www.w3.org/2001/XMLSchema#> \r\n"
					+ "INSERT DATA { <" +  iri + "> onto:hasAvailability [\r\n" 
					+ "                                            a           onto:Availability; \r\n"
					+ "					                           onto:updatedDatetime \"" + todayDate + "\"^^xsd:dateTime;\r\n"
					+ "					                           onto:availableBikes \""  +nava + "\";\r\n" 
					+ "                                            ] .\r\n" 
					+ "		}";
			
			
			UpdateRequest update  = UpdateFactory.create(query);
	        UpdateProcessor qexec = UpdateExecutionFactory.createRemote(update, FUESKI_LOCAL_ENDPOINT);
	        qexec.execute();
		}
		System.out.println("DONE: Added Saint Etienne dynamic data");
	}
}
