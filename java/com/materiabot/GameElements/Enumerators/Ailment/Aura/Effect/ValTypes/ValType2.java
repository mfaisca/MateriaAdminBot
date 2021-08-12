package com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect.ValTypes;
import java.util.Arrays;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Aura;

public class ValType2 extends _ValType{
	public ValType2() {
		super(2);
	}

	@Override
	public Integer[] getValues(Aura aura) {
		return Arrays.copyOfRange(aura.getRankData(), 0, aura.getAilment().getMaxStacks());
	}

	@Override
	public Integer[] getValues(Ailment fakeAil) {
		return Arrays.copyOfRange(fakeAil.getAuraRankData(), 0, fakeAil.getMaxStacks());
	}
}