package com.materiabot.GameElements;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import com.materiabot.GameElements.Enumerators.Ailment.ConditionBlock;
import com.materiabot.GameElements.Enumerators.Ailment.RankData;
import com.materiabot.GameElements.Enumerators.Ailment.TargetType;
import com.materiabot.GameElements.Enumerators.Ailment.Effect._AilmentEffect;
import com.materiabot.GameElements.Enumerators.Ailment.Required._AilmentRequired;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.ImageUtils;
import com.materiabot.Utils.MessageUtils;
import Shared.Methods;

public class Ailment {
	private int id, castId;
	private Text name, desc, fakeName, fakeDesc, fakeEmote;
	private int rate, rank, duration, maxStacks, buffType, iconType, dispType, spStacks, targetId, 
				hitOrder; //-1 = start | 0 = end | otherwise after said hit
	private Integer[] args, effects, valTypes, valEditTypes, valSpecify, rankTables, groupId, auraRankData; //auraRankData is for Fake Ailments for Auras
	private ConditionBlock[] conditions;
	private TargetType target;
	private boolean extendable, burstExtendable, framed, defaultAilment, triggeredAilment;
	private Unit unit;
	private Ability ability;
	private HashMap<Integer, RankData> rankData = new HashMap<>();
	private List<Aura> auras = new LinkedList<>();
	private _AilmentRequired ailmentCondition;
	private int ailmentConditionId;
	private int ailmentConditionValue;
	private ConditionBlock[] ailmentConditionBlocks; //When ailmentRequired = 26
	

	public Ailment() {}
	public Ailment(String text) { setName(new Text(text)); setFakeDesc(new Text(text)); }

	public boolean isStackable() { return getMaxStacks() > 0; }

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public int getCastId() { return castId; }
	public void setCastId(int castId) { this.castId = castId; }

	public Text getName() { 
		String name = this.name.getBest();
		if(name == null || name.length() == 0) {
			name = this.getDesc().getBest();
			if(name.contains(":"))
				return new Text(name.substring(0, name.indexOf(":")).trim());
			if(name.contains("from"))
				return new Text(name.substring(name.indexOf("from")+5).trim());
		}
		return this.name;
	}
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

	public Integer[] getAuraRankData() { return auraRankData; }
	public void setAuraRankData(Integer[] auraRankData) { this.auraRankData = auraRankData; }

	public ConditionBlock[] getConditions() { return conditions; }
	public void setConditions(ConditionBlock[] conditions) { this.conditions = conditions; }

	public TargetType getTarget() { return target; }
	public void setTarget(TargetType target) { this.target = target; }

	public boolean isExtendable() { return extendable; }
	public void setExtendable(boolean extendable) { this.extendable = extendable; }

	public boolean isBurstExtendable() { return burstExtendable; }
	public void setBurstExtendable(boolean burstExtendable) { this.burstExtendable = burstExtendable; }

	public boolean isFramed() { return framed; }
	public void setFramed(boolean framed) { this.framed = framed; }
	public boolean isGolden() { return Arrays.asList(this.getEffects()).stream().anyMatch(i -> i.intValue() == 413); }

	public boolean isDefault() { return defaultAilment; }
	public void setDefault(boolean defaultAilment) { this.defaultAilment = defaultAilment; }
	public boolean isTriggered() { return triggeredAilment; }
	public void setTriggered(boolean triggeredAilment) { this.triggeredAilment = triggeredAilment; }
	public Unit getUnit() { return unit; }
	public void setUnit(Unit unit) { this.unit = unit; }

	public Ability getAbility() { return ability; }
	public void setAbility(Ability ability) { this.ability = ability; }

	public HashMap<Integer, RankData> getRankData() { return rankData; }

	public List<Aura> getAuras() { return auras; }
	public void setAuras(List<Aura> auras) { this.auras = auras; }

	public int getHitOrder() { return hitOrder; }
	public void setHitOrder(int hitOrder) { this.hitOrder = hitOrder; }
	
