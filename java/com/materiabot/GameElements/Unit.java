package com.materiabot.GameElements;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import com.materiabot.GameElements.Ability.BestAbilities;
import com.materiabot.GameElements.Sphere.SphereType;
import Shared.Methods;
import com.google.common.collect.Streams;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;

public class Unit {
	private int id;
	private Region region = Region.None;
	private String name;
	private Text fullName;
	private int series = -1;
	private Crystal crystal;
	private Equipment.Type equipmentType;
	private Unit GL, JP, common;
	private Integer[] baseAbilities = new Integer[8];
	private List<ChainAbility> triggeredAbilities = new LinkedList<>();
	private List<ChainAbility> upgradedAbilities = new LinkedList<>();
	private Ability call, callLd = null;
	private HashMap<Integer, Ability> abilities = new HashMap<>();
	private HashMap<Integer, Passive> passives = new HashMap<>();
	private List<Passive> charaBoards = new LinkedList<>();
	private HashMap<Integer, Ailment> ailments = new HashMap<>();
	private HashMap<Integer, Ailment> defaultAilments = new HashMap<>();
	private List<Equipment> equipment = new LinkedList<>();
	private List<Passive> artifacts = new LinkedList<>();
	private SphereType[] sphereSlots = new SphereType[3];
	private Sphere weaponSphere, basicSphere;

