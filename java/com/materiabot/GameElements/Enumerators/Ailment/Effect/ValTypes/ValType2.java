package com.materiabot.GameElements.Enumerators.Ailment.Effect.ValTypes;
import com.materiabot.GameElements.Ailment;

public class ValType2 extends _ValType{
	public ValType2() {
		super(2);
	}

	@Override
	public Integer[] getValues(Ailment ailment, int index, int rank) { //Index of effect is on #1 on the args, if no rank_table, then its specific value
		if(ailment.getRankTables()[index].intValue() == -1)
			return new Integer[]{ailment.getArgs()[2]};
		switch(ailment.getValEditTypes()[index]) {
			case 0: //Rank Table direct value
				return new Integer[]{ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[ailment.getArgs()[1]-1][2]};
			case 1: //Rank Table direct negative value
				return new Integer[]{-ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[ailment.getArgs()[1]-1][2]};
			case 2: //Rank Table Left Value
				return new Integer[]{ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[ailment.getArgs()[1]-1][0]};
			case 3: //Rank Table Mid Value
				return new Integer[]{ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[ailment.getArgs()[1]-1][1]};
			case 4: //Rank Table Right Value
				return new Integer[]{ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[ailment.getArgs()[1]-1][2]};
			case 5: //Rank Table Left Negative Value
				return new Integer[]{-ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[ailment.getArgs()[1]-1][0]};
			case 6: //Rank Table Mid Negative Value
				return new Integer[]{-ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[ailment.getArgs()[1]-1][1]};
			case 7: //Rank Table Right Negative Value
				return new Integer[]{-ailment.getRankData().get(ailment.getRankTables()[index]).getValues()[ailment.getArgs()[1]-1][2]};
		}
		return null;
	}
}