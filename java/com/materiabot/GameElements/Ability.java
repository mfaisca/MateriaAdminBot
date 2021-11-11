package com.materiabot.GameElements;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import com.google.common.collect.Streams;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
import com.materiabot.GameElements.Enumerators.Ability.AttackType;
import com.materiabot.GameElements.Enumerators.Ability.ChargeRate;
import com.materiabot.GameElements.Enumerators.Ability.CommandType;
import com.materiabot.GameElements.Enumerators.Ability.HitType;
import com.materiabot.GameElements.Enumerators.Ability.TargetRange;
import com.materiabot.GameElements.Enumerators.Ability.TargetType;
import com.materiabot.GameElements.Enumerators.Ability.HitData.BasedOnStat;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Target;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Type;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Effect._AbilityEffect.TAG;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.Constants.DupeMerger;
import com.materiabot.Utils.ImageUtils;
import Shared.Methods;

public class Ability implements Comparable<Ability>{
	public static class MultiAbility extends Ability{
		private LinkedList<Ability> merged = new LinkedList<>();
		
		public MultiAbility(Ability... abilities) {
			try {
				BeanUtils.copyProperties(this, abilities[0]);
			} catch (Exception e) {
				this.setName(new Text("Error"));
			}
			merged.addAll(Arrays.asList(abilities));
		}

		@Override
		public List<HitData> getHitData(){
			return merged.stream().flatMap(a -> a.getHitData().stream()).collect(Collectors.toList());
		}
		@Override
		public List<Ailment> getAilments(){
			return merged.stream().flatMap(a -> a.getAilments().stream()).distinct().collect(Collectors.toList());
		}
	}
	
	private int id = -1;
	private Text name;
	private Text desc;
	private String manualDesc;
	private int rank;
	private int movementCost;
	private int baseUseCount;
	private boolean canLaunch;
	private int chaseDmg;
	private int hitTypeId;
	private HitType hitType; //Type_data.type
	private int attackTypeId;
	private AttackType attackType;
	private AttackName attackName; //My own data structure
	private int commandTypeId;
	private Integer[] groupId;
	private CommandType commandType;
	private int targetTypeId;
	private TargetType targetType;
	private int targetRangeId;
	private TargetRange targetRange;
	private List<HitData> hitData = new LinkedList<>();
	private List<Ailment> ailments = new LinkedList<>();
	private int[] arguments; //Unknown use
	private Unit unit;
	private String icon; //To be used on skill command menu
	private boolean main = false;
	