	//Profile Data
	public Unit(String name) { this.name = name; }
	public Unit get(Region region) { return region.equals(Region.GL) ? getGL() : getJP(); }
	public Unit getGL() { return GL; }
	public void setGL(Unit gL) { GL = gL; }
	public Unit getJP() { return JP; }
	public void setJP(Unit jP) { JP = jP; }
	public Unit getCommon() { return common == null ? this : common; }
	public void setCommon(Unit common) { this.common = common; }
	public Unit getOther() { return region.equals(Region.GL) ? common.getJP() : common.getGL(); }
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public Region getRegion() { return region; }
	public void setRegion(Region region) { this.region = region; }
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
	public HashMap<Integer, Passive> getPassives() { return passives; }
	public List<Passive> getCharaBoards() { return getCharaBoards(null); }
	public List<Passive> getCharaBoards(AttackName attackName) {
		if(attackName == null) return charaBoards;
		switch(attackName) {
		case S1: return charaBoards.subList(0, 6);
		case S2: return charaBoards.subList(6, 12);
		case EX: return charaBoards.subList(12, 18);
		case LD: return charaBoards.subList(18, charaBoards.size());
		default: return Arrays.asList();
		}
	}
	public HashMap<Integer, Ailment> getDefaultAilments() { return defaultAilments; }
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
		if(type != null && type.ordinal() < getBaseAbilities().length)
			return Arrays.asList(getAbilities().get(getBaseAbilities()[type.ordinal()]));
		return null;
	}
	public List<Ability> getAbility(AttackName type) {
		return getAbility(type, this.getRegion()).getAbilities();
	}

	public BestAbilities getAbility(AttackName type, Region region) { //The 2 getAbility exist so that this one can be overwritten by the Unit class
		if(type.equals(AttackName.CA))
			return new BestAbilities(Arrays.asList(this.getCall()));
		else if(type.equals(AttackName.CALD))
			return new BestAbilities(Arrays.asList(this.getCallLd()));
		Map<Integer, List<ChainAbility>> map = null;
		List<Integer> passivesIds = Streams.concat(	getPassives().values().stream(), 
													getEquipment().stream().flatMap(e -> e.getPassives().stream()),
													getCharaBoards().stream()).map(p -> p.getId())
											.collect(Collectors.toList());
		try {
			int baseId = this.getBaseAbility(type).get(0).getId();
//			map = this.getUpgradedAbilities().stream().filter(ca -> ca.getOriginalId() == baseId)
//													.filter(ca -> passivesIds.containsAll(ca.getReqExtendPassives()) && passivesIds.containsAll(ca.getReqWeaponPassives()))
//													.collect(Collectors.groupingBy(s -> Streams.concat(s.getReqExtendPassives().stream(), 
//																										s.getReqWeaponPassives().stream())
//																						.reduce(Integer::sum).orElse(0), Collectors.toList()));
			map = this.getUpgradedAbilities().stream().filter(ca -> ca.getOriginalId() == baseId)
					.filter(ca -> passivesIds.containsAll(ca.getReqExtendPassives()) && passivesIds.containsAll(ca.getReqWeaponPassives()))
					.collect(Collectors.groupingBy(s -> s.getReqExtendPassives().size() + s.getReqWeaponPassives().size(), Collectors.toList()));
		} catch(Exception e) { return new BestAbilities(null); }
		int max = map.keySet().stream().collect(Collectors.maxBy(Integer::compareTo)).orElse(0);
		Comparator<ChainAbility> comp = (ca1, ca2) -> {
			int ca1c = (int)ca1.getReqMiscConditions().stream().filter(mc -> !mc.getLabel().isInvisibleCondition(mc)).count();
			int ca2c = (int)ca2.getReqMiscConditions().stream().filter(mc -> !mc.getLabel().isInvisibleCondition(mc)).count();
			return Integer.compare(ca2c, ca1c);
		};
		comp = comp.thenComparing((ca1, ca2) -> {
			int cc1 = ca1.getSecondaryId();
			int cc2 = ca2.getSecondaryId();
			return Integer.compare(cc2, cc1);
		});
		List<ChainAbility> abs = new LinkedList<>();
		if(map.containsKey(max))
			for(ChainAbility ca : map.get(max).stream().sorted(comp).collect(Methods.maxAll((ca1, ca2) -> {
				int cc1 = this.getSpecificAbility(ca1.getSecondaryId()).getRank();
				int cc2 = this.getSpecificAbility(ca2.getSecondaryId()).getRank();
				return cc1 - cc2;
			}))) {
				long miscCount = ca.getReqMiscConditions().stream().filter(mc -> !mc.getLabel().isInvisibleCondition(mc)).count();
				if(miscCount == 0)
					abs.clear(); //To only have the highest ID'd ability, probably the best one
				abs.add(ca);
			}
		List<ChainAbility> abs2 = new LinkedList<>();
		for(int abId : abs.stream().map(ca -> ca.getSecondaryId()).collect(Collectors.toList())) {
			int recursiveId = abId;
			while(true) {
				int recursiveId2 = recursiveId;
				ChainAbility ca = getTriggeredAbilities().stream().filter(aa -> aa.getOriginalId() == recursiveId2)
						.filter(aa -> passivesIds.containsAll(aa.getReqExtendPassives()) && passivesIds.containsAll(aa.getReqWeaponPassives()))
						.findFirst().orElse(null);
				if(ca != null && ca.getOriginalId() == ca.getSecondaryId()) break;
				if(ca != null && getSpecificAbility(ca.getSecondaryId()) != null) {
					recursiveId = ca.getSecondaryId();
					abs2.add(ca);
				}
				else
					break;
			}
		}
		List<Ability> ret = Streams.concat(abs.stream(), abs2.stream()).map(ca -> ca.getSecondaryId()).distinct()
				.map(a -> this.getSpecificAbility(a)).collect(Collectors.toList());
		try {
			if(type.equals(AttackName.BT))
				ret.add(0, this.getBaseAbility(AttackName.BT).get(0));
		} catch(Exception e) {}
		if(ret.isEmpty()) {
			List<Ability> base = this.getBaseAbility(type);
			if(base != null)
				ret.addAll(base);
		}
		return new BestAbilities(ret);
	}
	public List<Ability> getAbility2(AttackName type, Region region) {
		if(type.equals(AttackName.CA))
			return Arrays.asList(this.getCall());
		else if(type.equals(AttackName.CALD))
			return Arrays.asList(this.getCallLd());
		Collection<ChainAbility> ret = new LinkedList<>();
//		ret.add(this.getBaseAbility(type).get(0).getId());
		int passiveCount = 0;
		for(ChainAbility ca : getUpgradedAbilities()) {
			if(this.getSpecificAbility(ca.getOriginalId()).getAttackName() != type) continue;			
			int reqCount = ca.getReqWeaponPassives().size() + ca.getReqExtendPassives().size();
			if(reqCount == passiveCount) {
				for(ChainAbility ca2 : ret) {
					int cac = (int)ca.getReqMiscConditions().stream().filter(mc -> !mc.getLabel().isInvisibleCondition(mc)).count();
					int ca2c = (int)ca.getReqMiscConditions().stream().filter(mc -> !mc.getLabel().isInvisibleCondition(mc)).count();
					if(ca2c >= cac || (ca.getReqMiscConditions().containsAll(ca2.getReqMiscConditions()) && ca2.getReqMiscConditions().containsAll(ca.getReqMiscConditions())))
						if(ca.getSecondaryId() > ca2.getSecondaryId()) {
							ret.remove(ca2);
							ret.add(ca);
							break;
						}
				}
				ret.add(ca);
			}
			else if(reqCount > passiveCount) {
				passiveCount = reqCount;
				ret.clear();
				ret.add(ca);
			}
		};
		LinkedList<ChainAbility> ret2 = new LinkedList<>();
		ret2.addAll(ret);
		ret.clear();
		for(int abId : ret2.stream().map(ca -> ca.getSecondaryId()).collect(Collectors.toList())) {
			int recursiveId = abId;
			while(true) {
				int recursiveId2 = recursiveId;
				ChainAbility ca = getTriggeredAbilities().stream().filter(aa -> aa.getOriginalId() == recursiveId2).findFirst().orElse(null);
				if(ca != null && ca.getOriginalId() == ca.getSecondaryId()) break;
				if(ca != null && getSpecificAbility(ca.getSecondaryId()) != null) {
					ret.add(ca);
					recursiveId = ca.getSecondaryId();
				}
				else
					break;
			}
		}
		LinkedList<Integer> rett = new LinkedList<>();
		if(type.equals(AttackName.BT))
			rett.addFirst(this.getBaseAbility(AttackName.BT).get(0).getId());
		return Streams.concat(rett.stream(), Streams.concat(ret2.stream(), ret.stream()).map(ca -> ca.getSecondaryId())).distinct()
				.map(a -> this.getSpecificAbility(a)).collect(Collectors.toList());
	}
	public Passive getPassive(int level) {
		return getPassives()
				.entrySet().stream()
				.filter(e -> e.getValue().getLevel() == level)
				.map(e -> e.getValue())
				.findFirst().orElse(null);
	}

	public Passive getEquipmentPassive(Equipment.Rarity rarity) {
		return getEquipmentPassive(rarity, 0);
	}
	public Equipment getEquipment(Equipment.Rarity rarity) {
		return getEquipment().stream().filter(e -> e.getRarity().equals(rarity)).findFirst().orElse(null);
	}
	public Passive getEquipmentPassive(Equipment.Rarity rarity, int idx) {
		return getEquipment().stream().filter(e -> e.getRarity().equals(rarity)).map(e -> e.getPassives().get(idx)).findFirst().orElse(null);
	}
	
	public Ability getSpecificAbility(Integer id) {
		if(id == null) return null;
		return Objects.requireNonNullElse(getAbilities().get(id), Ability.NULL(this, id));
	}
	public Passive getSpecificPassive(Integer id) {
		if(id == null) return null;
		return Objects.requireNonNullElse(
					getPassives().get(id), Objects.requireNonNullElse(
					getEquipment().stream().flatMap(e -> e.getPassives().stream()).filter(p -> p.getId() == id).findFirst().orElse(null), Objects.requireNonNullElse(
					getCharaBoards().stream().filter(p -> p.getId() == id).findFirst().orElse(null), 
					Passive.NULL(id))));
	}
	public Ailment getSpecificAilment(Integer id) {
		if(id == null) return null;
		return Objects.requireNonNullElse(getAilments().get(id), Objects.requireNonNullElse(getDefaultAilments().get(id), Ailment.NULL(id)));
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
	public void loadFixGL() {}
	public void loadFixJP() {}

	public String getPluginName() { return getName(); }
	public String toString() {
		return this.getName();
	}
	public Unit copy() { //Generic copy so that I can create a constructor for the right class that has the overridden methods
		try {
			return this.getClass().getConstructor(String.class).newInstance(this.getName());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchMethodException e) {
			try {
				return this.getClass().getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchMethodException e1) {
				return null;
			}
		}
	}
	public int compareTo(Object o) {
		return this.getName().compareTo(((Unit)o).getName());
	}
}