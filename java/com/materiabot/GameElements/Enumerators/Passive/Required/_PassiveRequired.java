package com.materiabot.GameElements.Enumerators.Passive.Required;
import com.materiabot.GameElements.Enumerators.Passive.PassiveCondition;

public abstract class _PassiveRequired {
	protected int id;
	protected String baseDescription;
	
	protected _PassiveRequired(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }
	
	public String getDescription(PassiveCondition pc) {
		String description = getBaseDescription()
				.replace("{t}", pc.getTarget() == null ? "Unknown Target: " + pc.getTargetId() : pc.getTarget().getDescription())
				.replace("{pt}", pc.getPassive().getTarget() == null ? "Unknown Target: " + pc.getPassive().getTargetId() : pc.getPassive().getTarget().getDescription());
		for(int i = 0; i < pc.getValues().length; i++) {
			description = description.replace("{ail" + i + "}", pc.getPassive().getUnit().getSpecificAilment(pc.getValues()[i]).getName().getBest());
			description = description.replace("{ab" + i + "}", pc.getPassive().getUnit().getSpecificAbility(pc.getValues()[i]).getName().getBest());
			description = description.replace("{p" + i + "}", pc.getPassive().getUnit().getSpecificPassive(pc.getValues()[i]).getName().getBest());
			description = description.replace("{" + i + "}", ""+pc.getValues()[i]);
		}
		while(description.contains("{pl")) { //{pl1;debuff;debuffs}  |||  buff{pl2;;s}
			String plurality = description.substring(description.indexOf("{pl"), description.indexOf("}", description.indexOf("{pl")) + 1);
			int idx = plurality.charAt(3) - '0';
			String ret = pc.getValues()[idx] == 1 ? 
							plurality.substring(plurality.indexOf(";") + 1, plurality.lastIndexOf(";")) : 
							plurality.substring(plurality.lastIndexOf(";") + 1, plurality.indexOf("}"));
			description = description.replace(plurality, ret);
		}
		return description;
	}
}