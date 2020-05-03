package com.materiabot;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.commands._Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
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
		_Library.GL.getUnit("Cloud").getEquipmentType();
		System.out.println("Bot is ready!!");
		int i = 0;
		for(Message m : client.getTextChannelById(504366585163546654L).getHistory().getRetrievedHistory()) {
			if(i++ == 20) break;
			System.out.println(m.getContentDisplay());
		}
		//TODO Test everything!!!
		//TODO Make Skill command
		//TODO Check missing commands (pull at least?)
		//TODO Ignore parsing numbers on skills for now
		//TODO Make nicknames and unit overrides, come up with a quick way to just have nicknames/weapons/colors until Rem finishes
	}
}