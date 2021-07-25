package com.materiabot.GameElements.Enumerators.Ailment.Effect.ValTypes;
import com.materiabot.GameElements.Ailment;

public class ValType0 extends _ValType{
	public ValType0() {
		super(0);
	}

	@Override
	public Integer[] getValues(Ailment ailment, int index, int rank) { //Rank Table direct value
		if(ailment.getRankTables()[index] != -1)
			switch(ailment.getValEditTypes()[index]) {
				case 0: //Rank Table direct value
					return new Integer[]{ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[rank][2]};
				case 1: //Rank Table direct negative value
					return new Integer[]{-ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[rank][2]};
				case 2: //Rank Table Left Value
					return new Integer[]{ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[rank][0]};
				case 3: //Rank Table Mid Value
					return new Integer[]{ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[rank][1]};
				case 4: //Rank Table Right Value
					return new Integer[]{ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[rank][2]};
				case 5: //Rank Table Left Negative Value
					return new Integer[]{-ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[rank][0]};
				case 6: //Rank Table Mid Negative Value
					return new Integer[]{-ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[rank][1]};
				case 7: //Rank Table Right Negative Value
					return new Integer[]{-ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[rank][2]};
			}
		return new Integer[]{};
	}
}