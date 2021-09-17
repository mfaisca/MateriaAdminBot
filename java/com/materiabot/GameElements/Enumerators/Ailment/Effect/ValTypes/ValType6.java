package com.materiabot.GameElements.Enumerators.Ailment.Effect.ValTypes;
import com.materiabot.GameElements.Ailment;

public class ValType6 extends _ValType{
	public ValType6() {
		super(6, true);
	}

	@Override
	public Integer[] getValues(Ailment ailment, int index, int rank) { //Returns val specify
		if(ailment.getValEditTypes()[index] == 1)
			return new Integer[] {-ailment.getValSpecify()[index]};
		return new Integer[] {ailment.getValSpecify()[index]};
	}
	@Override
	public boolean isSpecial(int valEditType) { 
		return valEditType == 16;
	}
	@Override
	public String getSpecialText() {
		return "per existing debuff";
	}
}