package com.materiabot.GameElements;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
import com.materiabot.GameElements.Enumerators.Ability.AttackType;
import com.materiabot.GameElements.Enumerators.Ability.CommandType;
import com.materiabot.GameElements.Enumerators.Ability.HitType;
import com.materiabot.GameElements.Enumerators.Ability.TargetRange;
import com.materiabot.GameElements.Enumerators.Ability.TargetType;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Type;

public class Ability {
	public static final class MiscCondition{
		public int label;
		public int target;
		public int[] values;
		
	}
	
	private int id;
	private Text name;
	private Text desc;
	private String manualDesc;
	private int rank;
	private int movementCost;
	private int useCount;
	private boolean canLaunch;
	private int chaseDmg;
	private int hitTypeId;
	private HitType hitType; //Type_data.type
	private int attackTypeId;
	private AttackType attackType;
	private AttackName attackName; //My own data structure
	private int commandTypeId;
	private CommandType commandType;
	private int targetTypeId;
	private TargetType targetType;
	private int targetRangeId;
	private TargetRange targetRange;
	private List<HitData> hitData = new LinkedList<HitData>();
	private List<Ailment> ailments = new LinkedList<Ailment>();
	private int[] arguments; //Unknown use
	private Unit unit;
	
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
	public int getUseCount() { return useCount; }
	public void setUseCount(int useCount) { this.useCount = useCount; }
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
	public void setAttackName(AttackName attackName) { this.attackName = attackName; }
	public int getCommandTypeId() { return commandTypeId; }
	public void setCommandTypeId(int commandTypeId) { this.commandTypeId = commandTypeId; }
	public int getTargetTypeId() { return targetTypeId; }
	public void setTargetTypeId(int targetTypeId) { this.targetTypeId = targetTypeId; }
	public int getTargetRangeId() { return targetRangeId; }
	public void setTargetRangeId(int targetRangeId) { this.targetRangeId = targetRangeId; }
	public Unit getUnit() { return unit; }
	public void setUnit(Unit unit) { this.unit = unit; }
	
	public String generateDescription() {
		if(getManualDesc() != null)
			return getManualDesc();
		int stBrvIncrease = this.getHitData().stream()
				.map(h -> (int)Math.floor((h.getSingleTargetBrvRate() / h.getBrvRate()) * 100))
				.filter(o -> o > 0)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(0);
		int overflow = this.getCommandType().equals(CommandType.BT) ? 100 : 
			this.getHitData().stream()
				.map(h -> h.getMaxBrvOverflow())
				.filter(o -> o > 100)
				.max((o1, o2) -> Integer.compare(o1, o2)).orElse(100);
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
		List<String> preEffects = new LinkedList<String>();
		List<String> postEffects = new LinkedList<String>();
		List<List<HitData>> effects = new LinkedList<List<HitData>>();
		List<HitData> currentChain = new LinkedList<HitData>();
		for(HitData hd : this.getHitData()) {
			if(hd.getId() == 5)
				System.out.println();
			if(hd.getEffect().isPreEffect(hd.getEffectValueType()) && !preEffects.contains(hd.getDescription()))
				preEffects.add(hd.getDescription());
			else if(hd.getEffect().isPostEffect(hd.getEffectValueType()) && !postEffects.contains(hd.getDescription()))
				postEffects.add(hd.getDescription());
			if(hd.getEffect().isBRV() && Type.isBRV(hd.getType()) && (currentChain.size() == 0 || currentChain.get(0).getEffect().isBRV())) {
				currentChain.add(hd);
			}else if(hd.getEffect().isHP() && Type.isHP(hd.getType()) && (currentChain.size() == 0 || currentChain.get(0).getEffect().isBRV())) {
				currentChain.add(hd);
				effects.add(currentChain);
				currentChain = new LinkedList<HitData>();
			}
			else {
				effects.add(currentChain);
				currentChain = new LinkedList<HitData>();
				currentChain.add(hd);
			}
		}
		List<String> effectList = new LinkedList<String>();
		StringBuilder brvPotency = new StringBuilder();
		int totalPotency = 0;
		if(currentChain.size() > 0)
			effects.add(currentChain);
		if(stBrvIncrease > 0)
			preEffects.add(0, "Raises BRV Damage by " + stBrvIncrease + "% against ST");
		if(brvDamageLimit > 0)
			preEffects.add("Maximum BRV damage limit +" + brvDamageLimit + "% (up to " + Math.round(9999 * (1 + brvDamageLimit / 100f)) + ")");
		if(maxBrvLimit > 0)
			preEffects.add("Maximum obtainable BRV & HP damage limit +" + maxBrvLimit + "% (up to " + Math.round(99999 * (1 + maxBrvLimit / 100f)) + ")");
		effectList.addAll(preEffects);
		for(List<HitData> chain : effects) {
			if(chain.isEmpty()) continue;
			if(chain.get(0).getEffect().isBRV() && Type.isBRV(chain.get(0).getType())) {
				HitData.Extra help = new HitData.Extra(chain.get(0));
				for(HitData hd : chain) {
					if(hd.getEffect().isBRV() && Type.isBRV(hd.getType()))
						help.count++;
					else if(hd.getEffect().isHP() && Type.isHP(hd.getType()))
						help.hp = new HitData.Extra(hd);
				}
				effectList.add(help.getHitsDescription());
				brvPotency.append(" + " + Math.round(chain.get(0).getBrvRate()) + "%");
				if(help.count > 1)
					brvPotency.append(" x " + help.count);
				totalPotency += Math.round(chain.get(0).getBrvRate() * help.count);
			} else if(chain.get(0).getEffect().isHP() && Type.isHP(chain.get(0).getType())) {
				effectList.add("Followed by an HP Attack");
			} else
				effectList.add(chain.get(0).getDescription());
		}
		effectList.addAll(postEffects);
		if(brvPotency.length() > 0) {
			brvPotency.append(" = " + totalPotency + "%");
			if(overflow > 100)
				brvPotency.append(" (" + overflow + "% overflow");
			if(breakOverflow > 0)
				brvPotency.append(", " + (overflow + breakOverflow) + "% if target broken");
			if(overflow > 100 || breakOverflow > 0)
				brvPotency.append(")");
			effectList.add(System.lineSeparator() + "BRV Potency: " + brvPotency.substring(2).trim());
		}
		return effectList.stream().reduce((s1, s2) -> s1 + System.lineSeparator() + s2).orElse("Error parsing ability");
	}
}