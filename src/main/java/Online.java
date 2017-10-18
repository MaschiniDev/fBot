import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Online {
	
	String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	String time = new SimpleDateFormat("[HH:mm] ").format(new Date());
	BufferedWriter log = new BufferedWriter(fileWriter());
	
	FileWriter fileWriter() {
		FileWriter fw = null;
		try {
			fw = new FileWriter("log/" + date + ".txt");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return fw;
	}
	
	
	private static JSONObject viewerInfos = new JSONObject();
	private static JSONObject chatters = new JSONObject();
	
	
	static void getViewerInfos(String channel) {
		try {
			JSONParser parser = new JSONParser();
			viewerInfos = (JSONObject) parser.parse(readUrl("http://tmi.twitch.tv/group/user/" + channel + "/chatters"));
			chatters = (JSONObject) viewerInfos.get("chatters");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void write(String text) {
		if (true)
			System.out.println(time + text);
		try {
			log.newLine();
			log.write(time + text);
			log.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			try {
				URL url = new URL(urlString);
				reader = new BufferedReader(new InputStreamReader(url.openStream()));
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);
			
			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
	
	
	static ArrayList getViewers() {
		return (JSONArray) chatters.get("viewers");
	}
	static ArrayList getGlobalMods() {
		return (JSONArray) chatters.get("global_mods");
	}
	static ArrayList getStaff() {
		return (JSONArray) chatters.get("staff");
	}
	static ArrayList getAdmins() {
		return (JSONArray) chatters.get("admons");
	}
	static ArrayList getMods() {
		return (JSONArray) chatters.get("moderators");
	}
	static ArrayList getAllViewer() {
		ArrayList allViewer = new ArrayList();
		if (getMods() != null) {allViewer.addAll(getMods());}
		if (getAdmins() != null) {allViewer.addAll(getAdmins());}
		if (getGlobalMods() != null) {allViewer.addAll(getGlobalMods());}
		if (getStaff() != null) {allViewer.addAll(getStaff());}
		if (getViewers() != null) {allViewer.addAll(getViewers());}
		return allViewer;
	}
}

/*
    After you have read my code I recommend you to look for help,
    here are the numbers of Suicide-Prevention Hotlines:
        -> Germany: 0800 1110111
        -> USA:     1-800-273-8255
*/