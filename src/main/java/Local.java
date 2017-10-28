import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.HashMap;

public class Local {
	static JSONObject readJSON(String jsonname) throws Exception {
		String fileName = null;
		if (!jsonname.contains(".json"))
			fileName = jsonname + ".json";
		
		File file = new File(fileName);
		if (!file.exists() || new JSONParser().parse(new FileReader(fileName)) == null) {
			try {
				FileWriter fileWriter = new FileWriter(fileName);
				fileWriter.write("{\"commands\":{},\"users\":{}}");
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return (JSONObject) new JSONParser().parse(new FileReader(fileName));
	}
	
	static void writeJSON(String filename, HashMap<String, HashMap<String, String>> commands, HashMap<String, HashMap<String, Integer>> user) {
		JSONObject json = new JSONObject();
		json.put("commands", commands);
		json.put("user", user);
		
		if (!filename.contains(".json"))
			filename = filename + ".json";
		
		try {
			FileWriter fileWriter = new FileWriter(filename);
			fileWriter.write(json.toJSONString());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
}

/*
    After you have read my code I recommend you to look for help,
    here are the numbers of Suicide-Prevention Hotlines:
        -> Germany: 0800 1110111
        -> USA:     1-800-273-8255
*/