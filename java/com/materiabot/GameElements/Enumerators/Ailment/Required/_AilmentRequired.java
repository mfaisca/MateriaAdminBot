package com.materiabot.GameElements.Enumerators.Ailment.Required;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.AuraTarget;

public abstract class _AilmentRequired {
	protected int id;
	protected String baseDescription;
	
	protected _AilmentRequired(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }
	
	public String getDescription(Ailment p, AuraTarget target, Integer requireTargetValue, Integer... values) {
		String description = getBaseDescription().replace("{t}", target.getDescription());
		for(int i = 0; i < values.length; i++) {
			description = description.replace("{ail" + i + "}", p.getUnit().getSpecificAilment(values[i]).getName().getBest());
			description = description.replace("{ab" + i + "}", p.getUnit().getSpecificAbility(values[i]).getName().getBest());
			description = description.replace("{p" + i + "}", p.getUnit().getSpecificPassive(values[i]).getName().getBest());
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