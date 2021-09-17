package com.materiabot.GameElements.Enumerators.Ailment.Effect;
import java.util.Arrays;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Aura;
import com.materiabot.GameElements.Enumerators._Plugin;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
import com.materiabot.GameElements.Enumerators.Ailment.Effect.ValTypes._ValType;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.ImageUtils;
import Shared.Methods;

public abstract class _AilmentEffect implements _Plugin {
	protected int id;
	protected String baseDescription;
	
	protected _AilmentEffect(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	//@Override
	public String getDescription(Ailment a, int effectIndex, int rank, boolean isAuraEffect) {
		String desc = getBaseDescription();
		return applyReplaces(a, effectIndex, desc, rank, isAuraEffect);
	}
	public static final String getDescriptionAsAura(Aura aura) {
		Ailment a = new Ailment();
		a.setName(aura.getAilment().getName());
		a.setConditions(aura.getAilment().getConditions());
		a.setEffects(new Integer[] {aura.getEffectId()});
		a.setValTypes(new Integer[] {aura.getValueType()});
		a.setValEditTypes(new Integer[] {aura.getValueEditType()});
		a.setRankTables(new Integer[] {123});
		a.setArgs(aura.getAilment().getArgs());
		a.setAuraRankData(aura.getRankData());
		a.setRank(aura.getAilment().getRank());
		a.setMaxStacks(aura.getAilment().getMaxStacks());
		a.setUnit(aura.getAilment().getUnit());
		_AilmentEffect ae = Constants.AILMENT_EFFECT.get(aura.getEffectId());
		if(ae == null)
			return "Unknown Effect " + aura.getEffectId();
		return ae.getDescription(a, 0, a.getRank(), true);
	}
	
	protected static final Integer[] getArgs(Ailment a, int effectIndex, int rank, boolean isAuraEffect) {
		Integer[] values = new Integer[0];
		if(isAuraEffect) {
			com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect.ValTypes._ValType type;
			type = com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect.ValTypes._ValType.VAL_TYPES.get(a.getValTypes()[effectIndex]);
			values = type == null ? null : type.getValues(a);
		}
		else {
			_ValType type = _ValType.VAL_TYPES.get(a.getValTypes()[effectIndex]); //TODO Check all ailment effects to swap this to the new function
			values = type == null ? null : type.getValues(a, effectIndex, rank);
		}
		return values;
	}

	protected static final String applyReplaces(Ailment a, int effectIndex, String description, boolean isAuraEffect) {
		return applyReplaces(a, effectIndex, description, a.getRank(), isAuraEffect);
	}
	
	protected static final String applyReplaces(Ailment a, int effectIndex, String description, int rank, boolean isAuraEffect) {
		//if(a.getEffects()[effectIndex] != id && a.getEffects()[effectIndex] != 53) return null; //53 = Multifunction effect - hardcoded stuff ingame
		Integer[] values = getArgs(a, effectIndex, rank, isAuraEffect);
		boolean isSpecialValType = !isAuraEffect && _ValType.VAL_TYPES.get(a.getValTypes()[effectIndex]).isSpecial(a.getValEditTypes()[effectIndex]); //Special ValueType for some effects that scale off something else other than stacks(I think)
		String stack = "";
		if(a.getMaxStacks() > 1 || (isSpecialValType && values != null)) {
			stack = Arrays.asList(values).stream().map(i1 -> i1.toString()).reduce((i1, i2) -> i1 + "/" + i2).orElse("ERROR");
			String[] stacks = stack.split("/");
			if(Arrays.asList(stacks).stream().distinct().count() == 1)
				stack = stacks[0];
		}
		
		if(values != null)
			for(int i = 0; i < values.length; i++) {
				description = description.replace("{ail" + i + "}", ImageUtils.getAilmentEmote(a.getUnit(), values[i]) + Methods.enframe(a.getUnit().getSpecificAilment(values[i]).getName().getBest()));
				description = description.replace("{ab" + i + "}", Methods.enframe(a.getUnit().getSpecificAbility(values[i]).getName().getBest()));
				description = description.replace("{p" + i + "}", Methods.enframe(a.getUnit().getSpecificPassive(values[i]).getName().getBest()));
				if(a.getMaxStacks() > 1 || isSpecialValType)
					description = description.replace("{" + i + "}", stack);
				else
					description = description.replace("{" + i + "}", values[i]+"");
			}
		if(description.contains("{a}"))
			description = description.replace("{a}", Methods.enframe(a.getName().getBest()));
		if(description.contains("{u}"))
			description = description.replace("{u}", a.getUnit().getName());
		if(description.contains("{s1}"))
			description = description.replace("{s1}", Methods.enframe(a.getUnit().getAbility(AttackName.S1).get(0).getName().getBest()));
		if(description.contains("{s2}"))
			description = description.replace("{s2}", Methods.enframe(a.getUnit().getAbility(AttackName.S2).get(0).getName().getBest()));
		if(description.contains("{sEX}"))
			description = description.replace("{sEX}", Methods.enframe(a.getUnit().getAbility(AttackName.EX).get(0).getName().getBest()));
		if(description.contains("{sLD}"))
			description = description.replace("{sLD}", Methods.enframe(a.getUnit().getAbility(AttackName.LD).get(0).getName().getBest()));
		if(description.contains("{sBT}"))
			description = description.replace("{sBT}", Methods.enframe(a.getUnit().getAbility(AttackName.BT).get(0).getName().getBest()));
		if(description.contains("{sAA}"))
			description = description.replace("{sAA}", Methods.enframe(a.getUnit().getAbility(AttackName.AA).get(0).getName().getBest()));
		while(description.contains("{s") && values != null) { //{s1} = +10 || -10
			String skill = description.substring(description.indexOf("{s"), description.indexOf("}", description.indexOf("{s")) + 1);
			int idx = skill.charAt(2) - '0';
			String ret = (values[idx] > 0 ? "+" : "") + (a.getMaxStacks() > 1 || isSpecialValType ? stack : values[idx]);;
			description = description.replace(skill, ret);
		}
		while(description.contains("{a") && values != null) { //{a1234} = Frame Ailment
			String ailm = description.substring(description.indexOf("{a"), description.indexOf("}", description.indexOf("{a")) + 1);
			Ailment ail = a.getUnit().getSpecificAilment(Integer.valueOf(ailm.substring(2, ailm.length()-1)));
			if(ail == null)
				description = description.replace(ailm, Methods.enframe("Unknown Ailment: " + Integer.valueOf(ailm.substring(2, ailm.length()-1))));
			else
				description = description.replace(ailm, Methods.enframe(ail.getName().getBest()));
		}
		while(description.contains("{pl") && values != null) { //{pl1;debuff;debuffs}  |||  buff{pl2;;s}
			String plurality = description.substring(description.indexOf("{pl"), description.indexOf("}", description.indexOf("{pl")) + 1);
			int idx = plurality.charAt(3) - '0';
			String ret = values[idx] == 1 || values[idx] == -1 ? 
							plurality.substring(plurality.indexOf(";") + 1, plurality.lastIndexOf(";")) : 
							plurality.substring(plurality.lastIndexOf(";") + 1, plurality.indexOf("}"));
			description = description.replace(plurality, ret);
		}
		if(isSpecialValType) {
			Ailment t = a.getUnit().getAilments().get(a.getValSpecify()[effectIndex]);
			description += " " + _ValType.VAL_TYPES.get(a.getValTypes()[effectIndex]).getSpecialText()
										.replace("{0}", Methods.enframe(t != null ? t.getName().getBest() : "Unknown Ailment: " + a.getValSpecify()[effectIndex]));
		}
		return description.trim();
	}
}