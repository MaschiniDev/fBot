import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Local {
	static JSONObject readJSON(String channel) throws Exception {
		String fileName = channel + ".json";
		
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				FileWriter fileWriter = new FileWriter(fileName);
				fileWriter.write("{\"commands\":[],\"users\":[]}");
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return (JSONObject) new JSONParser().parse(new FileReader(fileName));
	}
	
	static void writeJSON(String channel, JSONObject json) {
	
	}
	
	static HashMap<String, HashMap<String, Integer>> getAllUser (String channel) throws Exception {
		return (HashMap<String, HashMap<String,Integer>>) readJSON(channel).get("users");
	}
	static HashMap<String, HashMap<String, String>> getCommands (String channel) throws Exception {
		Tools.availableCommands = (ArrayList<String>) ((HashMap<String, HashMap<String,String>>) readJSON(channel).get("commands")).keySet();
		return (HashMap<String, HashMap<String,String>>) readJSON(channel).get("commands");
	}
}