	public Ability() {}
	public Ability(String text) { setName(new Text(text)); setManualDesc(text); }
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public Text getName() { return name; }
	public void setName(Text name) { this.name = name; }
	public Text getDesc() { return desc; }
	public void setDesc(Text desc) { this.desc = desc; }
	public String getManualDesc() { return manualDesc; }
	public void setManualDesc(String manualDesc) { this.manualDesc = manualDesc; }
	public int getRank() { return rank; }
	public void setRank(int rank) { this.rank = rank; }
	public int getMovementCost() { return movementCost; }
	public void setMovementCost(int movementCost) { this.movementCost = movementCost; }
	public int getBaseUseCount() { return baseUseCount; }
	public void setBaseUseCount(int useCount) { this.baseUseCount = useCount; }
	public boolean isCanLaunch() { return canLaunch; }
	public void setCanLaunch(boolean canLaunch) { this.canLaunch = canLaunch; }
	public int getChaseDmg() { return chaseDmg; }
	public void setChaseDmg(int chaseDmg) { this.chaseDmg = chaseDmg; }
	public HitType getHitType() { return hitType; }
	public void setHitType(HitType hitType) { this.hitType = hitType; }
	public AttackType getAttackType() { return attackType; }
	public void setAttackType(AttackType attackType) { this.attackType = attackType; }
	public CommandType getCommandType() { return commandType; }
	public void setCommandType(CommandType commandType) { this.commandType = commandType; }
	public TargetType getTargetType() { return targetType; }
	public void setTargetType(TargetType targetType) { this.targetType = targetType; }
	public TargetRange getTargetRange() { return targetRange; }
	public void setTargetRange(TargetRange targetRange) { this.targetRange = targetRange; }
	public List<HitData> getHitData() { return hitData; }
	public List<Ailment> getAilments() { return ailments; }
	public int[] getArguments() { return arguments; }
	public void setArguments(int[] arguments) { this.arguments = arguments; }
	public int getHitTypeId() { return hitTypeId; }
	public void setHitTypeId(int hitTypeId) { this.hitTypeId = hitTypeId; }
	public int getAttackTypeId() { return attackTypeId; }
	public void setAttackTypeId(int attackTypeId) { this.attackTypeId = attackTypeId; }
	public AttackName getAttackName() { return attackName; }
	public void setAttackName(AttackName attackName) { if(this.getAttackName() == null) this.attackName = attackName; }
	public int getCommandTypeId() { return commandTypeId; }
	public void setCommandTypeId(int commandTypeId) { this.commandTypeId = commandTypeId; }
	public Integer[] getGroupId() { return groupId; }
	public void setGroupId(Integer[] groupId) { this.groupId = groupId; }
	public int getTargetTypeId() { return targetTypeId; }
	public void setTargetTypeId(int targetTypeId) { this.targetTypeId = targetTypeId; }
	public int getTargetRangeId() { return targetRangeId; }
	public void setTargetRangeId(int targetRangeId) { this.targetRangeId = targetRangeId; }
	public Unit getUnit() { 
		return unit;
	}
	public void setUnit(Unit unit) { this.unit = unit; }
	public String getIcon() { return icon; }
	public void setIcon(String icon) { this.icon = icon; }
	public boolean isMain() { return main; }
	public void setMain(boolean main) { this.main = main; }
	public boolean isFollowup() {
		return this.getUnit().getTriggeredAbilities().stream().anyMatch(ta -> ta.getSecondaryId() == this.getId());
	}
	
	public String getSelectionMenuText() {
		if(AttackName.BT.equals(this.getAttackName())) {
			if(this.getUnit().getBaseAbility(AttackName.BT).get(0).getId() == this.getId())
				return getName().getBest() + " (BT Only)";
			else
				return getName().getBest() + " (BT+ 2/3)";
		}else
			return getName().getBest(); 
	}
	public int getTotalUseCount() {
		Ability base2 = null;
		if(getUnit().getBaseAbility(this.getAttackName()) == null)
			base2 = this;
		else
			base2 = getUnit().getBaseAbility(this.getAttackName()).get(0);
		Ability base = base2;
		int useCount = base.getBaseUseCount();
		useCount += Streams.concat(	getUnit().getEquipment().stream().flatMap(e -> e.getPassives().stream()),
									getUnit().getPassives().values().stream(),
									getUnit().getCharaBoards().stream())
					.filter(Methods.distinctByKey(p -> p.getId()))
					.flatMap(p -> p.getEffects().stream())
					.filter(e -> e.getEffectId() == 22)
					.filter(e -> e.getValues()[0] == base.getId())
					.map(e -> e.getValues()[1])
					.reduce(0, (v1, v2) -> v1 + v2);
		return useCount;
	}
	public int getChargeRate() {
		Ability base = getUnit().getBaseAbility(this.getAttackName()).get(0);
		int chargeRate = base.getBaseUseCount();
		float mult = Streams.concat(	getUnit().getEquipment().stream().flatMap(e -> e.getPassives().stream()),
											getUnit().getPassives().values().stream(),
											getUnit().getCharaBoards().stream())
							.filter(Methods.distinctByKey(p -> p.getId()))
							.flatMap(p -> p.getEffects().stream())
							.filter(e -> e.getEffectId() == 107)
							.filter(e -> e.getValues()[1] == base.getId())
							.map(e -> e.getValues()[0])
							.reduce(100, (v1, v2) -> v1 - v2).intValue();
		return (int)(chargeRate * (mult/100));
	}
	
