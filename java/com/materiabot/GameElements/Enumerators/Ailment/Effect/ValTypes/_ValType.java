package com.materiabot.GameElements.Enumerators.Ailment.Effect.ValTypes;
import java.util.HashMap;
import com.materiabot.GameElements.Ailment;

public abstract class _ValType {
	public static final HashMap<Integer, _ValType> VAL_TYPES = new HashMap<>();
	
	private int valType;
	private boolean special = false;
	
	static {
		new ValType0();
		new ValType1();
		new ValType2();
		new ValType3();
		new ValType4();
		new ValType6();
		new ValType7();
		new ValType8();
		new ValType9();
		new ValType10();
		new ValType11();
		new ValType14();
		new ValType16();
		new ValType18();
		new ValType20();
	}

	protected _ValType(int valType) { this(valType, false); }
	protected _ValType(int valType, boolean special) {
		this.valType = valType;
		this.special = special;
		VAL_TYPES.put(valType, this);
	}

	public int getValType() { return valType; }

	public abstract Integer[] getValues(Ailment ailment, int index, int rank);
	public boolean isSpecial(int valEditType) { return special; }
	public String getSpecialText() { return null; }
}