package com.materiabot.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Effect._AbilityEffect;
import com.materiabot.GameElements.Enumerators.Ability.MiscConditionLabel._MiscConditionLabel;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect._AuraEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Required._AuraRequired;
import com.materiabot.GameElements.Enumerators.Ailment.Effect._AilmentEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Required._AilmentRequired;
import com.materiabot.GameElements.Enumerators.Passive.Effect._PassiveEffect;
import com.materiabot.GameElements.Enumerators.Passive.Required._PassiveRequired;
import com.materiabot.commands._BaseCommand;
import net.dv8tion.jda.api.JDA;

public class Constants {
	private static JDA client;
	public static final HashMap<Long, String> PREFIX = new HashMap<>();
	public static final long QUETZ_ID = 141599746987917312L;
	public static final long INK_ID = 290867157435416577L;
	public static final long DREAMY_ID = 194476008395505664L;
	public static final Long MATERIABOT_SERVER_ID = 544340710862618624L;
	public static final Long MATERIABOT_ADMIN_SERVER_ID = 894309469670998026L;
	public static final List<Unit> UNITS = new LinkedList<>();
	public static final List<_BaseCommand> COMMANDS = new ArrayList<>();
	public static final HashMap<Integer, _AbilityEffect> ABILITY_EFFECT = new HashMap<>();
	public static final HashMap<Integer, _PassiveEffect> PASSIVE_EFFECT = new HashMap<>();
	public static final HashMap<Integer, _PassiveRequired> PASSIVE_REQUIRED = new HashMap<>();
	public static final HashMap<Integer, _AilmentEffect> AILMENT_EFFECT = new HashMap<>();
	public static final HashMap<Integer, _AilmentRequired> AILMENT_REQUIRED = new HashMap<>();
	public static final HashMap<Integer, _AuraEffect> AURA_EFFECT = new HashMap<>();
	public static final HashMap<Integer, _AuraRequired> AURA_REQUIRED = new HashMap<>();
	public static final HashMap<Integer, _MiscConditionLabel> LABELS = new HashMap<>();
	public static final List<Integer> EFFECTS_THAT_CAN_REFUND = new LinkedList<>();
	public static final boolean DEBUG;

	public static final class DupeMerger {
		public String text;
		public int count = 1;
		public DupeMerger real = this;
		
		public DupeMerger(String t) { text = t; }
		
		public void merge(DupeMerger other) {
			if(this.real.text.equals(other.text)) {
				this.real.count++; other.count--;
				other.real = this.real;
			}
		}
		@Override
		public String toString() {
			if(count < 1) return "";
			if(count > 1) return text + " **__" + count + " times__**";
			return text;
		}
	}
	
	static {
		DEBUG = true;
	}
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