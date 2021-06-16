package com.materiabot.Utils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Effect._AbilityEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect._AuraEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Required._AuraRequired;
import com.materiabot.GameElements.Enumerators.Ailment.Condition._Condition;
import com.materiabot.GameElements.Enumerators.Ailment.Effect._AilmentEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Required._AilmentRequired;
import com.materiabot.GameElements.Enumerators.Passive.Effect._PassiveEffect;
import com.materiabot.GameElements.Enumerators.Passive.Required._PassiveRequired;
//import java.util.concurrent.CopyOnWriteArrayList;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.commands._BaseCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;

public class Constants {
	private static JDA client;
	public static final String DEFAULT_PREFIX;
	public static final HashMap<Long, String> PREFIX = new HashMap<Long, String>();
	public static final long QUETZ_ID = 141599746987917312L;
	public static final long INK_ID = 290867157435416577L;
	public static final long DREAMY_ID = 194476008395505664L;
	public static final long JDOTA_ID = 521836678130827316L; //To create the votes
	public static final Long MATERIABOT_SERVER_ID = 544340710862618624L;
	public static final List<_BaseCommand> COMMANDS = new ArrayList<_BaseCommand>(); //CopyOnWriteArrayList
	public static final HashMap<Integer, _AbilityEffect> ABILITY_EFFECT = new HashMap<Integer, _AbilityEffect>();
	public static final HashMap<Integer, _PassiveEffect> PASSIVE_EFFECT = new HashMap<Integer, _PassiveEffect>();
	public static final HashMap<Integer, _PassiveRequired> PASSIVE_REQUIRED = new HashMap<Integer, _PassiveRequired>();
	public static final HashMap<Integer, _AilmentEffect> AILMENT_EFFECT = new HashMap<Integer, _AilmentEffect>();
	public static final HashMap<Integer, _AilmentRequired> AILMENT_REQUIRED = new HashMap<Integer, _AilmentRequired>();
	public static final HashMap<Integer, _Condition> AILMENT_CONDITION = new HashMap<Integer, _Condition>();
	public static final HashMap<Integer, _AuraEffect> AURA_EFFECT = new HashMap<Integer, _AuraEffect>();
	public static final HashMap<Integer, _AuraRequired> AURA_REQUIRED = new HashMap<Integer, _AuraRequired>();
	public static final boolean DEBUG;

	static {
		
		DEBUG = isHeaven();
		String p = DEBUG ? "%" : "$";
		DEFAULT_PREFIX = p;
	}
	
	public static final boolean isHeaven() {
		try {
			return InetAddress.getLocalHost().getHostName().equalsIgnoreCase("REACTOR");
		} catch (UnknownHostException e) {}
		return false;
	}
	
	public static JDA getClient() { return client; }
	public static boolean canDeleteMessages(Guild g) {
		return g.getMember(client.getSelfUser()).hasPermission(Permission.MESSAGE_MANAGE);
	}
	public static void setClient(JDA c) { client = c; }
	
	public static final void sleep(int sleep){
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {}
	}
	public static String getGuildPrefix(Guild g) {
		if(g == null || client.isUnavailable(g.getIdLong())) 
			return Constants.DEFAULT_PREFIX;
		if(!PREFIX.containsKey(g.getIdLong())) {
			String p = SQLAccess.getGuildPrefix(g);
			PREFIX.put(g.getIdLong(), p);
		}
		return PREFIX.get(g.getIdLong());
	}
}