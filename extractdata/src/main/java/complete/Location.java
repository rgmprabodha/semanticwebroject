package complete;

public class Location {

	private String ID;
	private String name;
	private double lat;
	private double lon;
	
	public Location(String iD, String name, double lat, double lon) {
		super();
		ID = iD;
		this.name = name;
		this.lat = lat;
		this.lon = lon;
	}

	public String getID() {
		return ID;
	}
	public double getLat() {
		return lat;
	}
	public String getName() {
		return name;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public void setName(String name) {
		this.name = name;
	}
}
