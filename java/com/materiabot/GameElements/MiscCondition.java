package com.materiabot.GameElements;
import com.materiabot.GameElements.Enumerators.Ability.MiscConditionTarget;
import com.materiabot.GameElements.Enumerators.Ability.MiscConditionLabel._MiscConditionLabel;
import com.materiabot.GameElements.Enumerators.Ailment.ConditionBlock;

public class MiscCondition {
	private ChainAbility ab;
	private int labelId;
	private _MiscConditionLabel label;
	private int targetId;
	private MiscConditionTarget target;
	private Integer[] values = new Integer[3];
	private ConditionBlock condition = null;
	
	public ChainAbility getAb() { return ab; }
	public void setAb(ChainAbility ab) { this.ab = ab; }
	public int getLabelId() { return labelId; }
	public void setLabelId(int labelId) { this.labelId = labelId; }
	public _MiscConditionLabel getLabel() { return label; }
	public void setLabel(_MiscConditionLabel label) { this.label = label; }
	public int getTargetId() { return targetId; }
	public void setTargetId(int targetId) { this.targetId = targetId; }
	public MiscConditionTarget getTarget() { return target; }
	public void setTarget(MiscConditionTarget target) { this.target = target; }
	public Integer[] getValues() { return values; }
	public void setValues(Integer[] values) { this.values = values; }
	public ConditionBlock getCondition() { return condition; }
	public void setCondition(ConditionBlock condition) { this.condition = condition; }
}