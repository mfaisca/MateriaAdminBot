package com.materiabot;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.commands._Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Main {
	public static void main(String[] args) throws Exception {
		String privateToken = SQLAccess.getKeyValue(SQLAccess.BOT_TOKEN_KEY);
		if(privateToken == null) {
			System.out.println("Bot Token isn't inserted." + System.lineSeparator());
			return;
		}
        JDA client = new JDABuilder(privateToken).setAutoReconnect(true)
				.setStatus(OnlineStatus.ONLINE)
				.setActivity(Activity.playing("Opera Omnia")).addEventListeners(new _Listener()).build();
		client.awaitReady();
		_Listener.setClient(client);
		Constants.getClient();
		PluginManager.loadCommands();
		//PluginManager.loadUnits();
		System.out.println("Bot is ready!!");
	}
}