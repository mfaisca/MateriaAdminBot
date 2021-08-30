package com.materiabot.GameElements.Enumerators.Ability.HitData.Effect;
import java.util.Arrays;
import java.util.List;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.HitData;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators._Plugin;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;

public class _AbilityEffect implements _Plugin {
	public enum TAG{
		PRE_EFFECT, POST_EFFECT, BRV, HP, FULLHP, SPLASH, NOREPEAT, MERGE;
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
		return applyReplaces(hd, getBaseDescription());
	}
	
	protected final String applyReplaces(HitData hd, String description) {
		if(hd.getTarget() != null) {
			description = description.replace("{t2}", hd.getTarget().getDescription2());
			description = description.replace("{t}", hd.getTarget().getDescription());
		}else {
			description = description.replace("{t2}", "Unknown Target: " + hd.getTargetId());
			description = description.replace("{t}", "Unknown Target: " + hd.getTargetId());
		}
		if(hd.getMaxBrvOverflow() > 100)
			description = description.replace("{of}", " (" + hd.getMaxBrvOverflow() + "% overflow)");
		else
			description = description.replace("{of}", "");
		for(int i = 0; i < hd.getArguments().length; i++) {
			description = description.replace("{ail" + i + "}", "「**" + hd.getAbility().getUnit().getSpecificAilment(hd.getArguments()[i]).getName().getBest() + "**」");
			description = description.replace("{ab" + i + "}", "「**" + hd.getAbility().getUnit().getSpecificAbility(hd.getArguments()[i]).getName().getBest() + "**」");
			description = description.replace("{p" + i + "}", "「**" + hd.getAbility().getUnit().getSpecificPassive(hd.getArguments()[i]).getName().getBest() + "**」");
			description = description.replace("{s1}", "「**" + hd.getAbility().getUnit().getAbility(AttackName.S1).get(0).getName().getBest() + "**」");
			description = description.replace("{s2}", "「**" + hd.getAbility().getUnit().getAbility(AttackName.S2).get(0).getName().getBest() + "**」");
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
	public boolean blockRepeats() { return tags.contains(TAG.NOREPEAT); }
	public List<TAG> getTags() { return tags; }

	protected static Ability convertIdToAbility(Unit u, int id) {
		return u.getAbilities().get(id);
	}

	public static _AbilityEffect MissingEffect(int effectId) {
		return new _AbilityEffect(effectId, "Unknown Effect " + effectId);
	}
}