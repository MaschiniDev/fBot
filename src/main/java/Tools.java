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
	private static JSONObject localData = new JSONObject();
	
	static void listModifier(String username, boolean add) {
		if (add && !live.contains(username)) {
			live.add(username);
		} else if (!add) {
			live.remove(live.indexOf(username));
		}
		try {
			if (user != null) {
				if (!user.containsKey(username) && !add) {
					HashMap<String, Integer> userData = new HashMap<>();
					userData.put("time", 0);
					userData.put("points", 0);
					
					user.put(username, userData);
					System.out.println(user);
				}
			} else {
				System.out.println("user = " + user);
			}
			
		} catch (NullPointerException npe) {
			npe.printStackTrace();
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
							response = "Default";
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
				response = user.get(sender).toString();
		}
		return response;
	}
	
	static void initData(String channel) {
		try {
			localData = Local.readJSON(channel);
			user = getAllUser();
			commands = getCommands();
			availableChannelCommands = getAvailableCommands();
			mods = Online.getMods();
			
			String[] aac = {"!help", "!list", "!add", "!remove", "!edit", "!me"};
			availableBotCommands =  new ArrayList<>(Arrays.asList(aac));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static HashMap<String, HashMap<String, Integer>> getAllUser () throws Exception {
		return (HashMap<String, HashMap<String, Integer>>) localData.get("user");
	}
	static HashMap<String, HashMap<String, String>> getCommands () throws Exception {
		return (HashMap<String, HashMap<String, String>>) localData.get("commands");
	}
	static ArrayList<String> getAvailableCommands() throws Exception {
		return new ArrayList<>(((HashMap<String, HashMap<String,String>>) localData.get("commands")).keySet());
	}
		
		
		
		static void addAllLiveUser() {
		live.addAll(Online.getAllViewer());
		System.out.println(live);
	}
	
	static void saveData() {
		Local.writeJSON(Main.getChannel(), commands, user);
		System.out.println("saved");
	}
	
	//TODO FIX THE NULLPOINTER!
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
		
		Thread autoSave = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						TimeUnit.MINUTES.sleep(10);
						Local.writeJSON(Main.getChannel(), commands, user);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
		
		/*
		Start Threads/Clocks/Timer
		 */
		timeClock.start();
		pointClock.start();
		autoSave.start();
		
	}
}

/*
    After you have read my code I recommend you to look for help,
    here are the numbers of Suicide-Prevention Hotlines:
        -> Germany: 0800 1110111
        -> USA:     1-800-273-8255
*/