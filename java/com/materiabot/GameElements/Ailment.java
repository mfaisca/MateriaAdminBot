package com.materiabot.GameElements;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import com.materiabot.GameElements.Enumerators.Ailment.ConditionBlock;
import com.materiabot.GameElements.Enumerators.Ailment.RankData;
import com.materiabot.GameElements.Enumerators.Ailment.TargetType;
import com.materiabot.GameElements.Enumerators.Ailment.Effect._AilmentEffect;
import com.materiabot.Utils.Constants;
import Shared.BotException;

public class Ailment { //TODO Missing icons	
	/* Regarding iconType and dispType
	 14 means not visible buff/debuff icon
	and then is disp_type is not 0 or -1, then it is a special effect
	else, it is a hidden ailment
	 */
	
	private int id, castId;
	private Text name, desc, fakeName, fakeDesc, fakeEmote;
	private int rate, rank, duration, maxStacks, buffType, iconType, dispType, spStacks, targetId;
	private Integer[] args, effects, valTypes, valEditTypes, valSpecify, rankTables, groupId;
	private ConditionBlock[] conditions;
	private TargetType target;
	private boolean extendable, framed, golden;
	private Unit unit;
	private Ability ability;
	private HashMap<Integer, RankData> rankData = new HashMap<Integer, RankData>();
	private List<Aura> auras = new LinkedList<Aura>();

	public Ailment() {}
	public Ailment(String text) { setName(new Text(text)); setFakeDesc(new Text(text)); }
	
	public boolean isStackable() {
		return getMaxStacks() > 0; //TODO - double check with rem if this is enough
	}
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public int getCastId() { return castId; }
	public void setCastId(int castId) { this.castId = castId; }

	public Text getName() { return name; }
	public void setName(Text name) { this.name = name; }

	public Text getDesc() { return desc; }
	public void setDesc(Text desc) { this.desc = desc; }

	public Text getFakeName() { return fakeName; }
	public void setFakeName(Text fakeName) { this.fakeName = fakeName; }

	public Text getFakeDesc() { return fakeDesc; }
	public void setFakeDesc(Text fakeDesc) { this.fakeDesc = fakeDesc; }

	public Text getFakeEmote() { return fakeEmote; }
	public void setFakeEmote(Text fakeEmote) { this.fakeEmote = fakeEmote; }

	public int getRate() { return rate; }
	public void setRate(int rate) { this.rate = rate; }

	public int getRank() { return rank; }
	public void setRank(int rank) { this.rank = rank; }

	public int getDuration() { return duration; }
	public void setDuration(int duration) { this.duration = duration; }

	public int getMaxStacks() { return maxStacks; }
	public void setMaxStacks(int maxStacks) { this.maxStacks = maxStacks; }

	public int getBuffType() { return buffType; }
	public void setBuffType(int buffType) { this.buffType = buffType; }

	public int getIconType() { return iconType; }
	public void setIconType(int iconType) { this.iconType = iconType; }

	public int getDispType() { return dispType; }
	public void setDispType(int dispType) { this.dispType = dispType; }

	public int getSpStacks() { return spStacks; }
	public void setSpStacks(int spStacks) { this.spStacks = spStacks; }

	public int getTargetId() { return targetId; }
	public void setTargetId(int targetId) { this.targetId = targetId; }

	public Integer[] getArgs() { return args; }
	public void setArgs(Integer[] args) { this.args = args; }

	public Integer[] getEffects() { return effects; }
	public void setEffects(Integer[] effects) { this.effects = effects; }

	public Integer[] getValTypes() { return valTypes; }
	public void setValTypes(Integer[] valTypes) { this.valTypes = valTypes; }

	public Integer[] getValEditTypes() { return valEditTypes; }
	public void setValEditTypes(Integer[] valEditTypes) { this.valEditTypes = valEditTypes; }

	public Integer[] getValSpecify() { return valSpecify; }
	public void setValSpecify(Integer[] valSpecify) { this.valSpecify = valSpecify; }

	public Integer[] getRankTables() { return rankTables; }
	public void setRankTables(Integer[] rankTables) { this.rankTables = rankTables; }

