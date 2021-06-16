package com.materiabot.GameElements.Enumerators.Ailment;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Enumerators.Ailment.Condition.ConditionTarget;
import com.materiabot.GameElements.Enumerators.Ailment.Condition._Condition;

public class ConditionBlock {
	private Integer simpleValue;
	
	public Integer getSimpleValue() { return simpleValue; }
	public void setSimpleValue(Integer simpleValue) { this.simpleValue = simpleValue; }
	///
	private int id;
	private List<ConditionBlock> conditions = new LinkedList<ConditionBlock>();
	private String name;
	private int targetId;
	private ConditionTarget target;
	private int conditionId;
	private _Condition condition;
	private Integer[] values;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public int getTargetId() { return targetId; }
	public void setTargetId(int target) { this.targetId = target; }
	public ConditionTarget getTarget() { return target; }
	public void setTarget(ConditionTarget target) { this.target = target; }
	public int getConditionId() { return conditionId; }
	public void setConditionId(int conditionId) { this.conditionId = conditionId; }
	public _Condition getCondition() { return condition; }
	public void setCondition(_Condition condition) { this.condition = condition; }
	public Integer[] getValues() { return values; }
	public void setValues(Integer[] values) { this.values = values; }
	public List<ConditionBlock> getConditions() { return conditions; }
	
	public String getDescription() {
		if(simpleValue != null)
			return simpleValue.intValue() + "";
		if(condition == null)
			return "Unknown Condition: " + conditionId;
		return condition.getDescription(this);
	}
}