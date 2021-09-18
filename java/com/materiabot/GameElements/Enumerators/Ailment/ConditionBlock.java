package com.materiabot.GameElements.Enumerators.Ailment;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Enumerators.Passive.RequiredTarget;
import com.materiabot.GameElements.Enumerators.Passive.Required._PassiveRequired;

public class ConditionBlock{
	private Ailment ailment;
	private Integer id;
	private List<ConditionBlock> conditions = new LinkedList<>();
	private String name;
	private Integer targetId;
	private RequiredTarget target;
	private Integer conditionId;
	private _PassiveRequired condition;
	private Integer[] values;	
	
	public ConditionBlock(Ailment a) {
		ailment = a;
	}

	public Ailment getAilment() { return ailment; }
	public void setAilment(Ailment a) { ailment = a; }
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Integer getTargetId() { return targetId; }
	public void setTargetId(Integer target) { this.targetId = target; }
	public RequiredTarget getTarget() { return target; }
	public void setTarget(RequiredTarget target) { this.target = target; }
	public Integer getConditionId() { return conditionId; }
	public void setConditionId(Integer conditionId) { this.conditionId = conditionId; }
	public _PassiveRequired getCondition() { return condition; }
	public void setCondition(_PassiveRequired condition) { this.condition = condition; }
	public Integer[] getValues() { return values; }
	public void setValues(Integer[] values) { this.values = values; }
	public List<ConditionBlock> getConditions() { return conditions; }
	
	public String getDescription() {
		if(condition == null)
			return "Unknown Condition: " + getConditionId();
		return condition.getDescription(this);
	}
}