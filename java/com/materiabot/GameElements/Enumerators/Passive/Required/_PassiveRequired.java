package com.materiabot.GameElements.Enumerators.Passive.Required;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators._Plugin;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
import com.materiabot.GameElements.Enumerators.Ailment.ConditionBlock;
import com.materiabot.GameElements.Enumerators.Passive.PassiveCondition;
import com.materiabot.GameElements.Enumerators.Passive.RequiredTarget;
import com.materiabot.Utils.ImageUtils;
import Shared.Methods;

public abstract class _PassiveRequired implements _Plugin {
	protected int id;
	protected String baseDescription;
	
	protected _PassiveRequired(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	//@Override
	public String getDescription(PassiveCondition pc) {
		String desc = getBaseDescription();
		return applyReplaces(pc, desc);
	}
	//@Override
	public String getDescription(ConditionBlock cb) {
		String desc = getBaseDescription();
		return applyReplaces(cb, desc);
	}
	protected final String applyReplaces(PassiveCondition pc, String description) {
		return applyReplaces(pc.getPassive().getUnit(), pc.getTarget(), pc.getTargetId(), description, pc.getValues());
	}
	protected final String applyReplaces(ConditionBlock pc, String description) {
		return applyReplaces(pc.getAilment().getUnit(), pc.getTarget(), pc.getTargetId(), description, pc.getValues());
	}
	private final String applyReplaces(Unit u, RequiredTarget target, Integer targetId, String description, Integer[] values) {
		description = description
				.replace("{t}", target == null ? "Unknown Target: " + targetId : target.getDescription());
		for(int i = 0; i < values.length; i++) {
			description = description.replace("{ail" + i + "}", ImageUtils.getAilmentEmote(u, values[i]) + Methods.enframe(u.getSpecificAilment(values[i]).getName().getBest()));
			description = description.replace("{ab" + i + "}", Methods.enframe(u.getSpecificAbility(values[i]).getName().getBest()));
			description = description.replace("{p" + i + "}", Methods.enframe(u.getSpecificPassive(values[i]).getName().getBest()));
			description = description.replace("{s1}", Methods.enframe(u.getAbility(AttackName.S1).get(0).getName().getBest()));
			description = description.replace("{s2}", Methods.enframe(u.getAbility(AttackName.S2).get(0).getName().getBest()));
			description = description.replace("{" + i + "}", ""+values[i]);
		}
		while(description.contains("{pl")) { //{pl1;debuff;debuffs}  |||  buff{pl2;;s}
			String plurality = description.substring(description.indexOf("{pl"), description.indexOf("}", description.indexOf("{pl")) + 1);
			int idx = plurality.charAt(3) - '0';
			String ret = values[idx] == 1 ? 
							plurality.substring(plurality.indexOf(";") + 1, plurality.lastIndexOf(";")) : 
							plurality.substring(plurality.lastIndexOf(";") + 1, plurality.indexOf("}"));
			description = description.replace(plurality, ret);
		}
		return description;
	}
}