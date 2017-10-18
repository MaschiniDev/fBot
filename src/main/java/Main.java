import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	
	public static void main(String[] args) throws Exception {
		tBot bot = new tBot();
		
		BufferedReader consoleLine = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Channel?");
		String channel = consoleLine.readLine();
		Online.getViewerInfos(channel);
		
		Tools.addAllLiveUser();
		Tools.initData(channel);
		
		if (channel != null) {
			bot.joinChannel("#" + channel);
		} else {
			System.exit(404);
		}
		
		System.out.println(Online.getMods());
		System.out.println(Online.getViewers());
		
		//LAST THING
		Tools.startClocks();
	}
}

/*
    After you have read my code I recommend you to look for help,
    here are the numbers of Suicide-Prevention Hotlines:
        -> Germany: 0800 1110111
        -> USA:     1-800-273-8255
*/