	public String generateTitle() {
		return getHitData().stream().map(hd -> hd.getAttackType()).filter(hd -> hd != null).map(hd -> ImageUtils.getEmoteText(hd.getEmote())).distinct().reduce("", (s1, s2) -> s1 + s2)
				+ getHitData().stream().flatMap(hd -> hd.getElements().stream()).map(e -> ImageUtils.getEmoteText(e.getEmote())).distinct().reduce("", (s1, s2) -> s1 + s2)
				+ getName().getBest()
				+ (getTotalUseCount() > 0 && !AttackName.EX.equals(this.getAttackName()) && !AttackName.BT.equals(this.getAttackName()) ? " (Uses: " + getTotalUseCount() + ")" : "")
				+ (AttackName.EX.equals(this.getAttackName()) ? " (Charge Rate: " + ChargeRate.getBy(getChargeRate()).getDescription().getBest() + " (" + getChargeRate() + "))" : "")
				+ (Constants.DEBUG ? " (ID: " + getId() + ")" : "");
	}

	public String generateDescription() {
		if(getManualDesc() != null)
			return getManualDesc();
		int stBrvIncrease = this.getHitData().stream()
				.map(h -> (int)Math.floor((h.getSingleTargetBrvRate() / h.getBrvRate()) * 100))
				.filter(o -> o > 0)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(0);
		int overflow = this.getCommandType() == null || this.getCommandType().equals(CommandType.BT) ? 100 : 
			this.getHitData().stream()
				.map(h -> h.getMaxBrvOverflow())
				.filter(o -> o > 100)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(100);
		if(overflow <= 100)
			overflow = this.getHitData().stream().filter(hd -> hd.getEffectId() == 106).map(hd -> hd.getArguments()[0]).findFirst().orElse(100);
		int breakOverflow = this.getHitData().stream()
				.map(h -> h.getMaxBrvOverflowBreak())
				.filter(o -> o > 0)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(0);
		int brvDamageLimit = this.getHitData().stream()
				.map(h -> h.getBrvDamageLimitUp())
				.filter(o -> o > 0)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(0);
		int maxBrvLimit = this.getHitData().stream()
				.map(h -> h.getMaxBrvLimitUp())
				.filter(o -> o > 0)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(0);
		List<String> preEffects = new LinkedList<>();
		List<String> effectList = new LinkedList<>();
		List<String> postEffects = new LinkedList<>();
		List<List<HitData>> effects = new LinkedList<>();
		List<HitData> currentChain = new LinkedList<>();
		
		List<Element> sameElements = null;
		com.materiabot.GameElements.Enumerators.Ability.HitData.AttackType sameAttackTypes = null;
		boolean sameElement = true;
		boolean sameAttackType = true;
		
		for(HitData hd : this.getHitData()) {
			if(hd.getManualDescription() != null) {
				effectList.add(hd.getManualDescription());
				continue;
			}
			hd.getDescription(); //To apply any fixes that might be needed to other stuff
			if(hd.getEffect().isPreEffect(hd.getEffectValueType()) && !preEffects.contains(hd.getDescription()))
				preEffects.add((hd.getEffect().getTags().contains(TAG.MERGE) ? "{retLine} " : "") + hd.getDescription());
			else if(hd.getEffect().isPostEffect(hd.getEffectValueType()) && !postEffects.contains(hd.getDescription()))
				postEffects.add((hd.getEffect().getTags().contains(TAG.MERGE) ? "{retLine} " : "") + hd.getDescription());
			if(hd.getEffect().isBRV() && Type.isBRV(hd.getType()) && (currentChain.isEmpty() || currentChain.get(0).getEffect().isBRV())) {
				currentChain.add(hd);
				if(sameElements == null)
					sameElements = hd.getElements();
				if(sameAttackTypes == null)
					sameAttackTypes = hd.getAttackType();
				sameElement = sameElement && sameElements.size() == hd.getElements().size() && sameElements.containsAll(hd.getElements()) && hd.getElements().containsAll(sameElements);
				sameAttackType = sameAttackType && sameAttackTypes.getId() == hd.getAttackType().getId();
			}else if(hd.getEffect().isHP() && Type.isHP(hd.getType()) && (currentChain.isEmpty() || currentChain.get(0).getEffect().isBRV())) {
				currentChain.add(hd);
				effects.add(currentChain);
				currentChain = new LinkedList<>();
			}
			else if(!hd.getEffect().isPostEffect(hd.getEffectValueType()) && !hd.getEffect().isPreEffect(hd.getEffectValueType())){
				if(!currentChain.isEmpty()) {
					effects.add(currentChain);
					currentChain = new LinkedList<>();
				}
				currentChain.add(hd);
				effects.add(currentChain);
				currentChain = new LinkedList<>();
			}
		}
		StringBuilder brvPotency = new StringBuilder();
		int totalPotency = 0;
		if(!currentChain.isEmpty())
			effects.add(currentChain);
		if(this.getMovementCost() == 0)
			preEffects.add(0, "Instant Turn Rate (" + this.getMovementCost() + ")");
		else if(this.getMovementCost() < 30)
			preEffects.add(0, "High Turn Rate (" + this.getMovementCost() + ")");
		else if(this.getMovementCost() > 30)
			preEffects.add(0, "Low Turn Rate (" + this.getMovementCost() + ")");
		if(chaseDmg == 50)
			postEffects.add("Initiates a chase sequence (" + chaseDmg + " [CU](https://www.reddit.com/r/DissidiaFFOO/comments/7x7ffp/chase_mechanic/))");
		else if(chaseDmg > 6)
			postEffects.add("Easier to initiate a chase sequence (" + chaseDmg + " [CU](https://www.reddit.com/r/DissidiaFFOO/comments/7x7ffp/chase_mechanic/))");
		else if(chaseDmg == 0)
			preEffects.add("Cannot initiate a chase sequence");
		if(stBrvIncrease > 0)
			preEffects.add(0, "Raises BRV Damage by " + stBrvIncrease + "% against ST");
		if(brvDamageLimit > 0)
			preEffects.add("+" + brvDamageLimit + "% Maximum BRV damage limit (up to " + Math.round(9999 * (1 + brvDamageLimit / 100f)) + ")");
		if(maxBrvLimit > 0)
			preEffects.add("+" + maxBrvLimit + "% Maximum obtainable BRV & HP damage limit (up to " + Math.round(99999 * (1 + maxBrvLimit / 100f)) + ")");
		for(List<HitData> chain : effects) {
			if(chain.isEmpty()) continue;
			if(chain.get(0).getEffect() == null)
				;
			if(chain.get(0).getEffect().isBRV() && Type.isBRV(chain.get(0).getType())) {
				HitData.Extra help = new HitData.Extra(chain.get(0));
				help.showElements = !sameElement;
				help.showType = !sameAttackType;
				for(HitData hd : chain) {
					if(hd.getEffect().isBRV() && Type.isBRV(hd.getType()))
						help.count++;
					else if(hd.getEffect().isHP() && Type.isHP(hd.getType())) {
						help.hp = new HitData.Extra(hd);
						help.showElements = !sameElement;
						help.showType = !sameAttackType;
					}
				}
				effectList.add(help.getHitsDescription());
				brvPotency.append(" + " + Math.round(chain.get(0).getBrvRate()) + "%");
				if(help.count > 1)
					brvPotency.append(" x " + help.count);
				totalPotency += Math.round(chain.get(0).getBrvRate() * help.count);
			} else if(chain.get(0).getEffect().isHP() && Type.isHP(chain.get(0).getType()))
				effectList.add("Followed by an HP Attack");
			else if(Constants.EFFECTS_THAT_CAN_REFUND.contains(chain.get(0).getEffectId()) && BasedOnStat.STATS_THAT_CAN_REFUND.contains(chain.get(0).getEffectValueType()) && chain.get(0).getTarget() == Target.Self) {
				effectList.add("{retLine} (" + chain.get(0).getArguments()[0] + "% refund)");
			}
			else {
				String text = (chain.get(0).getEffect().getTags().contains(TAG.MERGE) ? "{retLine} " : "") + chain.get(0).getDescription();
				if(!(chain.get(0).getEffect().blockRepeats() && effectList.contains(text)))
					effectList.add(text);
			}
		}
		for(Ailment a : this.getAilments()) {
			if(a.getHitOrder() == -1)
				preEffects.add("");
			else if(a.getHitOrder() == 0)
				postEffects.add("");
		}
		effectList.addAll(0, preEffects);
		effectList.addAll(postEffects);
		if(brvPotency.length() > 0) {
			brvPotency.append(" = " + totalPotency + "%");
			if(overflow > 100 && overflow < 5000)
				brvPotency.append(" (" + overflow + "% overflow");
			if(breakOverflow > 0 && breakOverflow < 5000)
				brvPotency.append(", " + (overflow + breakOverflow) + "% if target broken");
			if((overflow > 100 && overflow < 5000) || (breakOverflow > 0 && breakOverflow < 5000))
				brvPotency.append(")");
			effectList.add("{newLine}BRV Potency: " + brvPotency.substring(2).trim());
		}
		List<DupeMerger> as = new ArrayList<>(Arrays.asList(effectList.stream().filter(s -> !s.isEmpty())
					.reduce((s1, s2) -> s1 + System.lineSeparator() + s2).orElse("").replace(System.lineSeparator() + "{retLine}", "")
					.split(System.lineSeparator())).stream().map(s -> new DupeMerger(s)).collect(Collectors.toList()));
		for(int i = 1; i < as.size(); i++)
			as.get(i-1).merge(as.get(i));
		return as.stream()
				.map(m -> m.toString())
				.filter(m -> !m.isBlank())
				.reduce((s1, s2) -> s1 + System.lineSeparator() + s2
				).orElse("").replace("{newLine}", System.lineSeparator());
	}

