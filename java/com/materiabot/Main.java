package com.materiabot;
//import com.materiabot.GameElements.Ability;
//import com.materiabot.GameElements._Library;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.commands._Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Main {
	public static void main(String[] args) throws Exception {
		PluginManager.loadCommands(); //Load Plugins first so that the bot has all info when it launches
		PluginManager.loadUnits();

//		System.out.println(_Library.GL.getUnit("Aranea").getEquipmentType().name());
//		System.out.println(_Library.GL.getUnit("Aranea").getAbility(Ability.Type.HP).get(0).getName());
//		System.out.println(_Library.GL.getUnit("Aranea").getAbility(Ability.Type.HP).get(0).getDescription());
		
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
		System.out.println("Bot is ready!!");
	}
}