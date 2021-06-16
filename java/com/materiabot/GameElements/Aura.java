package com.materiabot.GameElements;
import com.materiabot.GameElements.Enumerators.Ailment.ConditionBlock;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.AuraTarget;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Effect._AuraEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Required._AuraRequired;

public class Aura {
	private int id, effectDataId;
	private Integer[] requiredConditionsIds, rankData;
	private _AuraRequired[] requiredConditions;
	private ConditionBlock[] requiredValues;
	private int effectId, typeId; //same thing, but typeId is used if effectId is 0 or -1, for whatever reason
	private _AuraEffect effect;
	private int targetId;
	private AuraTarget target;
	private int valueType, valueEditType;
	private Ailment ailment;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public int getEffectDataId() { return effectDataId; }
	public void setEffectDataId(int effectDataId) { this.effectDataId = effectDataId; }
	public Integer[] getRequiredConditionsIds() { return requiredConditionsIds; }
	public void setRequiredConditionsIds(Integer[] requiredConditions) { this.requiredConditionsIds = requiredConditions; }
	public _AuraRequired[] getRequiredConditions() { return requiredConditions; }
	public void setRequiredConditions(_AuraRequired[] requiredConditions) { this.requiredConditions = requiredConditions; }
	public ConditionBlock[] getRequiredValues() { return requiredValues; }
	public void setRequiredValues(ConditionBlock[] requiredValues) { this.requiredValues = requiredValues; }
	public Integer[] getRankData() { return rankData; }
	public void setRankData(Integer[] rankData) { this.rankData = rankData; }
	public int getEffectId() { return effectId; }
	public void setEffectId(int effectId) { this.effectId = effectId; }
	public int getTypeId() { return typeId; }
	public void setTypeId(int typeId) { this.typeId = typeId; }
	public _AuraEffect getEffect() { return effect; }
	public void setEffect(_AuraEffect effect) { this.effect = effect; }
	public int getTargetId() { return targetId; }
	public void setTargetId(int targetId) { this.targetId = targetId; }
	public AuraTarget getTarget() { return target; }
	public void setTarget(AuraTarget target) { this.target = target; }
	public int getValueType() { return valueType; }
	public void setValueType(int valueType) { this.valueType = valueType; }
	public int getValueEditType() { return valueEditType; }
	public void setValueEditType(int valueEditType) { this.valueEditType = valueEditType; }
	public Ailment getAilment() { return ailment; }
	public void setAilment(Ailment ailment) { this.ailment = ailment; }
}