package com.materiabot.GameElements.Enumerators.Passive.Effect;
import com.materiabot.GameElements.Passive;
import com.materiabot.GameElements.Enumerators.Passive.EffectTarget;

public abstract class _PassiveEffect {
	protected int id;
	protected String baseDescription;
	
	protected _PassiveEffect(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	public String getDescription(Passive p, EffectTarget target, Integer... values) {
		String r = getBaseDescription().replace("{t}", target.getDescription());
		for(int i = 0; i < values.length; i++) {
			r = r.replace("{ail" + i + "}", p.getUnit().getSpecificAilment(values[i]).getName().getBest());
			r = r.replace("{ab" + i + "}", p.getUnit().getSpecificAbility(values[i]).getName().getBest());
			r = r.replace("{p" + i + "}", p.getUnit().getSpecificPassive(values[i]).getName().getBest());
			r = r.replace("{" + i + "}", ""+values[i]);
		}
		return r;
	}
}