import org.jibble.pircbot.PircBot;

public class tBot extends PircBot {
	/*
	Pircbot
	(c) Paul Mutton
	 */
	public tBot() throws Exception {
		this.setName("derfidschi");
		this.connect("irc.chat.twitch.tv", 6667, "oauth:wina1jfkwzqzoraqb0vqy7jes8m93l");
		this.sendRawLine("CAP REQ :twitch.tv/membership");
		
		this.setVerbose(false); //debug
	}
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		String[] messagePart = message.split(" ");
		
		if (Tools.availableCommands.contains(messagePart[0])) {
			sendMessage(channel, Tools.userCommand(sender, messagePart));
		} else if (Tools.availableAdminCommands.contains(messagePart[0])) {
			sendMessage(channel, Tools.adminCommands(sender, messagePart));
		}
	}
	public void onJoin (String channel, String sender, String login, String hostname) {
		Tools.listMod(sender, true);
	}
	public void onPart (String channel, String sender, String login, String hostname) {
		Tools.listMod(sender, false);
	}
}