import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

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
}
