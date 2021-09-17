package com.materiabot.GameElements.Enumerators.Ailment.Aura.Required;
import com.materiabot.GameElements.Aura;
import com.materiabot.GameElements.Enumerators._Plugin;
import com.materiabot.Utils.ImageUtils;
import Shared.Methods;

public abstract class _AuraRequired implements _Plugin {
	protected int id;
	protected String baseDescription;
	
	protected _AuraRequired(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }
	
	public String getDescription(Aura a, int index) {
		return applyReplaces(a, index, getBaseDescription());
	}

	protected static final String applyReplaces(Aura a, int index, String description) {
		description = description.replace("{t}", a.getTarget().getDescription());
		description = description.replace("{u}", a.getAilment().getUnit().getName());
		description = description.replace("{ail}", a.getAilment().getName().getBest());
		for(int i = 0; i < 2; i++) {
			description = description.replace("{ail" + i + "}", ImageUtils.getAilmentEmote(a.getAilment().getUnit(), a.getRequiredValues()[index * 2 + i]) + Methods.enframe(a.getAilment().getUnit().getSpecificAilment(a.getRequiredValues()[index * 2 + i]).getName().getBest()));
			description = description.replace("{ab" + i + "}", Methods.enframe(a.getAilment().getUnit().getSpecificAbility(a.getRequiredValues()[index * 2 + i]).getName().getBest()));
			description = description.replace("{p" + i + "}", Methods.enframe(a.getAilment().getUnit().getSpecificPassive(a.getRequiredValues()[index * 2 + i]).getName().getBest()));
			description = description.replace("{" + i + "}", ""+a.getRequiredValues()[index * 2 + i]);
		}
		while(description.contains("{pl")) { //{pl1;debuff;debuffs}  |||  buff{pl2;;s}
			String plurality = description.substring(description.indexOf("{pl"), description.indexOf("}", description.indexOf("{pl")) + 1);
			int idx = plurality.charAt(3) - '0';
			String ret = a.getRequiredValues()[index * 2 + idx].intValue() == 1 ? 
							plurality.substring(plurality.indexOf(";") + 1, plurality.lastIndexOf(";")) : 
							plurality.substring(plurality.lastIndexOf(";") + 1, plurality.indexOf("}"));
			description = description.replace(plurality, ret);
		}
		return description;
	}
}