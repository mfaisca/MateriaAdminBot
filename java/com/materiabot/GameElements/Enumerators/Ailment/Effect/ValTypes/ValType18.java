package com.materiabot.GameElements.Enumerators.Ailment.Effect.ValTypes;
import com.materiabot.GameElements.Ailment;

public class ValType18 extends _ValType{
	public ValType18() {
		super(18);
	}

	@Override
	public Integer[] getValues(Ailment ailment, int index) { //Returns all rank_data for debuff-based ailment, from 0 to 8
		Integer[] ret = new Integer[8];
		for(int i = 0; i < ret.length; i++)
			switch(ailment.getValEditTypes()[index]) {
				case 0: //Rank Table direct value
					ret[i] = ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i][0];
				case 1: //Rank Table direct negative value
					ret[i] = -ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i][0];
				case 2: //Rank Table Left Value
					ret[i] = ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i][0];
				case 3: //Rank Table Mid Value
					ret[i] = ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i][1];
				case 4: //Rank Table Right Value
					ret[i] = ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i][2];
				case 5: //Rank Table Left Negative Value
					ret[i] = -ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i][0];
				case 6: //Rank Table Mid Negative Value
					ret[i] = -ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i][1];
				case 7: //Rank Table Right Negative Value
					ret[i] = -ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[i][2];
			}
		return ret;
	}
}