	public _AilmentRequired getAilmentCondition() { return ailmentCondition; }
	public void setAilmentCondition(_AilmentRequired ailmentCondition) { this.ailmentCondition = ailmentCondition; }
	public int getAilmentConditionId() { return ailmentConditionId; }
	public void setAilmentConditionId(int ailmentConditionId) { this.ailmentConditionId = ailmentConditionId; }
	public int getAilmentConditionValue() { return ailmentConditionValue; }
	public void setAilmentConditionValue(int ailmentConditionValue) { this.ailmentConditionValue = ailmentConditionValue; }
	public ConditionBlock[] getAilmentConditionBlock() { return ailmentConditionBlocks; }
	public void setAilmentConditionBlock(ConditionBlock[] conditionBlocks) { this.ailmentConditionBlocks = conditionBlocks; }
	public List<Integer> getTriggeredAbilities() {
		List<Integer> ret = new LinkedList<>();		
		if(this.getName().getBest().equals("Attack Change")) {
			List<Integer> enableds = this.getUnit().getUpgradedAbilities().stream()
										.flatMap(ca -> ca.getReqMiscConditions().stream())
										.filter(mc -> Arrays.asList(mc.getValues()).stream().anyMatch(v -> v.intValue() == this.getId()))
										.map(mc -> mc.getAb().getSecondaryId())
										.collect(Collectors.toList());
			ret.addAll(enableds);
		}
		for(int i = 0; i < this.getEffects().length; i++) {
			if(this.getEffects()[i] == -1 || this.getEffects()[i] == 60) 
				continue;
			_AilmentEffect effect = Constants.AILMENT_EFFECT.get(this.getEffects()[i]);
			if(effect != null)
				ret.addAll(effect.getTriggeredAbilities(this, i, getRank(), false));
		}
		return Stream.concat(ret.stream(), 
				this.getAuras().stream()
					.filter(a -> a.getEffectId() != -1)
					.flatMap(a -> _AilmentEffect.getTriggeredAbilitiesFromAura(a).stream())
					.distinct()).collect(Collectors.toList());
	}
	public List<Integer> getTriggeredAilments() {
		List<Integer> ret = new LinkedList<>();
		for(int i = 0; i < this.getEffects().length; i++) {
			if(this.getEffects()[i] == -1 || this.getEffects()[i] == 60) 
				continue;
			_AilmentEffect effect = Constants.AILMENT_EFFECT.get(this.getEffects()[i]);
			if(effect != null)
				ret.addAll(effect.getTriggeredAilments(this, i, getRank(), false));
		}
		return Stream.concat(ret.stream(), 
				this.getAuras().stream()
					.filter(a -> a.getEffectId() != -1)
					.flatMap(a -> _AilmentEffect.getTriggeredAilmentsFromAura(a).stream())
					.distinct()).collect(Collectors.toList());
	}
	
	public boolean isVisible() {
		return getIconType() != 14;
	}
	public boolean isSpecial() {
		return !isVisible() && getDispType() > 0;
	}

	public boolean isInvisibleSiphon() {
		boolean isSiphon = false;
		int effectCount = 0;
		for(int i = 0; i < getEffects().length; i++) {
			if(getEffects()[i] != -1) effectCount++;
			if(getEffects()[i] == 44) isSiphon = true;
		}
		return isSiphon && effectCount == 1 && !isVisible() && this.getDuration() == 1;
	}

