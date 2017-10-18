import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static java.lang.Math.toIntExact;

public class Tools {
	private static HashMap<String, HashMap<String, Integer>> user = new HashMap<>();
	private static HashMap<String, HashMap<String, String>> commands = new HashMap<>();
	public static ArrayList<String> availableCommands = new ArrayList<>();
	public static ArrayList<String> availableAdminCommands = new ArrayList<>();
	private static List<String> live = new ArrayList<>();
	private static JSONObject json = new JSONObject();
	
	static void listMod(String username, boolean add) {
		if (add) {
			live.add(username);
		} else {
			live.remove(live.indexOf(username));
		}
		
		if (!user.containsKey(username) && add) {
			HashMap<String, Integer> userData = new HashMap<>();
			userData.put("time", 0);
			userData.put("points", 0);
			
			user.put(username, userData);
		}
	}
	
	static String userCommand(String sender, String[] part) {
		String command = part[0];
		HashMap<String, String> commandData = commands.get(command);
		
		String response = commandData.get("command");
		
		if (response.contains("{value}")) {
			Integer value = (Integer.parseInt(String.valueOf(commandData.get("value"))) + 1);
			
			commandData.put("value", value.toString());
			response = response.replace("{value}", value.toString());
		}
		response = response.replace("{sender}", sender);
		return response;
	}
	static String adminCommands(String sender, String[] part) {
		String command = part[0].toLowerCase();
		String var1 = part[1].toLowerCase();
		
		String response = "No answer Found";
		
		switch (command) {
			case "!help":
				response = availableAdminCommands.toString().replace("[", "").replace("]", "");
				break;
			case "!list":
				switch (var1) {
					case "live":
						response = "Right now are watching the Following user the Stream: " + live;
						break;
					case "all":
						response = "Every user ever registered on this Channel: " + user.keySet();
						break;
					case "commands":
						response = "All commands available for this Channel: " + availableCommands;
						break;
					default:
						response = "Your command is now Available!";
				}
				break;
			case "!add":
				if (!commands.containsKey(var1)) {
					String var2 = part[2].toLowerCase();
					String var3 = part[3].toLowerCase();
					if (!var1.contains("!"))
						var1.replace(var1, "!" + var1);
					
					
					HashMap<String, String> newCommand = new HashMap<>();
					newCommand.put("command", var2);
					newCommand.put("value", var3);
					
					commands.put(var1, newCommand);
					response = "Command " + var1 + " was created with the following Text: " + var2 + " and the Triggercount: " + var3;
				} else {
					response = "This command already exists!";
				}
				break;
			case "!remove":
				if (!var1.contains("!"))
					var1.replace(var1, "!" + var1);
				
				if (commands.containsKey(var1)) {
					commands.remove(var1);
					response = "Removed command: " + var1;
				} else {
					response = "The command you want to remove dont exist!";
				}
				break;
			case "!edit":
				int newValue = Integer.parseInt(part[2].toLowerCase());
				
				if (commands.containsKey(var1)) {
					HashMap<String, String> commandValue = commands.get(var1);
					String changedValue;
					
					if (var1.contains("+")) {
						changedValue = Integer.toString(Integer.parseInt(commandValue.get("value")) + newValue);
						commandValue.put("value", changedValue);
						response = "The Value for " + var1 + " was changed to: " + changedValue;
					}
					else if (var1.contains("-")) {
						changedValue = Integer.toString(Integer.parseInt(commandValue.get("value")) - newValue);
						commandValue.put("value", changedValue);
						response = "The Value for " + var1 + " was changed to: " + changedValue;
					} else {
						changedValue = Integer.toString(newValue);
						commandValue.put("value", Integer.toString(newValue));
						response = "The Value for " + var1 + " was changed to: " + changedValue;
					}
				}
				break;
		}
		return response;
	}
	
	static void initData(String channel) {
		try {
			json = Local.readJSON(channel);
			user = Local.getAllUser(channel);
			commands = Local.getCommands(channel);
			availableCommands = Local.getAvailableCommands(channel);
			
			String[] aac = {"!help", "!list", "!add", "!remove", "!edit"};
			availableAdminCommands =  new ArrayList<>(Arrays.asList(aac));
		} catch (Exception e) {
			e.printStackTrace();
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
							HashMap<String, Integer> user = Tools.user.get(live.get(i));
							user.put("time", user.get("time") + 1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		//timeClock.start();
		
		Thread pointClock = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						TimeUnit.MINUTES.sleep(5);
						
						for (int i = 0; i < live.size(); i++) {
							HashMap<String, Integer> user = Tools.user.get(live.get(i));
							user.put("points", user.get("points") + 1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		//pointClock.start();
	}
}
