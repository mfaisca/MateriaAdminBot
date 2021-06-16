package com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect.ValTypes;
import com.materiabot.GameElements.Aura;

public class ValType6 extends _ValType{
	public ValType6() {
		super(6);
	}

	@Override
	public Integer[] getValues(Aura aura) {
		return aura.getRankData();
	}
}