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
		PluginManager.loadCommands();
		String privateToken = SQLAccess.getKeyValue(SQLAccess.BOT_TOKEN_KEY);
		if(privateToken == null) {
			System.out.println("Bot Token isn't inserted." + System.lineSeparator());
			return;
		}
        JDA client = new JDABuilder(privateToken).setAutoReconnect(true)
				.setStatus(OnlineStatus.ONLINE)
				.setActivity(Activity.playing("Opera Omnia")).build();
		client.awaitReady();
		client.addEventListener(new _Listener());
		Constants.setClient(client);
		//PluginManager.loadUnits();
		System.out.println("Bot is ready!!");
	}
}