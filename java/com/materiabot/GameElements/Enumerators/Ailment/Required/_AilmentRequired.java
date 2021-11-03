package com.materiabot.GameElements.Enumerators.Ailment.Required;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Enumerators._Plugin;
import com.materiabot.Utils.ImageUtils;
import Shared.Methods;

public abstract class _AilmentRequired implements _Plugin {
	protected int id;
	protected String baseDescription;
	
	protected _AilmentRequired(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	public String getDescription(Ailment a, int index) {
		return applyReplaces(a, index, getBaseDescription());
	}

	protected static final String applyReplaces(Ailment a, int index, String description) {
		description = description.replace("{t}", a.getTarget().getDescription());
		description = description.replace("{u}", a.getUnit().getName());
		description = description.replace("{ail}", a.getName().getBest());
		for(int i = 0; i < 2; i++) {
			description = description.replace("{ail" + i + "}", ImageUtils.getAilmentEmote(a.getUnit(), a.getAilmentConditionValue()) + Methods.enframe(a.getUnit().getSpecificAilment(a.getAilmentConditionValue()).getName().getBest()));
			description = description.replace("{ab" + i + "}", Methods.enframe(a.getUnit().getSpecificAbility(a.getAilmentConditionValue()).getName().getBest()));
			description = description.replace("{p" + i + "}", Methods.enframe(a.getUnit().getSpecificPassive(a.getAilmentConditionValue()).getName().getBest()));
			description = description.replace("{" + i + "}", ""+a.getAilmentConditionValue());
		}
		while(description.contains("{a")) { //{a1234} = Frame Ailment
			String ailm = description.substring(description.indexOf("{a"), description.indexOf("}", description.indexOf("{a")) + 1);
			Ailment ail = a.getUnit().getSpecificAilment(Integer.valueOf(ailm.substring(2, ailm.length()-1)));
			if(ail == null)
				description = description.replace(ailm, Methods.enframe("Unknown Ailment: " + Integer.valueOf(ailm.substring(2, ailm.length()-1))));
			else
				description = description.replace(ailm, Methods.enframe(ail.getName().getBest()));
		}
		while(description.contains("{pl")) { //{pl1;debuff;debuffs}  |||  buff{pl2;;s}
			String plurality = description.substring(description.indexOf("{pl"), description.indexOf("}", description.indexOf("{pl")) + 1);
			String ret = a.getAilmentConditionValue() == 1 ? 
							plurality.substring(plurality.indexOf(";") + 1, plurality.lastIndexOf(";")) : 
							plurality.substring(plurality.lastIndexOf(";") + 1, plurality.indexOf("}"));
			description = description.replace(plurality, ret);
		}
		return description;
	}
}