package com.materiabot.GameElements.Enumerators.Ailment.Effect;
import java.util.Arrays;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
import com.materiabot.GameElements.Enumerators.Ailment.Effect.ValTypes._ValType;

public abstract class _AilmentEffect {
	protected int id;
	protected String baseDescription;
	
	protected _AilmentEffect(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	public String getDescription(Ailment a, int effectIndex) {
		return getDescription(a, effectIndex, a.getRank());
	}
	public String getDescription(Ailment a, int effectIndex, int rank) {
		return applyReplaces(a, effectIndex, getBaseDescription(), rank);
	}

	protected final String applyReplaces(Ailment a, int effectIndex, String description) {
		return applyReplaces(a, effectIndex, description, a.getRank());
	}
	protected final String applyReplaces(Ailment a, int effectIndex, String description, int rank) {
		if(a.getEffects()[effectIndex] != id && a.getEffects()[effectIndex] != 53) return null; //53 = Multifunction effect - hardcoded stuff ingame
		Integer[] values = _ValType.VAL_TYPES.get(a.getValTypes()[effectIndex]).getValues(a, effectIndex, rank);
		
		String stack = "";
		if(a.getMaxStacks() > 1) {
			stack = Arrays.asList(values).stream().map(i1 -> i1.toString()).reduce((i1, i2) -> i1 + "/" + i2).orElse("ERROR");
			String[] stacks = stack.split("/");
			if(Arrays.asList(stacks).stream().distinct().count() == 1)
				stack = stacks[0];
		}
		
		if(values != null)
			for(int i = 0; i < values.length; i++) {
				description = description.replace("{ail" + i + "}", "「**" + a.getUnit().getSpecificAilment(values[i]).getName().getBest() + "**」");
				description = description.replace("{ab" + i + "}", "「**" + a.getUnit().getSpecificAbility(values[i]).getName().getBest() + "**」");
				description = description.replace("{p" + i + "}", "「**" + a.getUnit().getSpecificPassive(values[i]).getName().getBest() + "**」");
				if(a.getMaxStacks() > 1)
					description = description.replace("{" + i + "}", stack);
				else
					description = description.replace("{" + i + "}", values[i]+"");
			}
		if(description.contains("{s1}"))
			description = description.replace("{s1}", "「**" + a.getUnit().getAbility(AttackName.S1).get(0).getName().getBest() + "**」");
		if(description.contains("{s2}"))
			description = description.replace("{s2}", "「**" + a.getUnit().getAbility(AttackName.S2).get(0).getName().getBest() + "**」");
		if(description.contains("{sEX}"))
			description = description.replace("{sEX}", "「**" + a.getUnit().getAbility(AttackName.EX).get(0).getName().getBest() + "**」");
		if(description.contains("{sLD}"))
			description = description.replace("{sLD}", "「**" + a.getUnit().getAbility(AttackName.LD).get(0).getName().getBest() + "**」");
		if(description.contains("{sBT}"))
			description = description.replace("{sBT}", "「**" + a.getUnit().getAbility(AttackName.BT).get(0).getName().getBest() + "**」");
		if(description.contains("{sAA}"))
			description = description.replace("{sAA}", "「**" + a.getUnit().getAbility(AttackName.AA).get(0).getName().getBest() + "**」");
		while(description.contains("{s")) { //{s1} = +10 || -10
			String plurality = description.substring(description.indexOf("{s"), description.indexOf("}", description.indexOf("{s")) + 1);
			int idx = plurality.charAt(2) - '0';
			String ret = (values[idx] > 0 ? "+" : "") + (a.getMaxStacks() > 1 ? stack : values[idx]);
			description = description.replace(plurality, ret);
		}
		while(description.contains("{pl")) { //{pl1;debuff;debuffs}  |||  buff{pl2;;s}
			String plurality = description.substring(description.indexOf("{pl"), description.indexOf("}", description.indexOf("{pl")) + 1);
			int idx = plurality.charAt(3) - '0';
			String ret = values[idx] == 1 || values[idx] == -1 ? 
							plurality.substring(plurality.indexOf(";") + 1, plurality.lastIndexOf(";")) : 
							plurality.substring(plurality.lastIndexOf(";") + 1, plurality.indexOf("}"));
			description = description.replace(plurality, ret);
		}
		return description;
	}
}