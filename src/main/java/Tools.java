import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tools {
	HashMap<String, HashMap<String, Integer>> viewer = new HashMap<String, HashMap<String, Integer>>();
	private static List<String> live = new ArrayList<String>();
	
	static void liveUser(String username, boolean add) {
		if (add) {
			live.add(username);
		} else {
			live.remove(live.indexOf(username));
		}
	}
	
	static void addAllLiveUser() {
		live.addAll(Online.getAllViewer());
		System.out.println(live);
	}
}
