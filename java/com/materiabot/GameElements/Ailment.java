package com.materiabot.GameElements;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import com.materiabot.GameElements.Enumerators.Ailment.ConditionBlock;
import com.materiabot.GameElements.Enumerators.Ailment.RankData;
import com.materiabot.GameElements.Enumerators.Ailment.TargetType;
import com.materiabot.GameElements.Enumerators.Ailment.Effect._AilmentEffect;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.ImageUtils;
import com.materiabot.Utils.MessageUtils;

public class Ailment { //TODO Missing icons	
	/* Regarding iconType and dispType
	 14 means not visible buff/debuff icon
	and then is disp_type is not 0 or -1, then it is a special effect
	else, it is a hidden ailment
	 */

	private int id, castId;
	private Text name, desc, fakeName, fakeDesc, fakeEmote;
	private int rate, rank, duration, maxStacks, buffType, iconType, dispType, spStacks, targetId;
	private Integer[] args, effects, valTypes, valEditTypes, valSpecify, rankTables, groupId, auraRankData; //auraRankData is for Fake Ailments for Auras
	private ConditionBlock[] conditions;
	private TargetType target;
	private boolean extendable, burstExtendable, framed;
	private Unit unit;
	private Ability ability;
	private HashMap<Integer, RankData> rankData = new HashMap<>();
	private List<Aura> auras = new LinkedList<>();

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

	public Unit getUnit() { return unit; }
	public void setUnit(Unit unit) { this.unit = unit; }

	public Ability getAbility() { return ability; }
	public void setAbility(Ability ability) { this.ability = ability; }

	public HashMap<Integer, RankData> getRankData() { return rankData; }

	public List<Aura> getAuras() { return auras; }
	public void setAuras(List<Aura> auras) { this.auras = auras; }

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
		return isSiphon && effectCount == 1 && !isVisible();
	}

	public boolean isDeadEffect() {
		int effectCount = 0;
		for(@SuppressWarnings("unused") int e : this.getEffects())
			effectCount++;
		if(effectCount > 0 && !generateEffectDescription().isEmpty())
			return false;
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null || !o.getClass().equals(Ailment.class)) return false;
		return this.getId() == ((Ailment)o).getId();
	}

	public String generateTitle() {
		if(this.getFakeName() != null) return getFakeName().getBest();
		if(isDeadEffect()) return "";
		String icon = ImageUtils.getAilmentEmote(this);
		if(!(icon.startsWith("<:specialAilment") || icon.contains("Invisible")))
			icon = (this.isGolden() ? ImageUtils.getEmoteText("ailmentGolden") : (this.isFramed() ? ImageUtils.getEmoteText("ailmentSilver") : "")) + icon;
		return icon + this.getName().getBest() + (this.getName().getBest() == null || Constants.DEBUG ? " (" + this.getId() + ")" : "") + System.lineSeparator() + (this.isStackable() ? "(" + this.getMaxStacks() + " max stacks)" + System.lineSeparator() : "");
	}

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
		if(isDeadEffect()) return "";
		if(!isAuraEffect) {
			if(this.isStackable() && this.getArgs()[0] > 0)
				ret += "+" + this.getArgs()[0] + (this.getArgs()[0] == 1 ? " stack to " : " stacks to ");
			if(this.getTarget() != null) 
				ret += ret.length() == 0 ? this.getTarget().getDescription() : this.getTarget().getDescription().toLowerCase();
			else 
				ret += "unknown target (" + this.getTargetId() + ")";
			if(this.getDuration() > 0)
				ret += " for " + this.getDuration() + (this.getDuration() == 1 ? " turn" : " turns");
			finalDescription.add(new AilmentBlock(null, ret));
		}
		if(!isExtendable())
			finalDescription.add(0, new AilmentBlock(null, "Cannot be extended"));
		if(!isBurstExtendable())
			finalDescription.add(0, new AilmentBlock(null, "Decreases in BURST mode"));
		finalDescription.addAll(generateEffectDescription());
		return finalDescription.stream().distinct().sorted().map(ab -> ab.toString()).distinct().reduce((s1, s2) -> s1 + System.lineSeparator() + s2).orElse("");
	}
	private List<AilmentBlock> generateEffectDescription() {
		if(this.getFakeDesc() != null) return null;
		List<AilmentBlock> finalDescription = new LinkedList<>();
		boolean hasAuras = false;
		for(int i = 0; i < this.getEffects().length; i++) {
			if(this.getEffects()[i] == -1 || this.getEffects()[i] == 69) continue; //69 is a "meta" effect related to countering, other effects will use it.
			if(this.getEffects()[i] == 60) { hasAuras = true; continue; }
			String condi = "";
			for(int ic = 0; ic < this.getConditions()[i].getConditions().size(); ic++) {
				ConditionBlock cond = this.getConditions()[i].getConditions().get(ic);
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
			for(Aura a : this.getAuras()) { //I'm parsing the conditions here instead of in the Aura so that in the future I can merge effects that have the same conditions 
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