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
		
		if (channel != null) {
			bot.joinChannel("#" + channel);
		} else {
			System.exit(404);
		}
	}
}
