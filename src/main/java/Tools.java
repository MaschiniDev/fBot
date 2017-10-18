import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Tools {
	private static HashMap<String, HashMap<String, Integer>> user = new HashMap<>();
	private static HashMap<String, HashMap<String, String>> commands = new HashMap<>();
	static ArrayList<String> availableChannelCommands = new ArrayList<>();
	static ArrayList<String> availableBotCommands = new ArrayList<>();
	private static List<String> live = new ArrayList<>();
	private static List<String> mods = new ArrayList<>();
	private static JSONObject json = new JSONObject();
	
	static void listModifier(String username, boolean add) {
		if (add && !live.contains(username)) {
			live.add(username);
		} else if (!add) {
			live.remove(live.indexOf(username));
		}
		
		if (!user.containsKey(username) && add) {
			HashMap<String, Integer> userData = new HashMap<>();
			userData.put("time", 0);
			userData.put("points", 0);
			
			user.put(username, userData);
		}
	}
	
	static String channelCommands(String sender, String[] part) {
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
	static String botCommands(String sender, String[] part) {
		String command = part[0].toLowerCase();
		String var1 = part[1].toLowerCase();
		
		String response = "No answer Found";
		
		if (mods.contains(sender)) {
			switch (command) {
				case "!help":
					response = availableBotCommands.toString().replace("[", "").replace("]", "");
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
							response = "All commands available for this Channel: " + availableChannelCommands;
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
		}
		
		switch (command) {
			case "!me":
				response = "Your Points: ";
		}
		return response;
	}
	
	static void initData(String channel) {
		try {
			json = Local.readJSON(channel);
			user = Local.getAllUser(channel);
			commands = Local.getCommands(channel);
			availableChannelCommands = Local.getAvailableCommands(channel);
			mods = Online.getMods();
			
			String[] aac = {"!help", "!list", "!add", "!remove", "!edit"};
			availableBotCommands =  new ArrayList<>(Arrays.asList(aac));
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
							String username = live.get(i);
							
							listModifier(username, true);
							HashMap<String, Integer> oneuser = user.get(username);
							oneuser.put("time", (Integer.parseInt(String.valueOf(oneuser.get("time"))) + 1));
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
							String username = live.get(i);
							
							listModifier(username, true);
							HashMap<String, Integer> oneuser = user.get(username);
							oneuser.put("points", (Integer.parseInt(String.valueOf(oneuser.get("points"))) + 1));
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

/*
    After you have read my code I recommend you to look for help,
    here are the numbers of Suicide-Prevention Hotlines:
        -> Germany: 0800 1110111
        -> USA:     1-800-273-8255
*/