	public boolean isDeadEffect() {
		int effectCount = 0;;
		if(this.getEffects() != null)
			for(@SuppressWarnings("unused") int e : this.getEffects())
				effectCount++;
		if(effectCount > 0 && !generateEffectDescription().isEmpty())
			return false;
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null || !this.getClass().equals(o.getClass())) return false;
		Ailment other = (Ailment)o;
		if(this.getId() != other.getId()) return false;
		return this.generateDescription().equals(other.generateDescription());
	}

	public String generateTitle() {
		return generateTitle(true);
	}
	
	public String generateTitle(boolean showFrame) {
		if(this.getFakeName() != null) return getFakeName().getBest();
		if(this.getName().getBest().equals("Attack Change")) ;
		else if(isDeadEffect()) return "";
		String icon = ImageUtils.getAilmentEmote(this);
		if(showFrame && !(icon.startsWith("<:specialAilment") || icon.contains("Invisible")))
			icon = (this.isGolden() ? ImageUtils.getEmoteText("ailmentGolden") : (this.isFramed() ? ImageUtils.getEmoteText("ailmentSilver") : "")) + icon;
		return icon + this.getName().getBest() + (this.getName().getBest() == null || Constants.DEBUG ? " (" + this.getId() + ")" : "") + System.lineSeparator() + (this.isStackable() ? "(" + this.getMaxStacks() + " max stacks)" + System.lineSeparator() : "");
	}

	/*public String generateInLineDescription() {
		String ret = "Apply " + ImageUtils.getAilmentEmote(this) + Methods.enframe(this.getName().getBest()) + "to " + this.getTarget().getDescription();
		if(this.getDuration() > 0)
			ret += " for " + this.getDuration() + (this.getDuration() == 1 ? " turn" : " turns");
		return ret;
	}*/
	public String generateDescription() {
		return generateDescription(false);
	}

	private static final class AilmentBlock implements Comparable<AilmentBlock>{
		public String condi;
		public List<String> desc = new LinkedList<>();

		public AilmentBlock(String c, String... desc) {
			condi = c; add(desc);
		}
		public void add(String... desc) {
			this.desc.addAll(Arrays.asList(desc));
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || this.getClass() != obj.getClass())
				return false;
			AilmentBlock other = (AilmentBlock)obj;
			if(this.condi == null)
				return other.condi == null;
			return this.condi.equals(other.condi);
		}

		@Override
		public String toString() {
			return (condi != null && condi.length() > 0 ? condi + System.lineSeparator() : "") + 
					desc.stream()
						.map(d -> (condi != null && condi.length() > 0 ? MessageUtils.tab() : "") + d)
						.distinct()
						.reduce((d1, d2) -> d1 + System.lineSeparator() + d2).orElse("Error Parsing Ailment Block");
		}
		@Override
		public int compareTo(AilmentBlock other) {
			if(this.condi == null) return -1;
			if(other.condi == null) return 1;
			return this.condi.length() - other.condi.length();
		}
	}
	
	public String generateDescription(boolean isAuraEffect) {
		if(this.getFakeDesc() != null) return getFakeDesc().getBest();
		List<AilmentBlock> finalDescription = new LinkedList<>();
		String ret = "";
		if(isDeadEffect() && !this.getName().getBest().equals("Attack Change"))
			return "";;
		if(!isAuraEffect) {
			if(this.isStackable() && this.getArgs().length > 0 && this.getArgs()[0] > 0)
				ret += "+" + this.getArgs()[0] + (this.getArgs()[0] == 1 ? " stack to " : " stacks to ");
			if(this.getTarget() != null) 
				ret += ret.length() == 0 ? this.getTarget().getDescription() : this.getTarget().getDescription().toLowerCase();
			else 
				ret += "unknown target (" + this.getTargetId() + ")";
			if(this.getDuration() > 0)
				ret += " for " + this.getDuration() + (this.getDuration() == 1 ? " turn" : " turns");
			finalDescription.add(new AilmentBlock(null, ret));
		}
		if(this.getAilmentConditionId() > 0) {
			String condi = (this.getAilmentCondition() != null ? this.getAilmentCondition().getDescription(this, 0) : ("Unknown Condition " + this.getAilmentConditionId()));
			if(!condi.isBlank())
				finalDescription.add(new AilmentBlock(null, "Applied when:" + System.lineSeparator() + MessageUtils.tab() + condi));
		}
		if(this.getRate() < 100)
			finalDescription.add(new AilmentBlock(null, this.getRate() + "% chance to apply"));
		if(!isExtendable())
			finalDescription.add(0, new AilmentBlock(null, "Cannot be extended"));
		if(!isBurstExtendable())
			finalDescription.add(0, new AilmentBlock(null, "Decreases in BURST mode"));
		if(this.getName().getBest().equals("Attack Change")) {
			String enabledAbilities = this.getUnit().getUpgradedAbilities().stream()
										.flatMap(ca -> ca.getReqMiscConditions().stream())
										.filter(mc -> Arrays.asList(mc.getValues()).stream().anyMatch(v -> v.intValue() == this.getId()))
										.map(mc -> this.getUnit().getSpecificAbility(mc.getAb().getSecondaryId()))
										.map(a -> Methods.enframe(a.getName().getBest())).distinct()
										.reduce((n1, n2) -> n1 + "," + n2).orElse("special abilities");
			String ret2 = "Enables " + enabledAbilities + (this.getDuration() < 1 ? " for 1 use" : "");
			finalDescription.add(0, new AilmentBlock(null, ret2));
		}
		finalDescription.addAll(generateEffectDescription());
		return finalDescription.stream().distinct().sorted()
				.map(ab -> ab.toString()).distinct()
				.reduce((s1, s2) -> s1 + System.lineSeparator() + s2)
				.orElse("").replace(System.lineSeparator() + "{retline}", "");
	}
	private List<AilmentBlock> generateEffectDescription() {
		if(this.getFakeDesc() != null) return null;
		List<AilmentBlock> finalDescription = new LinkedList<>();
		boolean hasAuras = Arrays.asList(this.getEffects()).stream().anyMatch(i -> i == 60);
		if(this.getRank() >= 0)
			for(int i = 0; i < this.getEffects().length; i++) {
				if(this.getEffects()[i] == -1 || this.getEffects()[i] == 69 || this.getEffects()[i] == 60) continue; //69 is a "meta" effect related to countering, other effects will use it.
				String condi = "";
				for(int ic = 0; ic < this.getConditions()[i].getConditions().size(); ic++) {
					ConditionBlock cond = this.getConditions()[i].getConditions().get(ic);;
					if(cond.getCondition() != null)
						condi += cond.getCondition().getDescription(cond) + System.lineSeparator();
					else
						condi += "Unknown Ailment Condition: " + cond.getConditionId() + System.lineSeparator();
				}
				condi = condi.trim(); //Remove the last lineSeparator
				_AilmentEffect effect = Constants.AILMENT_EFFECT.get(this.getEffects()[i]);
				String effStr;
				if(effect == null)
					effStr = "Unknown Effect " + this.getEffects()[i];
				else {
					effStr = effect.getDescription(this, i, getRank(), false);
					if(effStr == null)
						effStr = "Error parsing Ailment " +  + this.getEffects()[i];
				}
				if(effStr.length() > 0) {
					AilmentBlock ab = new AilmentBlock(condi, effStr);
					if(finalDescription.contains(ab))
						finalDescription.get(finalDescription.indexOf(ab)).add(effStr);
					else
						finalDescription.add(ab);
				}
			}
		if(hasAuras) {
			for(Aura a : this.getAuras()) { //I'm parsing the conditions here instead of in the Aura so that I can merge effects that have the same conditions 
				String condi = "";
				for(int i = 0; i < a.getRequiredConditions().length; i++) {
					if(a.getRequiredConditions()[i] != null) {
						String cond = a.getRequiredConditions()[i].getDescription(a, i);
						if(cond.length() > 0)
							condi += cond + System.lineSeparator();
					}
					else {
						if(a.getRequiredConditionsIds()[i] != -1)
							condi += "Unknown Aura Condition: " + a.getRequiredConditionsIds()[i] + System.lineSeparator();
					}
				}
				condi = condi.trim();
				String auraDesc = a.getDescription();
				if(auraDesc == null)
					auraDesc = "Error parsing Aura " + a.getId() + " on " + a.getAilment().getUnit().getName() + "/" + a.getAilment().getId();
				if(auraDesc.length() > 0) {
					AilmentBlock ab = new AilmentBlock(condi, auraDesc);
					if(finalDescription.contains(ab))
						finalDescription.get(finalDescription.indexOf(ab)).add(auraDesc);
					else
						finalDescription.add(ab);
				}
			}
		}
//		if(finalDescription.size() == finalCountBeforeEffects) //If the ailment doesn't have any effect, hide it
//			return "";
		return finalDescription;
	}
	
	public static final Ailment NULL(int id) {
		return new Ailment("Unknown Ailment " + id) {};
	}
}