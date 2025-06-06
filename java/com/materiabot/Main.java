package com.materiabot;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.commands._Listener;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {	
	public static void main(String[] args) throws Exception {
		String privateToken = SQLAccess.getKeyValue(SQLAccess.BOT_TOKEN_KEY);
		if(privateToken == null) {
			System.out.println("Bot Token isn't inserted." + System.lineSeparator());
			return;
		}
		System.out.println("Connected to DB");
		final List<GatewayIntent> gateways = Arrays.asList(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, 
															GatewayIntent.GUILD_EMOJIS);
		JDA client = JDABuilder.createDefault(privateToken).setAutoReconnect(true)
				.setEnabledIntents(gateways).setMemberCachePolicy(MemberCachePolicy.NONE).disableCache(CacheFlag.VOICE_STATE)
				.setStatus(OnlineStatus.ONLINE).setActivity(Activity.playing("Opera Omnia")).build();
		Constants.setClient(client);
		client.awaitReady();
		PluginManager.reset(true);
		client.addEventListener(new _Listener());
		System.out.println("Bot is ready!!");
	}
}