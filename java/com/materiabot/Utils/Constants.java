package com.materiabot.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Unit;
import com.materiabot.commands._BaseCommand;
import net.dv8tion.jda.api.JDA;

public class Constants {
	private static JDA client;
	public static final HashMap<Long, String> PREFIX = new HashMap<>();
	public static final long QUETZ_ID = 141599746987917312L;
	public static final long QUETZ2_ID = 966478575014400120L;
	public static final long CEL_ID = 199361811965804544L;
	public static final Long MATERIABOT_SERVER_ID = 544340710862618624L;
	public static final Long MATERIABOT_ADMIN_SERVER_ID = 894309469670998026L;
	public static final List<Unit> UNITS = new LinkedList<>();
	public static final List<_BaseCommand> COMMANDS = new ArrayList<>();
	
	private Constants() {}
	
	public static JDA getClient() { return client; }
	public static void setClient(JDA c) { client = c; }
	
	public static final void sleep(int sleep){
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {;}
	}
	public static boolean userHasMateriaRole(long userId, long roleId) {
		try {
			return client.getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID)
						.retrieveMemberById(userId).complete()
						.getRoles().stream().anyMatch(r -> r.getIdLong() == roleId);
		} catch(Exception e) {
			return false;
		}
	}
}