package com.materiabot.GameElements;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Enumerators.Ability.HitData.AttackType;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Target;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Type;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Effect._AbilityEffect;

public class HitData {
	public static class Extra{
		public HitData t;
		public int count = 0;
		public Extra hp = null;
		public boolean showType = false;
		public boolean showElements = false;
		
		public Extra(HitData t) { this.t = t; }
		
		public String getHitsDescription() {
			if(t.effect.isHP() && Type.isHP(t.type))
				return (t.target == Target.ST ? "ST HP" : "AoE HP") + (t.getType() == Type.NoBRVConsumption ? " (Doesn't consume BRV)" : "");
			else if(Type.isBRV(t.type))
				return (count > 0 ? count + "x " : "") + t.target.name() + 
					(showType && t.attackType != null ? " " + t.attackType.getEmote() : "") + 
					(showElements && t.elements != null && t.elements.isEmpty() ? " " + t.elements.stream().map(e -> e.getEmote()).reduce((s1,  s2) -> s1 + s2).orElse("") : "") + 
					" BRV" + (hp != null ? (" + " + hp.getHitsDescription()) : ""); 
			else
				return "";
		}
	}
	
	private Ability ability;
	private int id;
	private int typeId;
	private Type type;
	private int attackTypeId;
	private AttackType attackType;
	private List<Element> elements = new LinkedList<>();
	private int targetId;
	private Target target;
	private int effectId;
	private _AbilityEffect effect;
	private int[] arguments;
	private int effectValueType;
	private int splashValue = 0; //Helpful for parsing, not a real camp
	
	private float brvRate;
	private int maxBrvOverflow;
	private int maxBrvOverflowBreak;
	private int singleTargetBrvRate;
	private int brvDamageLimitUp;
	private int maxBrvLimitUp;
	private String manualDescription;

	public HitData(Ability a) { ability = a; }
	public HitData(Ability a, String m) { this(a); this.setManualDescription(m); }
	
	public Ability getAbility() { return ability; }
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public int getTypeId() { return typeId; }
	public void setTypeId(int typeId) { this.typeId = typeId; }
	public Type getType() { return type; }
	public void setType(Type type) { this.type = type; }
	public int getAttackTypeId() { return attackTypeId; }
	public void setAttackTypeId(int attackTypeId) { this.attackTypeId = attackTypeId; }
	public AttackType getAttackType() { return attackType; }
	public void setAttackType(AttackType attackType) { this.attackType = attackType; }
	public int getTargetId() { return targetId; }
	public void setTargetId(int targetId) { this.targetId = targetId; }
	public Target getTarget() { return target; }
	public void setTarget(Target target) { this.target = target; }
	public int getEffectId() { return effectId; }
	public void setEffectId(int effectId) { this.effectId = effectId; }
	public _AbilityEffect getEffect() { return effect; }
	public void setEffect(_AbilityEffect effect) { this.effect = effect; }
	public int[] getArguments() { return arguments; }
	public void setArguments(int[] arguments) { this.arguments = arguments; }
	public int getEffectValueType() { return effectValueType; }
	public void setEffectValueType(int effectValueType) { this.effectValueType = effectValueType; }
	public int getSplashValue() { return splashValue; }
	public void setSplashValue(int splashValue) { this.splashValue = splashValue; }
	public float getBrvRate() { return brvRate; }
	public void setBrvRate(float brvRate) { this.brvRate = brvRate; }
	public int getMaxBrvOverflow() { return maxBrvOverflow; }
	public void setMaxBrvOverflow(int maxBrvOverflow) { this.maxBrvOverflow = maxBrvOverflow; }
	public int getMaxBrvOverflowBreak() { return maxBrvOverflowBreak; }
	public void setMaxBrvOverflowBreak(int maxBrvOverflowBreak) { this.maxBrvOverflowBreak = maxBrvOverflowBreak; }
	public int getSingleTargetBrvRate() { return singleTargetBrvRate; }
	public void setSingleTargetBrvRate(int singleTargetBrvRate) { this.singleTargetBrvRate = singleTargetBrvRate; }
	public int getBrvDamageLimitUp() { return brvDamageLimitUp; }
	public void setBrvDamageLimitUp(int brvDamageLimitUp) { this.brvDamageLimitUp = brvDamageLimitUp; }
	public int getMaxBrvLimitUp() { return maxBrvLimitUp; }
	public void setMaxBrvLimitUp(int maxBrvLimitUp) { this.maxBrvLimitUp = maxBrvLimitUp; }
	public List<Element> getElements() { return elements; }
	public String getManualDescription() { return manualDescription; }
	public void setManualDescription(String manualDescription) { this.manualDescription = manualDescription; }

	public String getDescription() {
		if(getManualDescription() != null)
			return getManualDescription();
		return getEffect().getDescription(this);
	}
}