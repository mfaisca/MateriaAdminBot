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
	private String name;
	private int series = -1;
	private List<String> nicknames = new LinkedList<String>();
	private Crystal crystal;
	private Equipment.Type equipmentType;
	private Integer[] baseAbilities = new Integer[8];
	private List<ChainAbility> triggeredAbilities = new LinkedList<ChainAbility>();
	private List<ChainAbility> upgradedAbilities = new LinkedList<ChainAbility>();
	private HashMap<Integer, Ability> abilities = new HashMap<Integer, Ability>();
	private HashMap<Integer, Passive> jpPassives = new HashMap<Integer, Passive>();
	private HashMap<Integer, Passive> glPassives = new HashMap<Integer, Passive>();
	private List<Passive> charaBoards = new LinkedList<Passive>();
	private HashMap<Integer, Ailment> ailments = new HashMap<Integer, Ailment>();
	private List<Equipment> equipment = new LinkedList<Equipment>();
	private List<Passive> artifacts = new LinkedList<Passive>();
	private SphereType[] sphereSlots = new SphereType[3];
	private Sphere weaponSphere, basicSphere;

	public Unit(String name, String... nicknames) {
		this.name = name;
		this.nicknames.add(name.toLowerCase());
		if(nicknames != null)
			this.nicknames.addAll(Arrays.asList(nicknames).stream().map(s -> s.toLowerCase()).collect(Collectors.toList()));
	}
	public Unit(String name, Crystal color, Equipment.Type eqType, String... nicknames) {
		this(name, nicknames);
		this.crystal = color;
		this.equipmentType = eqType;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getName() { return name; }
	public int getSeries() { return series; }
	public void setSeries(int series) { this.series = series; }
	public List<String> getNicknames() { return nicknames; }
	public Crystal getCrystal() { return crystal; }
	public void setCrystal(Crystal c) { crystal = c; }
	public Equipment.Type getEquipmentType() { return equipmentType; }
	public void setEquipmentType(Equipment.Type t) { equipmentType = t; }
	public List<ChainAbility> getUpgradedAbilities() { return upgradedAbilities; }
	public List<ChainAbility> getTriggeredAbilities() { return triggeredAbilities; }
	public HashMap<Integer, Ability> getAbilities() { return abilities; }
	public HashMap<Integer, Passive> getJPPassives() { return jpPassives; }
	public HashMap<Integer, Passive> getGLPassives() { return glPassives; }
	public HashMap<Integer, Passive> getPassives() { return getGLPassives(); }
	public List<Passive> getCharaBoards() { return charaBoards; }
	public HashMap<Integer, Ailment> getAilments() { return ailments; }
	public List<Equipment> getEquipment() { return equipment; }
	public List<Passive> getArtifacts() { return artifacts; }
	public SphereType[] getSphereSlots() { return sphereSlots; }
	public SphereType[] setSphereSlots(SphereType s1, SphereType s2, SphereType s3) { return sphereSlots = new SphereType[] {s1, s2, s3}; }
	public SphereType[] setSphereSlots(SphereType... sph) { return sphereSlots = sph; }
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
		return getAbility(type, null);
	}
	public List<Ability> getAbility(AttackName type, String region) { //XXX TEST THIS
		Collection<Passive> passives = region != null && region.equals("JP") ? getJPPassives().values() : getGLPassives().values();
		List<Integer> passivesIds = Streams.concat(	passives.stream().map(p -> p.getId()), 
													getCharaBoards().stream().map(p -> p.getId()))
											.collect(Collectors.toList());
		List<Integer> ret = new LinkedList<Integer>();
		int max = 0;
		for(ChainAbility ca : getUpgradedAbilities()) {
			if(!passivesIds.containsAll(ca.getReqExtendPassives())) continue;
			if(ca.getReqWeaponPassives().size() == max)
				ret.add(ca.getSecondaryId());
			else if(ca.getReqWeaponPassives().size() > max) {
				max = ca.getReqWeaponPassives().size();
				ret.clear();
				ret.add(ca.getSecondaryId());
			}
		}
		return ret.stream().map(a -> this.getSpecificAbility(a)).sorted((a1, a2) -> a1.getId() - a2.getId()).collect(Collectors.toList());
	}
	public Passive getPassive(int level) {
		return getPassive(level, null);
	}
	public Passive getPassive(int level, String region) {
		return ((region == null ? "GL" : region).equals("GL") ? getPassives() : getJPPassives())
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
		return equipment.stream().filter(e -> e.getRarity().equals(rarity)).findFirst().orElse(null).getPassives().get(idx);
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

	public String toString() {
		return this.getName();
	}
	public Unit clone() { //Generic copy so that I can create a constructor for the right class that has the overridden methods
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