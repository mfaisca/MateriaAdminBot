package com.materiabot.GameElements;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.materiabot.GameElements.Sphere.SphereType;
import com.google.common.collect.Streams;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;

public class Unit {
	private int id;
	private Region region = Region.GL;
	private String name;
	private Text fullName;
	private int series = -1;
	private Crystal crystal;
	private Equipment.Type equipmentType;
	private Integer[] baseAbilities = new Integer[8];
	private List<ChainAbility> triggeredAbilities = new LinkedList<>();
	private List<ChainAbility> upgradedAbilities = new LinkedList<>();
	private Ability call, callLd = null;
	private HashMap<Integer, Ability> abilities = new HashMap<>();
	private HashMap<Integer, Passive> jpPassives = new HashMap<>();
	private HashMap<Integer, Passive> glPassives = new HashMap<>();
	private List<Passive> charaBoards = new LinkedList<>();
	private HashMap<Integer, Ailment> ailments = new HashMap<>();
	private List<Equipment> equipment = new LinkedList<>();
	private List<Passive> artifacts = new LinkedList<>();
	private SphereType[] sphereSlots = new SphereType[3];
	private Sphere weaponSphere, basicSphere;

	public Unit(String name, String... nicknames) {
		if(name != null && name.endsWith("_JP")) {
			region = Region.JP;
			name = name.replace("_JP", "");
		}
		this.name = name;
	}
	public Unit(String name, Crystal color, Equipment.Type eqType, String... nicknames) {
		this(name, nicknames);
		this.crystal = color;
		this.equipmentType = eqType;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public Region getRegion() { return region; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Text getFullName() { return fullName; }
	public void setFullName(Text fullName) { this.fullName = fullName; }
	public int getSeries() { return series; }
	public void setSeries(int series) { this.series = series; }
	public Crystal getCrystal() { return crystal; }
	public void setCrystal(Crystal c) { crystal = c; }
	public Equipment.Type getEquipmentType() { return equipmentType; }
	public void setEquipmentType(Equipment.Type t) { equipmentType = t; }
	public List<ChainAbility> getUpgradedAbilities() { return upgradedAbilities; }
	public List<ChainAbility> getTriggeredAbilities() { return triggeredAbilities; }
	public HashMap<Integer, Ability> getAbilities() { return abilities; }
	public HashMap<Integer, Passive> getJPPassives() { return jpPassives; }
	public HashMap<Integer, Passive> getGLPassives() { return glPassives; }
	public HashMap<Integer, Passive> getPassives() { return region.equals(Region.GL) ? getGLPassives() : getJPPassives(); }
	public HashMap<Integer, Passive> getPassives(Region region) { return region.equals(Region.GL) ? getGLPassives() : getJPPassives(); }
	public List<Passive> getCharaBoards() { return getCharaBoards(null); }
	public List<Passive> getCharaBoards(AttackName attackName) {
		if(attackName == null) return charaBoards;
		switch(attackName) {
		case S1: return charaBoards.subList(0, 6);
		case S2: return charaBoards.subList(6, 12);
		case EX: return charaBoards.subList(12, 18);
		case LD: return charaBoards.subList(18, charaBoards.size());
		default: return null; //Never
		}
	}
	public HashMap<Integer, Ailment> getAilments() { return ailments; }
	public List<Equipment> getEquipment() { return equipment; }
	public List<Passive> getArtifacts() { return artifacts; }
	public SphereType[] getSphereSlots() { return sphereSlots; }
	public void setSphereSlots(SphereType s1, SphereType s2, SphereType s3) { setSphereSlots(new SphereType[]{s1, s2, s3}); }
	public void setSphereSlots(SphereType... sph) { sphereSlots = sph; }
	public Sphere getWeaponSphere() { return weaponSphere; }
	public Sphere getBasicSphere() { return basicSphere; }
	public void setSpheres(Sphere weapon, Sphere basic) { this.basicSphere = basic; this.weaponSphere = weapon; }
	public Integer[] getBaseAbilities() { return baseAbilities; }
	public void setBaseAbilities(Integer[] baseAbls) { baseAbilities = baseAbls; }

	public List<Ability> getBaseAbility(AttackName type) {
		if(type != null && type.ordinal() < baseAbilities.length)
			return Arrays.asList(abilities.get(baseAbilities[type.ordinal()]));
		return null;
	}
	public List<Ability> getAbility(AttackName type) {
		return getAbility(type, "GL");
	}
	public List<Ability> getAbility(AttackName type, String region) {
		if(type.equals(AttackName.CA))
			return Arrays.asList(this.getCall());
		else if(type.equals(AttackName.CALD))
			return Arrays.asList(this.getCallLd());
		Collection<Passive> passives = region != null && region.equals("JP") ? getJPPassives().values() : getGLPassives().values();
		List<Integer> passivesIds = Streams.concat(	passives.stream(), 
													getCharaBoards().stream()).map(p -> p.getId())
											.collect(Collectors.toList());
		List<Integer> ret = new LinkedList<>();
		ret.add(this.getBaseAbility(type).get(0).getId());
		int passiveCount = 0;
		int condiCount = 0;
		for(ChainAbility ca : getUpgradedAbilities()) {
			if(this.getSpecificAbility(ca.getOriginalId()).getAttackName() != type) continue;
			if(!passivesIds.containsAll(ca.getReqExtendPassives())) continue;
			
			int reqCount = ca.getReqWeaponPassives().size() + ca.getReqExtendPassives().size();
			if(reqCount == passiveCount) {
				if(ca.getReqMiscConditions().stream().filter(mc -> !mc.getLabel().isInvisibleCondition(mc)).count() >= condiCount) {
					condiCount = (int)ca.getReqMiscConditions().stream().filter(mc -> !mc.getLabel().isInvisibleCondition(mc)).count();
					ret.add(ca.getSecondaryId());
				}
			}
			else if(reqCount > passiveCount) {
				condiCount = 0;
				passiveCount = reqCount;
				ret.clear();
				ret.add(ca.getSecondaryId());
			}
		}
		LinkedList<Integer> ret2 = new LinkedList<>();
		if(condiCount == 0)
			ret2.add(ret.stream().max(Integer::compareTo).orElse(-1));
		else
			ret2.addAll(ret);
		ret.clear();
		for(int abId : ret2) {
			int recursiveId = abId;
			while(true) {
				int recursiveId2 = recursiveId;
				ChainAbility ca = getTriggeredAbilities().stream().filter(aa -> aa.getOriginalId() == recursiveId2).findFirst().orElse(null);
				if(ca != null && ca.getOriginalId() == ca.getSecondaryId()) break;
				if(ca != null && getSpecificAbility(ca.getSecondaryId()) != null) {
					ret.add(ca.getSecondaryId());
					recursiveId = ca.getSecondaryId();
				}
				else
					break;
			}
		}
		if(type.equals(AttackName.BT))
			ret2.addFirst(this.getBaseAbility(AttackName.BT).get(0).getId());
		return Streams.concat(ret2.stream(), ret.stream()).distinct().map(a -> this.getSpecificAbility(a)).collect(Collectors.toList());
	}
	public Passive getPassive(int level) {
		return getPassive(level, null);
	}
	public Passive getPassive(int level, Region region) {
		return ((region == null ? Region.GL : region).equals(Region.GL) ? getPassives() : getJPPassives())
				.entrySet().stream()
				.filter(e -> e.getValue().getLevel() == level)
				.map(e -> e.getValue())
				.findFirst().orElse(null);
	}

	public Passive getEquipmentPassive(Equipment.Rarity rarity) {
		return getEquipmentPassive(rarity, 0);
	}
	public Equipment getEquipment(Equipment.Rarity rarity) {
		return equipment.stream().filter(e -> e.getRarity().equals(rarity)).findFirst().orElse(null);
	}
	public Passive getEquipmentPassive(Equipment.Rarity rarity, int idx) {
		return equipment.stream().filter(e -> e.getRarity().equals(rarity)).map(e -> e.getPassives().get(idx)).findFirst().orElse(null);
	}
	
	public Ability getSpecificAbility(Integer id) {
		if(id == null) return null;
		return Objects.requireNonNullElse(abilities.get(id), Ability.NULL(id));
	}
	public Passive getSpecificPassive(Integer id) {
		if(id == null) return null;
		return Objects.requireNonNullElse(glPassives.get(id), Passive.NULL(id));
	}
	public Ailment getSpecificAilment(Integer id) {
		if(id == null) return null;
		return Objects.requireNonNullElse(ailments.get(id), Ailment.NULL(id));
	}
	public List<ChainAbility> getUpgradedAbilities(Integer id){
		return this.getUpgradedAbilities().stream().filter(ta -> ta.getOriginalId() == id.intValue()).collect(Collectors.toList());
	}
	public List<ChainAbility> getTriggeredAbilities(Integer id){
		return this.getTriggeredAbilities().stream().filter(ta -> ta.getOriginalId() == id.intValue()).collect(Collectors.toList());
	}
	public Ability getCall() { return call; }
	public void setCall(Ability call) { this.call = call; }
	public Ability getCallLd() { return callLd; }
	public void setCallLd(Ability callLd) { this.callLd = callLd; }
		
	public String toString() {
		return this.getName();
	}
	public Unit copy() { //Generic copy so that I can create a constructor for the right class that has the overridden methods
		try {
			return (Unit)this.getClass().getConstructors()[0].newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			return null;
		}
	}
	public int compareTo(Object o) {
		return this.getName().compareTo(((Unit)o).getName());
	}
}