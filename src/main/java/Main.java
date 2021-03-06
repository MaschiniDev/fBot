import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	private static String channel = null;
	
	public static void main(String[] args) throws Exception {
		Bot tBot = new Bot();
		
		BufferedReader consoleLine = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Channel?");
		channel = consoleLine.readLine();
		Online.getViewerInfos(channel);
		
		Tools.addAllLiveUser();
		Tools.initData(channel);
		
		if (channel != null) {
			tBot.joinChannel("#" + channel);
		} else {
			System.exit(404);
		}
		
		System.out.println(Online.getMods());
		System.out.println(Online.getViewers());
		
		//LAST THING
		Tools.startClocks();
		
		while (true) {
			if (consoleLine.readLine().equalsIgnoreCase("save")) {
				Tools.saveData();
			}
		}
	}
	
	static String getChannel() {
		return channel;
	}
}

/*
    After you have read my code I recommend you to look for help,
    here are the numbers of Suicide-Prevention Hotlines:
        -> Germany: 0800 1110111
        -> USA:     1-800-273-8255
*/