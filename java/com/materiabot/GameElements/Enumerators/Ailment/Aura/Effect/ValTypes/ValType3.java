package com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect.ValTypes;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Aura;

public class ValType3 extends _ValType{
	public ValType3() {
		super(3);
	}

	@Override
	public Integer[] getValues(Aura aura) {
		return new Integer[] {aura.getRankData()[aura.getAilment().getRank()]};
	}

	@Override
	public Integer[] getValues(Ailment fakeAil) {
		return new Integer[] {fakeAil.getAuraRankData()[fakeAil.getRank()]};
	}
}