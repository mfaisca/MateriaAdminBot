package com.materiabot;
import com.materiabot.Utils.Constants;
import com.materiabot.commands._Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Main {	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		String privateToken = "Mzg3NTQ5NDkxNDAzODE2OTYx.XkCeLQ.Z3pGiYEFSVh7heA8jcKyzXnKt5w";
							//"NTMxNDE2ODc4MDExMzgzODA4.Xj_9Tg.eQv26k1-yqlyilMe3Rocmb3-Q0c";
							//ConfigsDB.getKeyValue(ConfigsDB.BOT_TOKEN_KEY);
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