package com.materiabot.GameElements;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Enumerators.Passive.PassiveCondition;
import com.materiabot.GameElements.Enumerators.Passive.PassiveEffect;
import com.materiabot.GameElements.Enumerators.Passive.PassiveTarget;

public class Passive implements Comparable<Passive>{
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
	
	public Passive() {}
	public Passive(String text) { setName(new Text(text)); setManualDesc(text); }
	
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
		if(this.getLevel() == 20 || this.getLevel() == 55 || this.getLevel() == 60 || 
			this.getLevel() == 70 || this.getLevel() == 75 || this.getLevel() == 80 || this.getLevel() == 85 || this.getLevel() == 90)
			return this.getDesc().getBest();
		List<String> ret = new LinkedList<String>();
		ret.add(buildCondition());
		boolean hasCondition = ret.get(0).length() > 0;
		for(PassiveEffect pe : getEffects())
			ret.add((hasCondition ? "\t" : "") + pe.getDescription());
		return ret.stream().distinct().filter(s -> s.length() > 0).reduce((s1, s2) -> s1 + System.lineSeparator() + s2).orElse("").trim();
	}
	private String buildCondition() {
		String condition1 = getConditions().get(0).getDescription().trim();
		String condition2 = getConditions().get(1).getDescription().trim();
		if(condition1.length() == 0)
			condition1 = condition2;
		else if(getConditions().get(1).getRequiredId() == 53)
			condition1 += " " + condition2;
		else if(condition2.length() > 0)
			condition1 = condition1.replace(":", " and") + condition2.replace("When", "").replace("While", "");
		return condition1.trim();
	}
	
	public static final Passive NULL(int id) {
		return new Passive("Unknown Passive " + id) {};
	}
	@Override
	public int compareTo(Passive other) {
		int ret = Integer.compare(this.getLevel(), other.getLevel());
		return ret != 0 ? ret : Integer.compare(this.getId(), other.getId());
	}
}