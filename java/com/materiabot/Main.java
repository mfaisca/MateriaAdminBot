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
//		System.out.println(_Library.GL.getUnit("Aranea").getEquipmentType().name());
//		System.out.println(_Library.GL.getUnit("Aranea").getAbility(Ability.Type.HP).get(0).getName());
//		System.out.println(_Library.GL.getUnit("Aranea").getAbility(Ability.Type.HP).get(0).getDescription());
		String privateToken = SQLAccess.getKeyValue(SQLAccess.BOT_TOKEN_KEY);
		if(privateToken == null) {
			System.out.println("Bot Token isn't inserted." + System.lineSeparator());
			return;
		} 
        JDA client = JDABuilder.createDefault(privateToken).setAutoReconnect(true)
				.setStatus(OnlineStatus.ONLINE).setMemberCachePolicy(MemberCachePolicy.NONE)
				.setActivity(Activity.playing("Opera Omnia")).build();
		PluginManager.loadCommands();
		//PluginManager.loadUnits();
		client.awaitReady();
		Constants.setClient(client);
		client.addEventListener(new _Listener());
		System.out.println("Bot is ready!!");
	}
}