package complete;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.json.JSONException;

/**
 * This is the first file to be run
 * Extract the static data and save it in Fueski. For now, consider 4 cities,
 * add more in future. Run this file to save static data to DB.
 */

public class CreateModel {
	private static Model model;
	private static final String FUESKI_LOCAL_ENDPOINT = "http://localhost:3030/bicycle_stations";

	static String bStationURIPrefix;
	static String cityURIPrefix;
	static String publicBicycleStationURIPrefix;
	static String[] cities = { "SAINT-ETIENNE", "LYON", "TOULOUSE", "NANTES" };

	public static void main(String args[]) throws JSONException, IOException {
		sslResolve();
		initializeModel();
		for (String city : cities) {
			List<Station> stations = null;

			if (city == "SAINT-ETIENNE") {
				StaticSaintEtienne se = new StaticSaintEtienne();
				stations = se.processData();
				addCityToModel(stations, city);
			} else if (city == "LYON") {
				StaticLyonKey ly = new StaticLyonKey();
				stations = ly.processData();
				addCityToModel(stations, city);
			}
//			else if (city == "PARIS") {
//				StaticParis pa = new StaticParis();
//				stations = pa.processData();
//				addCityToModel(stations, city);
//			}
			else if (city == "TOULOUSE") {
				StaticToulouse pa = new StaticToulouse();
				stations = pa.processData();
				addCityToModel(stations, city);
			}
			else if (city == "NANTES") {
				StaticToulouse pa = new StaticToulouse();
				stations = pa.processData();
				addCityToModel(stations, city);
			}
			saveToFueski();
		}
		System.out.println("DONE: Extracted all the data and save to DB successfully..");
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

	public static void initializeModel() {
		model = ModelFactory.createDefaultModel();
		model.setNsPrefix("ex", NsPrefix.getExNS());
		model.setNsPrefix("geo", NsPrefix.getGeoNS());
		model.setNsPrefix("onto", NsPrefix.getOntoNS());
	}

	public static void addCityToModel(List<Station> stations, String city) {
		// word Station is used for BicycleStations

		bStationURIPrefix = NsPrefix.getOntoNS() + "Station";
		cityURIPrefix = NsPrefix.getOntoNS() + "City";
		publicBicycleStationURIPrefix = NsPrefix.getOntoNS() + "PublicBicycleStation";

		for (Station station : stations) {
			Resource StationRs = model.createResource(bStationURIPrefix + ":" + city + ":" + station.getID());
			if (station.getName() != "") {
				Property pstationName = model.createProperty(NsPrefix.getOntoNS() + "stationName");
				StationRs.addProperty(pstationName, station.getName(), "En");
			}

			if (station.getCapacity() > 0) {
			Property pcapacity = model.createProperty(NsPrefix.getOntoNS() + "capacity");
			Statement statement_pcapacity = model.createLiteralStatement(StationRs, pcapacity, station.getCapacity());
			model.add(statement_pcapacity);
			}

			Property phasStation = model.createProperty(NsPrefix.getOntoNS() + "hasStation");
			Resource CityRs = model.createResource(cityURIPrefix + "/" + city);
			CityRs.addProperty(phasStation, StationRs);

			Property pcityName = model.createProperty(NsPrefix.getOntoNS() + "cityName");
			CityRs.addProperty(RDF.type, NsPrefix.getOntoNS() + "PublicBicycleStation");
			CityRs.addProperty(pcityName, city); // When add Language tag, select query gives an error, Fix this with language tag

			// xsd datetype
			if (station.getLat() > 0) {
			StationRs.addLiteral(model.createProperty(NsPrefix.getGeoNS() + "lat"), station.getLat());
			}

			if (station.getLon() > 0) {
			StationRs.addLiteral(model.createProperty(NsPrefix.getGeoNS() + "long"), station.getLon());
			}
		}
	}


	// Save the model in Fueski DB
	public static void saveToFueski() {
		try {
			DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(FUESKI_LOCAL_ENDPOINT);
			accessor.putModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		String url = "E:\\CPS2\\Year_2\\Semantic_Web\\Jena\\extractdata\\src\\main\\java\\static_01_19.ttl";
//
//		try {
//			model.write(new FileOutputStream(new File(url)), "TURTLE");
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
	}

}
