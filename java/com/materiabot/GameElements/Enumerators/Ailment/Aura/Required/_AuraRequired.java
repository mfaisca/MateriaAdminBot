package com.materiabot.GameElements.Enumerators.Ailment.Aura.Required;
import com.materiabot.GameElements.Aura;

public abstract class _AuraRequired {
	protected int id;
	protected String baseDescription;
	
	protected _AuraRequired(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }
	
	public String getDescription(Aura a, int index) {
		String description = getBaseDescription().replace("{t}", a.getTarget().getDescription());
		for(int i = 0; i < 2; i++) {
			description = description.replace("{ail" + i + "}", a.getAilment().getUnit().getSpecificAilment(a.getRequiredValues()[index * 2 + i].getSimpleValue()).getName().getBest());
			description = description.replace("{ab" + i + "}", a.getAilment().getUnit().getSpecificAbility(a.getRequiredValues()[index * 2 + i].getSimpleValue()).getName().getBest());
			description = description.replace("{p" + i + "}", a.getAilment().getUnit().getSpecificPassive(a.getRequiredValues()[index * 2 + i].getSimpleValue()).getName().getBest());
			description = description.replace("{" + i + "}", a.getRequiredValues()[index * 2 + i].getDescription());
		}
		while(description.contains("{pl")) { //{pl1;debuff;debuffs}  |||  buff{pl2;;s}
			String plurality = description.substring(description.indexOf("{pl"), description.indexOf("}", description.indexOf("{pl")) + 1);
			int idx = plurality.charAt(3) - '0';
			String ret = a.getRequiredValues()[index * 2 + idx].getSimpleValue().intValue() == 1 ? 
							plurality.substring(plurality.indexOf(";") + 1, plurality.lastIndexOf(";")) : 
							plurality.substring(plurality.lastIndexOf(";") + 1, plurality.indexOf("}"));
			description = description.replace(plurality, ret);
		}
		return description;
	}
}