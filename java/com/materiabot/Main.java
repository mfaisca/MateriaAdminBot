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
		
		/*
		 * Weird Barret Avalanche Buff
		 * Steiner BRV+ should be 4 hits
		 * Agrias is +10/20/30/40/60% instead of 20/30/40/50/70
		 * Aphmau Autonomize BRV Regen should be party-wide
		 * Yuri ATK Aura is 20%
		 * Fang BRV+ Saboteur has High Turn Rate
		 * Fang HP+Commando has bonus dmg vs ST
		 * Celes Runic buff should be 40% instead of 60% ??
		 * Locke EX Buff(speed) is 10% only
		 */
		
		
		String privateToken = SQLAccess.getKeyValue(SQLAccess.BOT_TOKEN_KEY);
		if(privateToken == null) {
			System.out.println("Bot Token isn't inserted." + System.lineSeparator());
			return;
		} 
		System.out.println("Connected to DB");
		final List<GatewayIntent> gateways = Arrays.asList(	GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS, 
															GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES, 
															GatewayIntent.GUILD_EMOJIS);
        JDA client = JDABuilder.createDefault(privateToken).setAutoReconnect(true)
				.setEnabledIntents(gateways).setMemberCachePolicy(MemberCachePolicy.NONE).disableCache(CacheFlag.VOICE_STATE)
				.setStatus(OnlineStatus.ONLINE).setActivity(Activity.playing("Opera Omnia")).build();
		Constants.setClient(client);
		PluginManager.loadCommands();
		PluginManager.loadUnits();
		client.awaitReady();
		client.addEventListener(new _Listener());
		System.out.println("Bot is ready!!");
		
//		com.materiabot.IO.JSON.UnitParser.UNITS.stream().forEach(c -> System.out.println(c.getName() + " - " + String.join(" / ", c.getNicknames())));
		
//		System.out.println(client.getGuildById(423915309523664898l).getTimeCreated().toString());
//		client.getGuildById(423915309523664898l).retrieveMembers().get();
//		client.getGuildById(423915309523664898l).getMembers().stream().sorted((m1, m2) -> m1.getTimeJoined().compareTo(m2.getTimeJoined())).limit(10).forEach(m -> System.out.println(m.getEffectiveName() + " - " + m.getTimeJoined()));
		
//		client.getTextChannelById(586848659447218198L).getHistoryAround(737364251056013373L, 100).complete().getRetrievedHistory().forEach(m -> {
//			System.out.println(m.getId() + " - " + m.getTimeCreated().toString() + " " + m.getAuthor().getName() + ": " + m.getContentDisplay());
//		});
		
//		System.out.println("---");
//		client.getTextChannelById(504366585163546654l).getHistoryBefore(711738862346698803L, 100).complete().getRetrievedHistory().forEach(m -> {
//			System.out.println(m.getId() + " - " + m.getTimeCreated().toString() + " " + m.getAuthor().getName() + ": " + m.getContentDisplay());
//		});
	}
}