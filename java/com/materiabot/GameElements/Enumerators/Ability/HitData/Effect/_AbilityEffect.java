package com.materiabot.GameElements.Enumerators.Ability.HitData.Effect;
import java.util.Arrays;
import java.util.List;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.HitData;
import com.materiabot.GameElements.Unit;

public class _AbilityEffect {
	public static enum TAG{
		PRE_EFFECT, POST_EFFECT, BRV, HP, FULLHP, SPLASH, REPEAT;
	}
	
	protected int id;
	protected String baseDescription;
	protected List<TAG> tags;
	
	public _AbilityEffect(int id, String desc, TAG... tags) {
		this.id = id;
		this.baseDescription = desc;
		this.tags = Arrays.asList(tags);
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	public String getDescription(HitData hd) {
		String description = getBaseDescription().replace("{t}", hd.getTarget().getDescription());
		for(int i = 0; i < 2; i++) {
			description = description.replace("{ail" + i + "}", hd.getAbility().getUnit().getSpecificAilment(hd.getArguments()[i]).getName().getBest());
			description = description.replace("{ab" + i + "}", hd.getAbility().getUnit().getSpecificAbility(hd.getArguments()[i]).getName().getBest());
			description = description.replace("{p" + i + "}", hd.getAbility().getUnit().getSpecificPassive(hd.getArguments()[i]).getName().getBest());
			description = description.replace("{" + i + "}", hd.getArguments()[i]+"");
		}
		while(description.contains("{pl")) { //{pl1;debuff;debuffs}  |||  buff{pl2;;s}
			String plurality = description.substring(description.indexOf("{pl"), description.indexOf("}", description.indexOf("{pl")) + 1);
			int idx = plurality.charAt(3) - '0';
			String ret = hd.getArguments()[idx] == 1 ? 
							plurality.substring(plurality.indexOf(";") + 1, plurality.lastIndexOf(";")) : 
							plurality.substring(plurality.lastIndexOf(";") + 1, plurality.indexOf("}"));
			description = description.replace(plurality, ret);
		}
		return description;
	}

	public final boolean isPreEffect() { return tags.contains(TAG.PRE_EFFECT); }
	public final boolean isPostEffect() { return tags.contains(TAG.POST_EFFECT); }
	public boolean isPreEffect(int evt) { return isPreEffect(); }
	public boolean isPostEffect(int evt) { return isPostEffect(); }
	public boolean isBRV() { return tags.contains(TAG.BRV); }
	public boolean isHP() { return tags.contains(TAG.HP); }
	public boolean allowRepeats() { return tags.contains(TAG.REPEAT); }
	public List<TAG> getTags() { return tags; }

	protected static Ability convertIdToAbility(Unit u, int id) {
		return u.getAbilities().get(id);
	}
}