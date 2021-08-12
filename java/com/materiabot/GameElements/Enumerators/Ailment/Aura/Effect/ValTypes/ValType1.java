package com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect.ValTypes;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Aura;

public class ValType1 extends _ValType{
	public ValType1() {
		super(1);
	}

	@Override
	public Integer[] getValues(Aura aura) {
		return new Integer[] {aura.getRankData()[0]};
	}

	@Override
	public Integer[] getValues(Ailment fakeAil) {
		return new Integer[] {fakeAil.getAuraRankData()[0]};
	}
}