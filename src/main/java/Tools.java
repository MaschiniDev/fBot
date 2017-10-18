import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Tools {
	private static HashMap<String, HashMap<String, Integer>> viewer = new HashMap<>();
	private static HashMap<String, HashMap<String, String>> commands = new HashMap<>();
	public static ArrayList<String> availableCommands = new ArrayList<>();
	private static List<String> live = new ArrayList<>();
	
	static void listMod(String username, boolean add) {
		if (add) {
			live.add(username);
		} else {
			live.remove(live.indexOf(username));
		}
		
		if (!viewer.containsKey(username) && add) {
			HashMap<String, Integer> userData = new HashMap<>();
			userData.put("time", 0);
			userData.put("points", 0);
			
			viewer.put(username, userData);
		}
	}
	
	static void addAllLiveUser() {
		live.addAll(Online.getAllViewer());
		System.out.println(live);
	}
	static void startClocks() {
		Thread timeClock = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						TimeUnit.MINUTES.sleep(1);
						
						for (int i = 0; i < live.size(); i++) {
							HashMap<String, Integer> user = viewer.get(live.get(i));
							user.put("time", user.get("time") + 1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		timeClock.start();
		
		Thread pointClock = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						TimeUnit.MINUTES.sleep(5);
						
						for (int i = 0; i < live.size(); i++) {
							HashMap<String, Integer> user = viewer.get(live.get(i));
							user.put("points", user.get("points") + 1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		pointClock.start();
	}
}
