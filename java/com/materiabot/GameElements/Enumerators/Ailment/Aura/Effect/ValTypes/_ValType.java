package com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect.ValTypes;
import java.util.HashMap;
import com.materiabot.GameElements.Aura;

public abstract class _ValType {
	public static final HashMap<Integer, _ValType> VAL_TYPES = new HashMap<Integer, _ValType>();
	
	private int valType;
	
	static {
		new ValType1();
		new ValType2();
		new ValType3();
		new ValType6();
	}
	
	public _ValType(int valType) {
		this.valType = valType;
		VAL_TYPES.put(valType, this);
	}

	public int getValType() { return valType; }
	
	public abstract Integer[] getValues(Aura aura);
}