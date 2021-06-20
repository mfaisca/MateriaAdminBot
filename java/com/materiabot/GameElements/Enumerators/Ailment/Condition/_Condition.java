package com.materiabot.GameElements.Enumerators.Ailment.Condition;
import com.materiabot.GameElements.Enumerators.Ailment.ConditionBlock;

public abstract class _Condition {
	protected int id;
	protected String name;
	protected String baseDescription;

	protected _Condition(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	protected _Condition(String name, String desc) {
		this.name = name;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	public String getDescription(ConditionBlock cond) {
		String description = getBaseDescription().replace("{t}", cond.getTarget().getDescription());
		for(int i = 0; i < cond.getValues().length; i++) {
			description = description.replace("{ail" + i + "}", cond.getAilment().getUnit().getSpecificAilment(cond.getValues()[i]).getName().getBest());
			description = description.replace("{ab" + i + "}", cond.getAilment().getUnit().getSpecificAbility(cond.getValues()[i]).getName().getBest());
			description = description.replace("{p" + i + "}", cond.getAilment().getUnit().getSpecificPassive(cond.getValues()[i]).getName().getBest());
			description = description.replace("{" + i + "}", "" + cond.getValues()[i]);
		}
		while(description.contains("{pl")) { //{pl1;debuff;debuffs}  |||  buff{pl2;;s}
			String plurality = description.substring(description.indexOf("{pl"), description.indexOf("}", description.indexOf("{pl")) + 1);
			int idx = plurality.charAt(3) - '0';
			String ret = cond.getValues()[idx] == 1 ? 
							plurality.substring(plurality.indexOf(";") + 1, plurality.lastIndexOf(";")) : 
							plurality.substring(plurality.lastIndexOf(";") + 1, plurality.indexOf("}"));
			description = description.replace(plurality, ret);
		}
		return description;
	}
}