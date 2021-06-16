package com.materiabot.GameElements.Enumerators.Ailment.Aura.Required;
import com.materiabot.GameElements.Aura;

public abstract class _AuraRequired {
	protected int id;
	protected String baseDescription;
	
	protected _AuraRequired(int id, String desc) {
		this.id = id;
		this.baseDescription = desc;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }
	
	public String getDescription(Aura p, int index) {
		String r = baseDescription;
		for(int i = 0; i < 2; i++) {
			r = r.replace("{ail" + i + "}", p.getAilment().getUnit().getSpecificAilment(p.getRequiredValues()[index * 2 + i].getSimpleValue()).getName().getBest());
			r = r.replace("{ab" + i + "}", p.getAilment().getUnit().getSpecificAbility(p.getRequiredValues()[index * 2 + i].getSimpleValue()).getName().getBest());
			r = r.replace("{p" + i + "}", p.getAilment().getUnit().getSpecificPassive(p.getRequiredValues()[index * 2 + i].getSimpleValue()).getName().getBest());
			r = r.replace("{" + i + "}", p.getRequiredValues()[index * 2 + i].getDescription());
		}
		return r;
	}
}