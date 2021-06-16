package com.materiabot.GameElements;

import com.materiabot.GameElements.Enumerators.Ability.MiscConditionTarget;

public class MiscCondition {
	private int label; //XXX FIGURE THESE OUT OR ASK REM
	private int targetId;
	private MiscConditionTarget target;
	private Integer[] values = new Integer[3];
	
	public int getLabel() { return label; }
	public void setLabel(int label) { this.label = label; }
	public int getTargetId() { return targetId; }
	public void setTargetId(int targetId) { this.targetId = targetId; }
	public MiscConditionTarget getTarget() { return target; }
	public void setTarget(MiscConditionTarget target) { this.target = target; }
	public Integer[] getValues() { return values; }
	public void setValues(Integer[] values) { this.values = values; }
}