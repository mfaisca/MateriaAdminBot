package com.materiabot.GameElements.Enumerators.Passive.Effect;
import java.util.Arrays;
import java.util.List;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
import com.materiabot.GameElements.Enumerators.Passive.PassiveEffect;

public abstract class _PassiveEffect {
	public static enum TAG{
		ABILITY1;
	}
	
	protected int id;
	protected String baseDescription;
	protected List<TAG> tags;
	
	protected _PassiveEffect(int id, String desc, TAG... tags) {
		this.id = id;
		this.baseDescription = desc;
		this.tags = Arrays.asList(tags);
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	public final boolean hasAbility1() { return tags.contains(TAG.ABILITY1); }
	
	public String getDescription(PassiveEffect p) {
		return applyReplaces(p, getBaseDescription());
	}
	protected String applyReplaces(PassiveEffect p, String description) {
		description = description
				.replace("{t}", p.getTarget() == null ? "Unknown Target: " + p.getTargetId() : p.getTarget().getDescription())
				.replace("{pt}", p.getPassive().getTarget() == null ? "Unknown Target: " + p.getPassive().getTargetId() : p.getPassive().getTarget().getDescription());
		for(int i = 0; i < p.getValues().length; i++) {
			description = description.replace("{ail" + i + "}", "「**" + p.getPassive().getUnit().getSpecificAilment(p.getValues()[i]).getName().getBest() + "**」");
			description = description.replace("{ab" + i + "}", "「**" + p.getPassive().getUnit().getSpecificAbility(p.getValues()[i]).getName().getBest() + "**」");
			description = description.replace("{p" + i + "}", "「**" + p.getPassive().getUnit().getSpecificPassive(p.getValues()[i]).getName().getBest() + "**」");
			description = description.replace("{s1}", "「**" + p.getPassive().getUnit().getAbility(AttackName.S1).get(0).getName().getBest() + "**」");
			description = description.replace("{s2}", "「**" + p.getPassive().getUnit().getAbility(AttackName.S2).get(0).getName().getBest() + "**」");
			description = description.replace("{" + i + "}", ""+p.getValues()[i]);
		}
		while(description.contains("{pl")) { //{pl1;debuff;debuffs}  |||  buff{pl2;;s}
			String plurality = description.substring(description.indexOf("{pl"), description.indexOf("}", description.indexOf("{pl")) + 1);
			int idx = plurality.charAt(3) - '0';
			String ret = p.getValues()[idx] == 1 ? 
					plurality.substring(plurality.indexOf(";") + 1, plurality.lastIndexOf(";")) : 
						plurality.substring(plurality.lastIndexOf(";") + 1, plurality.indexOf("}"));
			description = description.replace(plurality, ret);
		}
		return description;
	}
}