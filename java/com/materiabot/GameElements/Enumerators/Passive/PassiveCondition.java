package com.materiabot.GameElements.Enumerators.Passive;
import java.util.Arrays;
import com.materiabot.GameElements.Passive;
import com.materiabot.GameElements.Enumerators.Passive.Required._PassiveRequired;
import com.materiabot.Utils.Constants;

public class PassiveCondition {
	private Passive passive;
	private Integer requiredId;
	private _PassiveRequired required;
	private Integer targetId;
	private RequiredTarget target;
	private Integer[] values;
	private Integer targetValue;
	
	public PassiveCondition(Passive p, Integer requiredId, Integer target, 
						Integer targetValue, Integer... values) {
		this.passive = p;
		this.requiredId = requiredId;
		this.targetId = target;
		this.targetValue = targetValue;
		this.values = values;
		this.required = Constants.PASSIVE_REQUIRED.get(requiredId);
		this.target = RequiredTarget.get(targetId);
	}
	
	public Passive getPassive() { return passive; }
	public Integer getRequiredId() { return requiredId; }
	public _PassiveRequired getRequired() { return required; }
	public Integer getTargetId() { return targetId; }
	public RequiredTarget getTarget() { return target; }
	public Integer[] getValues() { return values; }
	public Integer getTargetValue() { return targetValue; }

	public String getDescription() {
		if(required == null)
			return "Unknown Condition " + requiredId + Arrays.toString(values) + " T:" + targetId + "/" + targetValue;
		else
			return required.getDescription(this);
	}
}