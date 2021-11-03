package com.materiabot.IO.JSON;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.ChainAbility;
import com.materiabot.GameElements.Crystal;
import com.materiabot.GameElements.Equipment;
import com.materiabot.GameElements.MiscCondition;
import com.materiabot.GameElements.Passive;
import com.materiabot.GameElements.Sphere;
import com.materiabot.GameElements.Sphere.SphereType;
import com.materiabot.GameElements.Text;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
import com.materiabot.GameElements.Enumerators.Ability.MiscConditionTarget;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import com.materiabot.IO.JSON.Unit.AbilityParser;
import com.materiabot.IO.JSON.Unit.AilmentParser;
import com.materiabot.IO.JSON.Unit.PassiveParser;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import Shared.Methods;

public class UnitParser {
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
				u = new Unit(name);
			String unitName = Methods.urlizeDB(u.getName()).toLowerCase();
			File f = new File("./resources/units/db_" + unitName + ".json");
			if(!f.exists())
				return new Unit(null);
			MyJSONObject obj = JSONParser.loadContent(f.getAbsolutePath(), false);
			parseProfile(u, obj);
			if(quickRead)
				return u;
			parseDefaultAilments(u, obj);
			parseCompleteListAbilities(u, obj);
			parseBaseAbilities(u, obj);
			parseOptionalAbilities(u, obj);
			parseTriggeredAbilities(u, obj);
			parseCalls(u, obj);
			parsePassives(u, obj);
			parseCharaBoards(u, obj);
			mergeFakeFollowups(u);
			parseArtifacts(u, obj);
			parseSpheres(u, obj);
			parseGear(u, obj);
			u.loadFix();
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
		u.setName(name.getBest());
		u.setFullName(obj.getObject("profile").getText("fullName"));
		u.setCrystal(Crystal.find(obj.getObject("profile").getInt("crystal")));
		u.setEquipmentType(Equipment.Type.find(obj.getObject("profile").getInt("weaponType")));
		u.setSeries(obj.getObject("profile").getInt("world"));
		u.setId(obj.getInt("id"));
		for(int i = 0; i < 3; i++)
			u.getSphereSlots()[i] = SphereType.get(obj.getObject("profile").getObject("traits").getStringArray("spheres")[i]);
	}
	private static void parsePassives(Unit u, MyJSONObject obj) {
		for(Passive p : new PassiveParser().parsePassives(obj, "awakeningPassives")) {
			p.setUnit(u);
			u.getJPPassives().put(p.getLevel(), p);
		}
		for(Passive p : new PassiveParser().parsePassives(obj, "glAwakeningPassives")) {
			p.setUnit(u);
			u.getGLPassives().put(p.getLevel(), p);
		}
	}
	private static void parseCompleteListAbilities(Unit u, MyJSONObject obj) {
		for(Ability a : AbilityParser.parseAbilities(obj, "completeListOfAbilities")) {
			a.setUnit(u);
			a.getAilments().stream()
				.forEach(ail -> {
					ail.setAbility(a);
					u.getAilments().put(ail.getId(), ail);
					ail.setUnit(u);
				});
			u.getAbilities().put(a.getId(), a);
		}
	}
	private static void parseBaseAbilities(Unit u, MyJSONObject obj) {
		u.setBaseAbilities(obj.getIntArray("defaultAbilities"));
		int typeIdx = 0;
		for(int id : u.getBaseAbilities())
			u.getSpecificAbility(id).setAttackName(AttackName.values()[typeIdx++]);
	}
	private static void parseDefaultAilments(Unit u, MyJSONObject obj) {
		for(Ailment a : AilmentParser.parseAilments(obj, "defaultAilments")) {
			a.setUnit(u);
			a.setDefault(true);
			u.getAilments().put(a.getId(), a);
		}
	}
	private static void parseOptionalAbilities(Unit u, MyJSONObject obj) {
		int typeIdx = 0;
		for(MyJSONObject[] skill : obj.getArrayArray("optionalAbilities")) {
			for(MyJSONObject skillLevel : skill) {
				ChainAbility ca = new ChainAbility();
				ca.setUnit(u);
				ca.setId(skillLevel.getInt("id"));
				ca.setOriginalId(skillLevel.getInt("originalAbility"));
				ca.setSecondaryId(skillLevel.getInt("upgradedAbility"));
				ca.setUpgraded(true);
				ca.setReqExtendPassives(Arrays.asList(skillLevel.getIntArray("reqExtendPassives")));
				ca.setReqWeaponPassives(Arrays.asList(skillLevel.getIntArray("reqWeaponPassives")));
				for(MyJSONObject miscC : skillLevel.getObjectArray("miscConditions")) {
					MiscCondition mc = new MiscCondition();
					mc.setAb(ca);
					mc.setLabelId(miscC.getInt("label"));
					mc.setLabel(Constants.LABELS.get(mc.getLabelId()));
					mc.setTargetId(miscC.getInt("target"));
					mc.setTarget(MiscConditionTarget.get(mc.getTargetId()));
					try {
						mc.setValues(miscC.getIntArray("values"));
					} catch(Exception e) {
						mc.setCondition(AilmentParser.parseConditionBlocks(null, miscC, "values")[0]);
					}
					ca.getReqMiscConditions().add(mc);
				}
				if(u.getSpecificAbility(ca.getSecondaryId()) == null) 
					continue;
				u.getSpecificAbility(ca.getSecondaryId()).setAttackName(AttackName.values()[typeIdx]);
				u.getUpgradedAbilities().add(ca);
			}
			if(skill.length > 0)
				typeIdx++;
		}
	}
	private static void parseTriggeredAbilities(Unit u, MyJSONObject obj) {
		int typeIdx = 0;
		for(MyJSONObject skillLevel : obj.getObjectArray("triggeredAbilities")) {
			ChainAbility ca = new ChainAbility();
			ca.setUnit(u);
			ca.setId(skillLevel.getInt("id"));
			ca.setOriginalId(skillLevel.getInt("originalAbility"));
			ca.setSecondaryId(skillLevel.getInt("triggeredAbility"));
			ca.setTriggered(true);
			ca.setReqExtendPassives(Arrays.asList(skillLevel.getIntArray("reqExtendPassives")));
			ca.setReqWeaponPassives(Arrays.asList(skillLevel.getIntArray("reqWeaponPassives")));
			for(MyJSONObject miscC : skillLevel.getObjectArray("miscConditions")) {
				MiscCondition mc = new MiscCondition();
				mc.setAb(ca);
				mc.setLabelId(miscC.getInt("label"));
				mc.setLabel(Constants.LABELS.get(mc.getLabelId()));
				mc.setTargetId(miscC.getInt("target"));
				mc.setTarget(MiscConditionTarget.get(mc.getTargetId()));
				mc.setValues(miscC.getIntArray("values"));
				ca.getReqMiscConditions().add(mc);
			}
			if(u.getSpecificAbility(ca.getSecondaryId()) == null) 
				continue;
			u.getSpecificAbility(ca.getSecondaryId()).setAttackName(AttackName.values()[typeIdx]);
			u.getTriggeredAbilities().add(ca);
		}
	}
	private static void parseCalls(Unit u, MyJSONObject obj) {
		{
			if(obj.getObject("assistAbility").getInt("id") == null)
				return;
			Integer callId = obj.getObject("assistAbility").getObject("ability").getInt("rank_up");
			if(callId == null)
				return;
			Ability a = u.getSpecificAbility(callId);
			a.setAttackName(AttackName.CA);
			u.setCall(a);
		}{
			if(obj.getObject("ldAssistAbility").getInt("id") == null)
				return;
			Integer callLdId = obj.getObject("ldAssistAbility").getObject("ability").getInt("rank_up");
			if(callLdId == null)
				return;
			Ability a = u.getSpecificAbility(callLdId);
			a.setAttackName(AttackName.CALD);
			u.setCallLd(a);
		}
	}
	private static void parseCharaBoards(Unit u, MyJSONObject obj) {
		if(obj.getObject("enhancementBoard") == null)
			return;
		for(Passive p : new PassiveParser().parsePassives(obj.getObject("enhancementBoard"), "passives")) {
			p.setUnit(u);
			u.getCharaBoards().add(p);
		}
	}
	private static void parseArtifacts(Unit u, MyJSONObject obj) {
		u.getArtifacts().clear();
		for(Passive p : new PassiveParser().parsePassives(obj, "artifactList")) {
			p.setUnit(u);
			u.getArtifacts().add(p);
		}
	}
	private static void parseGear(Unit u, MyJSONObject obj) {
		PassiveParser pp = new PassiveParser();
		for(String gearType : Arrays.asList("silverWeapon", "baseWeapon", 
				"uniqueWeapon", "summonWeapon", "ntWeapon", "manikinWeapon", "exWeapon", "realizedWeapon", "limitedWeapon", "burstWeapon", "rzBurstWeapon", 
				"silverArmor", "uniqueArmor", "exArmor", "realizedArmor", "highArmor", "rzHighArmor")) {
			MyJSONObject gear = obj.getObject("gearList").getObject(gearType);
			if(gear.getInt("id") == null) continue;
			Equipment equip = new Equipment();
			equip.setId(gear.getInt("id"));
			equip.setName(gear.getText("name"));
			equip.setType(gearType.contains("Armor") ? Equipment.Type.Armor : u.getEquipmentType());
			equip.setRarity(Equipment.Rarity.getByName(gearType));
			equip.setUnit(u);
			Passive p = pp.parsePassive(gear.getObject("passive"));
			if(p != null) {
				p.setUnit(u);
				equip.getPassives().add(p);
			}
			if(gear.getObjectArray("passives") != null)
				for(Passive ppp : pp.parsePassives(gear, "passives")) {
					ppp.setUnit(u);
					equip.getPassives().add(ppp);
				}
			u.getEquipment().add(equip);
		}
		MyJSONObject gear = obj.getObject(Equipment.Rarity.BS.getName());
		Equipment equip = new Equipment();
		equip.setId(gear.getInt("id"));
		equip.setName(new Text());
		equip.setType(Equipment.Type.BloomStone);
		equip.setRarity(Equipment.Rarity.BS);
		equip.setUnit(u);
		MyJSONObject gearPassive = gear.getObject("passive");
		equip.getPassives().add(new PassiveParser().parsePassive(gearPassive));
		u.getEquipment().add(equip);
	}
	private static void parseSpheres(Unit u, MyJSONObject obj) {
		Sphere s1 = null, s2 = null;
		for(String s : Arrays.asList("bonusSphere", "craftedSphere")) {
			MyJSONObject gear = obj.getObject(s);
			if(gear.getInt("id") == null) continue;
			Sphere sphere = new Sphere();
			sphere.setId(gear.getInt("id"));
			sphere.setType(SphereType.valueOf(gear.getString("category")));
			MyJSONObject gearPassive = gear.getObject("passive");
			sphere.setPassive(new PassiveParser().parsePassive(gearPassive));
			sphere.getPassive().setUnit(u);
			s2 = s1 == null ? null : sphere;
			s1 = s1 == null ? sphere : s1;
		}
		u.setSpheres(s1, s2);
	}
	private static void mergeFakeFollowups(Unit u) {
		if(u.getId() == 130) return; //Emperor to avoid merging traps
		List<ChainAbility> c = null;
		HashMap<Integer, Integer> map = new HashMap<>();
		c = u.getTriggeredAbilities().stream().map(ca -> {
			Ability a = null;
			Ability b = null;
			if(map.containsKey(ca.getOriginalId()) && ca.getReqMiscConditions().isEmpty()) {
				a = u.getSpecificAbility(map.get(ca.getOriginalId()));
				b = u.getSpecificAbility(ca.getSecondaryId());
			}
			else if(ca.getReqMiscConditions().stream().filter(mc -> !mc.getLabel().isInvisibleCondition(mc)).count() == 0) {
				a = u.getSpecificAbility(ca.getOriginalId());
				b = u.getSpecificAbility(ca.getSecondaryId());
			}
			if(a != null && b != null) {
				a.getHitData().addAll(b.getHitData());
				a.getAilments().addAll(b.getAilments());
				if(b.getMovementCost() > a.getMovementCost())
					a.setMovementCost(b.getMovementCost());
				a.setCanLaunch(a.isCanLaunch() || b.isCanLaunch());
				if(b.getChaseDmg() > a.getChaseDmg())
					a.setChaseDmg(b.getChaseDmg());
				map.put(b.getId(), a.getId());
			}
			return ca;
		}).filter(ca -> !ca.getReqMiscConditions().isEmpty()).collect(Collectors.toList());
		u.getTriggeredAbilities().clear();
		u.getTriggeredAbilities().addAll(c);
	}
}