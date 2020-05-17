package com.materiabot;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.commands._Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Main {
	public static void main(String[] args) throws Exception {
		String privateToken = SQLAccess.getKeyValue(SQLAccess.BOT_TOKEN_KEY);
		if(privateToken == null) {
			System.out.println("Bot Token isn't inserted." + System.lineSeparator());
			return;
		} 
        JDA client = JDABuilder.createDefault(privateToken).setAutoReconnect(true)
				.setStatus(OnlineStatus.ONLINE).setMemberCachePolicy(MemberCachePolicy.NONE)
				.setActivity(Activity.playing("Opera Omnia")).build();
		Constants.setClient(client);
		PluginManager.loadCommands();
		PluginManager.loadUnits();
		client.awaitReady();
		client.addEventListener(new _Listener());
		System.out.println("Bot is ready!!");
		//TODO Make Skill command
		//TODO Check missing commands (pull at least?)
		//TODO Ignore parsing numbers on skills for now
		
//		client.getTextChannelById(671911192385552394l).getHistory().retrievePast(100).complete().forEach(m -> {
//			System.out.println(m.getTimeCreated().toString() + " " + m.getAuthor().getName() + ": " + m.getContentDisplay());
//		});
	}
}