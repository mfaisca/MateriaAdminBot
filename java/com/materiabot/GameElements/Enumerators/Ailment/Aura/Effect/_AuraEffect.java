package com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect;
import com.materiabot.GameElements.Aura;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect.ValTypes._ValType;

public abstract class _AuraEffect {
	protected int id;
	protected String baseDescription;
	
	protected _AuraEffect(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	public String getDescription(Aura a) {
		String description = getBaseDescription().replace("{t}", a.getTarget().getDescription());
		Integer[] values = _ValType.VAL_TYPES.get(a.getValueType()).getValues(a);
		for(int i = 0; i < values.length; i++) {
			description = description.replace("{ail" + i + "}", a.getAilment().getUnit().getSpecificAilment(values[i]).getName().getBest());
			description = description.replace("{ab" + i + "}", a.getAilment().getUnit().getSpecificAbility(values[i]).getName().getBest());
			description = description.replace("{p" + i + "}", a.getAilment().getUnit().getSpecificPassive(values[i]).getName().getBest());
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