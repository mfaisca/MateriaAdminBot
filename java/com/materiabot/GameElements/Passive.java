package com.materiabot.GameElements;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Enumerators.Passive.PassiveCondition;
import com.materiabot.GameElements.Enumerators.Passive.PassiveEffect;
import com.materiabot.GameElements.Enumerators.Passive.PassiveTarget;

public class Passive{
	private int id;
	private Text name;
	private Unit unit;
	private Text desc, shortDesc;
	private String manualDesc;
	private int cp, level;
	private int targetId;
	private PassiveTarget target;
	private List<PassiveEffect> effects = new LinkedList<PassiveEffect>();
	private List<PassiveCondition> conditions = new LinkedList<PassiveCondition>();
	
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public Text getName() { return name; }
	public void setName(Text name) { this.name = name; }
	public Unit getUnit() { return unit; }
	public void setUnit(Unit unit) { this.unit = unit; }
	public Text getDesc() { return desc; }
	public void setDesc(Text desc) { this.desc = desc; }
	public Text getShortDesc() { return shortDesc; }
	public void setShortDesc(Text shortDesc) { this.shortDesc = shortDesc; }
	public String getManualDesc() { return manualDesc; }
	public void setManualDesc(String manualDesc) { this.manualDesc = manualDesc; }
	public int getCp() { return cp; }
	public void setCp(int cp) { this.cp = cp; }
	public int getLevel() { return level; }
	public void setLevel(int level) { this.level = level; }
	public List<PassiveEffect> getEffects() { return effects; }
	public List<PassiveCondition> getConditions() { return conditions; }
	public int getTargetId() { return targetId; }
	public void setTargetId(int targetId) { this.targetId = targetId; }
	public PassiveTarget getTarget() { return target; }
	public void setTarget(PassiveTarget target) { this.target = target; }
	
	public String generateDescription() {
		if(getManualDesc() != null)
			return getManualDesc();
		String ret = "";
		for(PassiveCondition pc : getConditions())
			ret += System.lineSeparator() + pc.getDescription();
		ret = ret.trim();
		boolean hasCondition = ret.length() > 0;
		for(PassiveEffect pe : getEffects())
			ret += System.lineSeparator() + (hasCondition ? "\t" : "") + pe.getDescription();
		return ret.trim();
	}
}