	public Integer[] getGroupId() { return groupId; }
	public void setGroupId(Integer[] groupId) { this.groupId = groupId; }
	
	public ConditionBlock[] getConditions() { return conditions; }
	public void setConditions(ConditionBlock[] conditions) { this.conditions = conditions; }

	public TargetType getTarget() { return target; }
	public void setTarget(TargetType target) { this.target = target; }

	public boolean isExtendable() { return extendable; }
	public void setExtendable(boolean extendable) { this.extendable = extendable; }

	public boolean isFramed() { return framed; }
	public void setFramed(boolean framed) { this.framed = framed; }

	public boolean isGolden() { return golden; }
	public void setGolden(boolean golden) { this.golden = golden; }

	public Unit getUnit() { return unit; }
	public void setUnit(Unit unit) { this.unit = unit; }

	public Ability getAbility() { return ability; }
	public void setAbility(Ability ability) { this.ability = ability; }

	public HashMap<Integer, RankData> getRankData() { return rankData; }

	public List<Aura> getAuras() { return auras; }
	public void setAuras(List<Aura> auras) { this.auras = auras; }
	
	public String generateDescription() throws BotException {
		if(this.getFakeDesc() != null) return getFakeDesc().getBest();
		String ret = "";
		//DEBUG
		ret += this.getName().getBest() + " (" + this.getId() + ")" + System.lineSeparator() + (this.isStackable() ? "(" + this.getMaxStacks() + " max stacks)" : "");
		//DEBUG

		if(this.isStackable())
			ret += "+" + this.getArgs()[0] + (this.getArgs()[0] == 1 ? " stack to " : "stacks to ");
		if(this.getTarget() != null) 
			ret += this.isStackable() ? this.getTarget().getDescription() : this.getTarget().getDescription();
		else 
			ret += "unknown target (" + this.getTargetId() + ")";
		if(this.getDuration() > 0)
			ret += " for " + this.getDuration() + (this.getDuration() == 1 ? " turn" : " turns") + System.lineSeparator();
		boolean hasAuras = false;
		for(int i = 0; i < this.getEffects().length; i++) {
			if(this.getEffects()[i] == -1) continue;
			if(this.getEffects()[i] == 60) { hasAuras = true; continue; }
			_AilmentEffect effect = Constants.AILMENT_EFFECT.get(this.getEffects()[i]);
			if(effect == null)
				ret += "Unknown Effect " + this.getEffects()[i];
			else {
				String condi = "";
				if(this.getConditions()[i].getConditions().size() > 0){
					ConditionBlock cond = this.getConditions()[i].getConditions().get(0);
					condi += cond.getCondition().getDescription(cond) + System.lineSeparator();
				}
				String effStr = effect.getDescription(this, i);
				if(effStr == null)
					throw new BotException("Ailment Effect does not match ID");
				ret += (condi.length() > 0 ? condi + "\t" : "") + effStr;
			}
			ret += System.lineSeparator();
		}
		if(hasAuras) {
			for(Aura a : this.getAuras()) {
				String condi = "";
				for(int i = 0; i < a.getRequiredConditions().length; i++) {
					if(a.getRequiredConditions()[i] == null) {
						if(a.getRequiredConditionsIds()[i] == -1)
							continue;
						else
							condi += "Unknown Aura Condition: " + a.getRequiredConditionsIds()[i] + System.lineSeparator();
					}
					if(a.getRequiredConditions()[i] != null)
						condi += a.getRequiredConditions()[i].getDescription(a, i) + System.lineSeparator();
				}
				if(a.getEffect() == null)
					ret += (condi.length() > 0 ? condi + "\t" : "") + "Unknown Aura Effect: " + a.getEffectId() + "/" + a.getTypeId();
				else
					ret += (condi.length() > 0 ? condi + "\t" : "") + a.getEffect().getDescription(a);
				ret += System.lineSeparator();
			}
		}
		return ret.trim();
	}
	public static final Ailment NULL(int id) {
		return new Ailment("Unknown Ailment " + id) {};
	}
}