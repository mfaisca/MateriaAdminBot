package com.materiabot.GameElements.Enumerators.Ailment.Effect.ValTypes;
import com.materiabot.GameElements.Ailment;

public class ValType4 extends _ValType{
	public ValType4() {
		super(4);
	}

	@Override
	public Integer[] getValues(Ailment ailment, int index, int rank) { //Returns all rank_data for stack-based ailment, starting from index 5
		Integer[] ret = new Integer[ailment.getMaxStacks()];
		for(int i = 0; 0 < ret.length; i++)
			switch(ailment.getValEditTypes()[index]) {
				case 0: //Rank Table direct value
					ret[i] = ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i+5][2]; break;
				case 1: //Rank Table direct negative value
					ret[i] = -ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i+5][2]; break;
				case 2: //Rank Table Left Value
					ret[i] = ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i+5][0]; break;
				case 3: //Rank Table Mid Value
					ret[i] = ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i+5][1]; break;
				case 4: //Rank Table Right Value
					ret[i] = ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i+5][2]; break;
				case 5: //Rank Table Left Negative Value
					ret[i] = -ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i+5][0]; break;
				case 6: //Rank Table Mid Negative Value
					ret[i] = -ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i+5][1]; break;
				case 7: //Rank Table Right Negative Value
					ret[i] = -ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i+5][2]; break;
			}
		return ret;
	}
}