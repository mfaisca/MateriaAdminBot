package com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect;
import java.util.Arrays;
import com.materiabot.GameElements.Aura;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
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
		return applyReplaces(a, getBaseDescription());
	}
	
	public String applyReplaces(Aura a, String description) {
		description = description.replace("{t}", a.getTarget().getDescription());
		Integer[] values = _ValType.VAL_TYPES.get(a.getValueType()).getValues(a);
		String stack = "";
		if(a.getAilment().getMaxStacks() > 1) {
			stack = Arrays.asList(values).stream().map(i1 -> i1.toString()).reduce((i1, i2) -> i1 + "/" + i2).orElse("ERROR");
			String[] stacks = stack.split("/");
			if(Arrays.asList(stacks).stream().distinct().count() == 1)
				stack = stacks[0];
		}
		if(description.contains("{u}"))
			description = description.replace("{u}", a.getAilment().getUnit().getName());
		if(description.contains("{s1}"))
			description = description.replace("{s1}", "「**" + a.getAilment().getUnit().getAbility(AttackName.S1).get(0).getName().getBest() + "**」");
		if(description.contains("{s2}"))
			description = description.replace("{s2}", "「**" + a.getAilment().getUnit().getAbility(AttackName.S2).get(0).getName().getBest() + "**」");
		if(description.contains("{sEX}"))
			description = description.replace("{sEX}", "「**" + a.getAilment().getUnit().getAbility(AttackName.EX).get(0).getName().getBest() + "**」");
		if(description.contains("{sLD}"))
			description = description.replace("{sLD}", "「**" + a.getAilment().getUnit().getAbility(AttackName.LD).get(0).getName().getBest() + "**」");
		if(description.contains("{sBT}"))
			description = description.replace("{sBT}", "「**" + a.getAilment().getUnit().getAbility(AttackName.BT).get(0).getName().getBest() + "**」");
		if(description.contains("{sAA}"))
			description = description.replace("{sAA}", "「**" + a.getAilment().getUnit().getAbility(AttackName.AA).get(0).getName().getBest() + "**」");
		for(int i = 0; i < values.length; i++) {
			description = description.replace("{ail" + i + "}", a.getAilment().getUnit().getSpecificAilment(values[i]).getName().getBest());
			description = description.replace("{ab" + i + "}", a.getAilment().getUnit().getSpecificAbility(values[i]).getName().getBest());
			description = description.replace("{p" + i + "}", a.getAilment().getUnit().getSpecificPassive(values[i]).getName().getBest());
			if(a.getAilment().getMaxStacks() > 1)
				description = description.replace("{" + i + "}", stack);
			else
				description = description.replace("{" + i + "}", values[i]+"");
		}
		while(description.contains("{s") && values != null) { //{s1} = +10 || -10
			String plurality = description.substring(description.indexOf("{s"), description.indexOf("}", description.indexOf("{s")) + 1);
			int idx = plurality.charAt(2) - '0';
			String ret = (values[idx] > 0 ? "+" : "") + (a.getAilment().getMaxStacks() > 1 ? stack : values[idx]);
			description = description.replace(plurality, ret);
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