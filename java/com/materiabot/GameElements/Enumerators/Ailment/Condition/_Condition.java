package com.materiabot.GameElements.Enumerators.Ailment.Condition;
import com.materiabot.GameElements.Enumerators.Ailment.ConditionBlock;

public abstract class _Condition {
	protected int id;
	protected String name;
	protected String baseDescription;

	protected _Condition(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	protected _Condition(String name, String desc) {
		this.name = name;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	public String getDescription(ConditionBlock cond) {
		String r = getBaseDescription();
		if(cond.getTarget() != null)
			r = r.replace("{t}", cond.getTarget().getDescription());
		else
			r = r.replace("{t}", "**Unknown Target " + cond.getTargetId() + "**");
		for(int i = 0; i < cond.getValues().length; i++)
			r = r.replace("{" + i + "}", ""+cond.getValues()[i]);
		return r;
	}
}