	/*public String generateDescription() {
		if(getManualDesc() != null)
			return getManualDesc();
		int stBrvIncrease = this.getHitData().stream()
				.map(h -> (int)Math.floor((h.getSingleTargetBrvRate() / h.getBrvRate()) * 100))
				.filter(o -> o > 0)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(0);
		int overflow = this.getCommandType() == null || this.getCommandType().equals(CommandType.BT) ? 100 : 
			this.getHitData().stream()
				.map(h -> h.getMaxBrvOverflow())
				.filter(o -> o > 100)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(100);
		if(overflow <= 100)
			overflow = this.getHitData().stream().filter(hd -> hd.getEffectId() == 106).map(hd -> hd.getArguments()[0]).findFirst().orElse(100);
		int breakOverflow = this.getHitData().stream()
				.map(h -> h.getMaxBrvOverflowBreak())
				.filter(o -> o > 0)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(0);
		int brvDamageLimit = this.getHitData().stream()
				.map(h -> h.getBrvDamageLimitUp())
				.filter(o -> o > 0)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(0);
		int maxBrvLimit = this.getHitData().stream()
				.map(h -> h.getMaxBrvLimitUp())
				.filter(o -> o > 0)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(0);
		List<String> preEffects = new LinkedList<>();
		List<String> effectList = new LinkedList<>();
		List<String> postEffects = new LinkedList<>();
		List<List<HitData>> effects = new LinkedList<>();
		List<HitData> currentChain = new LinkedList<>();
		
		List<Element> sameElements = null;
		com.materiabot.GameElements.Enumerators.Ability.HitData.AttackType sameAttackTypes = null;
		boolean sameElement = true;
		boolean sameAttackType = true;
		
		List<Ailment> toAdd = new LinkedList<>();
		List<Ailment> missing = this.getAilments();
		int count = -1;
		for(HitData hd : this.getHitData()) {
			count++;
			if(count > 0) {
				int count2 = count;
				List<Ailment> thisCountAilments = missing.stream().filter(a -> a.getHitOrder() == count2).collect(Collectors.toList());
				if(!thisCountAilments.isEmpty())
					toAdd.addAll(thisCountAilments);
			}
			if(hd.getManualDescription() != null) {
				effectList.add(hd.getManualDescription());
				continue;
			}
			hd.getDescription(); //To apply any fixes that might be needed to other stuff
			if(hd.getEffect().isPreEffect(hd.getEffectValueType()) && !preEffects.contains(hd.getDescription()))
				preEffects.add((hd.getEffect().getTags().contains(TAG.MERGE) ? "{retLine} " : "") + hd.getDescription());
			else if(hd.getEffect().isPostEffect(hd.getEffectValueType()) && !postEffects.contains(hd.getDescription()))
				postEffects.add((hd.getEffect().getTags().contains(TAG.MERGE) ? "{retLine} " : "") + hd.getDescription());
			if(hd.getEffect().isBRV() && Type.isBRV(hd.getType()) && (currentChain.isEmpty() || currentChain.get(0).getEffect().isBRV())) {
				currentChain.add(hd);
				if(sameElements == null)
					sameElements = hd.getElements();
				if(sameAttackTypes == null)
					sameAttackTypes = hd.getAttackType();
				sameElement = sameElement && sameElements.size() == hd.getElements().size() && sameElements.containsAll(hd.getElements()) && hd.getElements().containsAll(sameElements);
				sameAttackType = sameAttackType && sameAttackTypes.getId() == hd.getAttackType().getId();
			}else if(hd.getEffect().isHP() && Type.isHP(hd.getType()) && (currentChain.isEmpty() || currentChain.get(0).getEffect().isBRV())) {
				currentChain.add(hd);
				toAdd.stream().forEach(a -> effects.add(Arrays.asList(new HitData(this, a.generateInLineDescription()))));
				toAdd = new LinkedList<>();
				effects.add(currentChain);
				currentChain = new LinkedList<>();
			}
			else if(!hd.getEffect().isPostEffect(hd.getEffectValueType()) && !hd.getEffect().isPreEffect(hd.getEffectValueType())){
				if(!currentChain.isEmpty()) {
					effects.add(currentChain);
					currentChain = new LinkedList<>();
				}
				currentChain.add(hd);
				toAdd.stream().forEach(a -> effects.add(Arrays.asList(new HitData(this, a.generateInLineDescription()))));
				toAdd = new LinkedList<>();
				effects.add(currentChain);
				currentChain = new LinkedList<>();
			}
		}
		StringBuilder brvPotency = new StringBuilder();
		int totalPotency = 0;
		if(!currentChain.isEmpty())
			effects.add(currentChain);
		if(this.getMovementCost() == 0)
			preEffects.add(0, "Instant Turn Rate (" + this.getMovementCost() + ")");
		else if(this.getMovementCost() < 30)
			preEffects.add(0, "High Turn Rate (" + this.getMovementCost() + ")");
		else if(this.getMovementCost() > 30)
			preEffects.add(0, "Low Turn Rate (" + this.getMovementCost() + ")");
		if(chaseDmg == 50)
			postEffects.add("Initiates a chase sequence (" + chaseDmg + " [CU](https://www.reddit.com/r/DissidiaFFOO/comments/7x7ffp/chase_mechanic/))");
		else if(chaseDmg > 6)
			postEffects.add("Easier to initiate a chase sequence (" + chaseDmg + " [CU](https://www.reddit.com/r/DissidiaFFOO/comments/7x7ffp/chase_mechanic/))");
		else if(chaseDmg == 0)
			preEffects.add("Cannot initiate a chase sequence");
		if(stBrvIncrease > 0)
			preEffects.add("Raises BRV Damage by " + stBrvIncrease + "% against ST");
		if(brvDamageLimit > 0)
			preEffects.add("+" + brvDamageLimit + "% Maximum BRV damage limit (up to " + Math.round(9999 * (1 + brvDamageLimit / 100f)) + ")");
		if(maxBrvLimit > 0)
			preEffects.add("+" + maxBrvLimit + "% Maximum obtainable BRV & HP damage limit (up to " + Math.round(99999 * (1 + maxBrvLimit / 100f)) + ")");
		for(List<HitData> chain : effects) {
			if(chain.isEmpty()) continue;
			if(chain.get(0).getEffect() != null && chain.get(0).getEffect().isBRV() && Type.isBRV(chain.get(0).getType())) {
				HitData.Extra help = new HitData.Extra(chain.get(0));
				help.showElements = !sameElement;
				help.showType = !sameAttackType;
				for(HitData hd : chain) {
					if(hd.getEffect().isBRV() && Type.isBRV(hd.getType()))
						help.count++;
					else if(hd.getEffect().isHP() && Type.isHP(hd.getType())) {
						help.hp = new HitData.Extra(hd);
						help.showElements = !sameElement;
						help.showType = !sameAttackType;
					}
				}
				effectList.add(help.getHitsDescription());
				brvPotency.append(" + " + Math.round(chain.get(0).getBrvRate()) + "%");
				if(help.count > 1)
					brvPotency.append(" x " + help.count);
				totalPotency += Math.round(chain.get(0).getBrvRate() * help.count);
			}
			else if(chain.get(0).getEffect() != null && chain.get(0).getEffect().isHP() && Type.isHP(chain.get(0).getType()))
				effectList.add("Followed by an HP Attack");
			else if(Constants.EFFECTS_THAT_CAN_REFUND.contains(chain.get(0).getEffectId()) && BasedOnStat.STATS_THAT_CAN_REFUND.contains(chain.get(0).getEffectValueType()) && chain.get(0).getTarget() == Target.Self) {
				effectList.add("{retLine} (" + chain.get(0).getArguments()[0] + "% refund)");
			}
			else {
				String text = (chain.get(0).getEffect() != null && chain.get(0).getEffect().getTags().contains(TAG.MERGE) ? "{retLine} " : "") + chain.get(0).getDescription();
				if(!(chain.get(0).getEffect() != null && chain.get(0).getEffect().blockRepeats() && effectList.contains(text)))
					effectList.add(text);
			}
		}
		effectList = effectList.stream().filter(s -> !s.isBlank()).collect(Collectors.toList());
		for(Ailment a : this.getAilments())
			if(a.getHitOrder() == -1)
				preEffects.add(a.generateInLineDescription());
			else if(a.getHitOrder() == 0)
				postEffects.add(a.generateInLineDescription());
		effectList.addAll(0, preEffects);
		effectList.addAll(postEffects);
		if(brvPotency.length() > 0) {
			brvPotency.append(" = " + totalPotency + "%");
			if(overflow > 100 && overflow < 5000)
				brvPotency.append(" (" + overflow + "% overflow");
			if(breakOverflow > 0 && breakOverflow < 5000)
				brvPotency.append(", " + (overflow + breakOverflow) + "% if target broken");
			if((overflow > 100 && overflow < 5000) || (breakOverflow > 0 && breakOverflow < 5000))
				brvPotency.append(")");
			effectList.add("{newLine}BRV Potency: " + brvPotency.substring(2));
		}
		List<DupeMerger> as = new ArrayList<>(Arrays.asList(effectList.stream().filter(s -> !s.isEmpty())
					.reduce((s1, s2) -> s1 + System.lineSeparator() + s2).orElse("").replace(System.lineSeparator() + "{retLine}", "")
					.split(System.lineSeparator())).stream().map(s -> new DupeMerger(s)).collect(Collectors.toList()));
		for(int i = 1; i < as.size(); i++)
			as.get(i-1).merge(as.get(i));
		return as.stream()
				.filter(dm -> !dm.toString().isBlank())
				.map(m -> m.toString())
				.reduce((s1, s2) -> s1 + System.lineSeparator() + s2
				).orElse("").replace("{newLine}", System.lineSeparator());
	}*/
	
	public static final Ability NULL(Unit unit, int id) {
		Ability a = new Ability("Unknown Ability " + id) {};
		a.setUnit(unit);
		return a;
	}
    @Override
    public int compareTo(Ability other) {
        return Integer.compare(this.getId(), other.getId());
    }
    @Override
    public String toString() {
        return this.getName().getBest();
    }
}