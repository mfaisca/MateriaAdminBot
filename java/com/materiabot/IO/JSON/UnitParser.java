package com.materiabot.IO.JSON;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import com.materiabot.GameElements.Crystal;
import com.materiabot.GameElements.Text;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Equipment;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import Shared.Methods;

public abstract class UnitParser {
	private UnitParser() {}
	private static boolean debug = false; //Debug only for the Effect Analyzer for faster processing
	
	public static void setDebug(boolean d) {
		debug = d;
	}
	public static Unit parseUnit(String name) {
		return createUnit(name, false);
	}
	public static Unit parseUnitQuick(String name) {
		return createUnit(name, true);
	}
	private static Unit createUnit(String n, boolean quickRead) {
		try{
			String name = debug ? n : Optional.ofNullable(SQLAccess.getUnitNameFromNickname(n)).orElse(n);
			Unit u = Constants.UNITS.stream()
						.filter(uu -> uu.getName().equalsIgnoreCase(name))
						.findFirst().orElse(null);
			if(u != null && u.getName() != null)
				u = u.copy();
			else
				u = new Unit();
			String unitName = Methods.urlizeDB(u.getPluginName() != null ? u.getPluginName() : name).toLowerCase();
			File fGL = new File("./resources/units/gl/db_" + unitName + ".json");
			File fJP = new File("./resources/units/jp/db_" + unitName + ".json");
			if(!fGL.exists() && !fJP.exists())
				return new Unit(null);
			MyJSONObject objGL = fGL.exists() ? JSONParser.loadContent(fGL.getAbsolutePath(), false) : null;
			MyJSONObject objJP = fJP.exists() ? JSONParser.loadContent(fJP.getAbsolutePath(), false) : null;
			if(objGL == null)
				parseProfile(u, objJP);
			else
				parseProfile(u, objGL);
			if(quickRead)
				return u;
			if(objGL != null) {
				Unit u2 = u.copy();
				parseGear(u2, objGL);
				u.setGL(u2);
			}
			if(objJP != null) {
				Unit u2 = u.copy();
				parseGear(u2, objJP);
				u.setJP(u2);
			}
			return u;
		} catch(Exception e) {
			e.printStackTrace();
			return new Unit(null);
		}
	}
	
	private static void parseProfile(Unit u, MyJSONObject obj) {
		Text name = obj.getObject("profile").getText("shortName");
		if(name == null)
			name = obj.getObject("profile").getText("fullName");
		if(u.getName() == null)
			u.setName(name.getBest());
		u.setFullName(obj.getObject("profile").getText("fullName"));
		u.setCrystal(Crystal.find(obj.getObject("profile").getInt("crystal")));
		u.setEquipmentType(Equipment.Type.find(obj.getObject("profile").getInt("weaponType")));
		u.setSeries(obj.getObject("profile").getInt("world"));
		u.setId(obj.getInt("id"));
//		for(int i = 0; i < 3; i++)
//			u.getSphereSlots()[i] = SphereType.get(obj.getObject("profile").getObject("traits").getStringArray("spheres")[i]);
	}
	private static void parseGear(Unit u, MyJSONObject obj) {
		for(String gearType : Arrays.asList("silverWeapon", "baseWeapon", 
				"uniqueWeapon", "summonWeapon", "ntWeapon", "manikinWeapon", "exWeapon", "realizedWeapon", "limitedWeapon", "forceWeapon", "burstWeapon", "rzBurstWeapon", 
				"silverArmor", "uniqueArmor", "exArmor", "realizedArmor", "highArmor", "rzHighArmor")) {
			MyJSONObject gear = obj.getObject("gearList").getObject(gearType);
			if(gear.getInt("id") == null) continue;
			Equipment equip = new Equipment();
			equip.setId(gear.getInt("id"));
			equip.setName(gear.getText("name"));
			equip.setType(gearType.contains("Armor") ? Equipment.Type.Armor : u.getEquipmentType());
			equip.setRarity(Equipment.Rarity.getByName(gearType));
			equip.setUnit(u);
			u.getEquipment().add(equip);
		}
		MyJSONObject gear = obj.getObject(Equipment.Rarity.BS.getName());
		Equipment equip = new Equipment();
		equip.setId(gear.getInt("id"));
		equip.setName(new Text());
		equip.setType(Equipment.Type.BloomStone);
		equip.setRarity(Equipment.Rarity.BS);
		equip.setUnit(u);
		u.getEquipment().add(equip);
	}
}