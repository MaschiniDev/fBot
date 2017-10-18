import org.jibble.pircbot.PircBot;

public class Bot extends PircBot {
	/*
	Pircbot
	(c) Paul Mutton
	 */
	public Bot() throws Exception {
		this.setName("derfidschi");
		this.connect("irc.chat.twitch.tv", 6667, "oauth:wina1jfkwzqzoraqb0vqy7jes8m93l");
		this.sendRawLine("CAP REQ :twitch.tv/membership");
		
		this.setVerbose(false); //debug
	}
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		String[] messagePart = message.split(" ");
		
		if (Tools.availableChannelCommands.contains(messagePart[0])) {
			sendMessage(channel, Tools.channelCommands(sender, messagePart));
		} else if (Tools.availableBotCommands.contains(messagePart[0])) {
			sendMessage(channel, Tools.botCommands(sender, messagePart));
		}
	}
	public void onJoin (String channel, String sender, String login, String hostname) {
		Tools.listModifier(sender, true);
	}
	public void onPart (String channel, String sender, String login, String hostname) {
		Tools.listModifier(sender, false);
	}
}

/*
    After you have read my code I recommend you to look for help,
    here are the numbers of Suicide-Prevention Hotlines:
        -> Germany: 0800 1110111
        -> USA:     1-800-273-8255
*/