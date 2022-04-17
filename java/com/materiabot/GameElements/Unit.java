package com.materiabot.GameElements;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class Unit {
	private int id;
	private Region region = Region.None;
	private String name;
	private String pluginName;
	private Text fullName;
	private int series = -1;
	private Crystal crystal;
	private Equipment.Type equipmentType;
	private Unit GL, JP, common;
	private List<Equipment> equipment = new LinkedList<>();

	//Profile Data
	public Unit() { }
	public Unit(String name) { this.pluginName = this.name = name; }
	public Unit(String name, String pluginName) { this.name = name; this.pluginName = pluginName; }
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
	public List<Equipment> getEquipment() { return equipment; }
	public Equipment getEquipment(Equipment.Rarity rarity) {
		return getEquipment().stream().filter(e -> e.getRarity().equals(rarity)).findFirst().orElse(null);
	}
	public void loadFixGL() {}
	public void loadFixJP() { loadFixGL(); }

	public String getPluginName() { return pluginName != null ? pluginName : getName(); }
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