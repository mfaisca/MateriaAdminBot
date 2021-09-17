package com.materiabot.GameElements.Enumerators.Ability.MiscConditionLabel;
import com.materiabot.GameElements.MiscCondition;
import com.materiabot.GameElements.Enumerators._Plugin;
import com.materiabot.Utils.ImageUtils;
import Shared.Methods;

public class _MiscConditionLabel implements _Plugin{	
	protected int id;
	protected String baseDescription;
	
	protected _MiscConditionLabel(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	//@Override
	public String getDescription(MiscCondition a) {
		String desc = getBaseDescription();
		return applyReplaces(a, desc);
	}
	protected static final String applyReplaces(MiscCondition a, String description) {
		Integer[] values = a.getValues();
		description = description.replace("{t}", a.getTarget().getDescription());
		description = description.replace("{u}", a.getAb().getUnit().getName());
		for(int i = 0; i < values.length; i++) {
			description = description.replace("{ail" + i + "}", ImageUtils.getAilmentEmote(a.getAb().getUnit(), values[i]) + Methods.enframe(a.getAb().getUnit().getSpecificAilment(values[i]).getName().getBest()));
			description = description.replace("{ab" + i + "}", Methods.enframe(a.getAb().getUnit().getSpecificAbility(values[i]).getName().getBest()));
			description = description.replace("{p" + i + "}", Methods.enframe(a.getAb().getUnit().getSpecificPassive(values[i]).getName().getBest()));
			description = description.replace("{" + i + "}", "" + values[i]);
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