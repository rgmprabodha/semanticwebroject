
package complete;

import java.util.Map;

public class New {

	static String address = "Emile Littre, Saint Etienne";

	public static void main(String[] args) {
		Map<String, Double> coords;
		coords = OpenStreetMapUtils.getInstance().getCoordinates(address);
		System.out.println(coords);
		System.out.println("latitude :" + coords.get("lat"));
		System.out.println("longitude:" + coords.get("lon"));
	}
}