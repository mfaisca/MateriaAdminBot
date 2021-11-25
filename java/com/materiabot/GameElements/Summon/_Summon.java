package com.materiabot.GameElements.Summon;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.materiabot._Library;
import com.materiabot.GameElements.Element;
import com.materiabot.GameElements.Unit;

public abstract class _Summon{
	public static class SummonPassive{
		private String name, description, shortDesc;
		private boolean recommended = false;

		public SummonPassive(String n, String d){
			this(n, d, d, false);
		}
		public SummonPassive(String n, String d, boolean r){
			this(n, d, d, r);
		}
		public SummonPassive(String n, String d, String sd){
			this(n, d, sd, false);
		}
		public SummonPassive(String n, String d, String sd, boolean r) {
			name = n; description = d; shortDesc = sd; recommended = r;
		}
		
		public String getName() { return name; }
		public String getDescription() { return description; }
		public String getShortDesc() { return shortDesc; }
		public boolean isRecommended() { return recommended; }
		public void setRecommended(boolean recommended) { this.recommended = recommended; }
	}
	
	static {
		new Ifrit30();
		new Ifrit40();
		new Shiva30();
		new Shiva40();
		new Ramuh30();
		new Ramuh40();
		new Leviathan30();
		new Leviathan40();
		new Brothers30();
		new Brothers40();
		new Pandemonium30();
		new Pandemonium40();
		new Alexander30();
		new Alexander40();
		new Diabolos30();
		new Diabolos40();
		new Odin30();
		new Bahamut30();
		new Bahamut40();
		new Chocobo20();
		new Sylph20();
		new SpiritMoogle20();
	}
	
	private Element element;
	private String attackName, blessing, ability, chargeType, nodeCount;
	private List<String> nicknames = new LinkedList<>();
	private int turns, maxBrvBonus, maxLevel;
	private SummonPassive[] boardPassives;
	private String specialBoostedChars;
	private Unit[] chars = new Unit[6];
	
	protected _Summon(List<String> names, int level, Element element, String attackName, String blessing, String ability, int turns, int maxbrvBonus, String chargeSpeed) {
		this(names, level, element, attackName, blessing, null, ability, null, turns, maxbrvBonus, chargeSpeed);
	}
	protected _Summon(List<String> names, int level, Element element, String attackName, String blessing, String ability, 
					String nodeCount, String specialBoostedChars, int turns, int maxbrvBonus, String chargeSpeed, 
					SummonPassive... boardPassives) {
		nicknames = names.stream().map(s -> s.toLowerCase()).collect(Collectors.toList());
		this.maxLevel = level;
		this.element = element;
		this.attackName = attackName;
		this.blessing = blessing;
		this.ability = ability;
		this.nodeCount = nodeCount;
		this.boardPassives = boardPassives;
		this.turns = turns;
		this.maxBrvBonus = maxbrvBonus;
		this.chargeType = chargeSpeed;
		this.specialBoostedChars = specialBoostedChars;
		_Library.SUMMON_LIST.add(this);
	}
	
	public String getName() {
		return StringUtils.capitalize(nicknames.get(0));
	}
	public Element getElement() {
		return element;
	}
	public String getAttackName() {
		return attackName;
	}
	public String getBlessing() {
		return blessing;
	}
	public String getAbility() {
		return ability;
	}
	public String getChargeType() {
		return chargeType;
	}
	public String getNodeCount() {
		return nodeCount;
	}
	public List<String> getNicknames() {
		return nicknames;
	}
	public int getTurns() {
		return turns;
	}
	public int getMaxBrvBonus() {
		return maxBrvBonus;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public SummonPassive[] getBoardPassives() {
		return boardPassives;
	}
	public Unit[] getSpecialBoosteds() {
		int i = 0;
		if(specialBoostedChars != null)
			for(String ch : StringUtils.split(specialBoostedChars, "|"))
				chars[i++] = _Library.L.getUnit(ch);
		return chars;
	}
	public String getEmoteFrame() { return getName().replace(" ", "").toLowerCase() + "Frame"; }
	public String getEmoteGoldFrame() { return getName().replace(" ", "").toLowerCase() + "GFrame"; }
	public String getEmoteCrystal() { return getName().replace(" ", "").toLowerCase() + "Crystal"; }
	public String getEmoteFace() { return getName().replace(" ", "").toLowerCase() + "Face"; }
	public String getEmoteFrameLevelled() { return getMaxLevel() == 40 ? getEmoteGoldFrame() : getMaxLevel() == 30 ? getEmoteFrame() : getEmoteFace(); }

	public static final _Summon getSummon(String name) {
		return getSummon(name, -1);
	}
	public static final _Summon getSummon(String name, int level) {
		return _Library.SUMMON_LIST.stream()
					.filter(s -> s.getNicknames().contains(name) && (level == -1 || s.getMaxLevel() == level))
					.sorted((s1, s2) -> Integer.compare(s2.getMaxLevel(), s1.getMaxLevel()))
					.findFirst().orElse(null);
	}
	
	public static final _Summon getSummonFromWoIWeapon(Unit u) {
		_Summon s = null;
		switch(u.getName().toLowerCase()){
			case "cloud": s = _Library.getSummon("Ifrit"); break;
			case "rem": s = _Library.getSummon("Shiva"); break;
			case "yuna": s = _Library.getSummon("Ramuh"); break;
			case "laguna": s = _Library.getSummon("Leviathan"); break;
			case "squall": s = _Library.getSummon("Brothers"); break;
			case "zidane": s = _Library.getSummon("Pandemonium"); break;
			case "hope": s = _Library.getSummon("Alexander"); break;
			case "edgar": s = _Library.getSummon("Diabolos"); break;
			case "bartz": s = _Library.getSummon("Odin"); break;
			case "vaan": s = _Library.getSummon("Bahamut"); break;
		}
		return s;
	}
}