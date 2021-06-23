package com.materiabot.IO.JSON.Unit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Element;
import com.materiabot.GameElements.HitData;
import com.materiabot.GameElements.Enumerators.Ability.*;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Target;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Type;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import com.materiabot.Utils.Constants;

public class AbilityParser {	
	public static List<Ability> parseAbilities(MyJSONObject obj, String abilityArray) {
		List<Ability> abilities = new LinkedList<Ability>();
		for(MyJSONObject s : obj.getObjectArray(abilityArray)) {
			if(s.getInt("error") != null) continue; //Some abilities have error
			Ability a = parseAbility(s);
			if(a != null) {
				a.getAilments().addAll(AilmentParser.parseAilments(s, "ailments"));
				abilities.add(a);
			}
		}
		return abilities;
	}
	public static Ability parseAbility(MyJSONObject ab) {
		Ability a = new Ability();
		a.setId(ab.getInt("id"));
		a.setRank(ab.getInt("rank"));
		a.setName(ab.getText(ab.getObject("name")));
		a.setDesc(ab.getText(ab.getObject("desc")));
		a.setUseCount(ab.getInt("use_count"));
		a.setGroupId(ab.getIntArray("group_id"));
		a.setMovementCost(ab.getInt("movement_cost"));
		a.setAttackTypeId(ab.getObject("type_data").getInt("attack_type"));
		a.setAttackType(AttackType.get(a.getAttackTypeId()));
		a.setCommandTypeId(ab.getObject("type_data").getInt("command_type"));
		a.setCommandType(CommandType.get(a.getCommandTypeId()));
		a.setTargetTypeId(ab.getObject("type_data").getInt("target_type"));
		a.setTargetType(TargetType.get(a.getTargetTypeId()));
		a.setTargetRangeId(ab.getObject("type_data").getInt("target_range"));
		a.setTargetRange(TargetRange.get(a.getTargetRangeId()));
		a.setCanLaunch(ab.getObject("chase_data").getBoolean("can_initiate_chase"));
		a.setChaseDmg(ab.getObject("chase_data").getInt("chase_dmg"));
		a.setArguments(Arrays.asList(ab.getIntArray("arguments")).stream().mapToInt(i -> i).toArray());
		for(MyJSONObject data : ab.getObjectArray("hit_data")) {
			HitData hd = new HitData(a);
			hd.setId(data.getInt("id"));
			if(hd.getId() == 518) continue; //its that first useless hitframe
			hd.setTypeId(data.getInt("type"));
			hd.setType(Type.get(hd.getTypeId()));
			hd.setArguments(Arrays.asList(data.getIntArray("arguments")).stream().mapToInt(i -> i).toArray());
			hd.setAttackTypeId(data.getInt("attack_type"));
			hd.setAttackType(com.materiabot.GameElements.Enumerators.Ability.HitData.AttackType.get(hd.getAttackTypeId()));
			hd.getElements().addAll(Arrays.asList(data.getIntArray("element")).stream().map(eid -> Element.get(eid)).collect(Collectors.toList()));
			hd.setBrvRate(data.getObject("brv_data").getInt("brv_rate"));
			hd.setMaxBrvOverflow(data.getObject("brv_data").getInt("max_brv_overflow"));
			hd.setMaxBrvOverflowBreak(data.getObject("brv_data").getInt("max_brv_overflow_with_break"));
			hd.setSingleTargetBrvRate(data.getObject("brv_data").getInt("single_target_brv_rate"));
			hd.setBrvDamageLimitUp(data.getObject("brv_data").getInt("brv_damage_limit_up"));
			hd.setMaxBrvLimitUp(data.getObject("brv_data").getInt("max_brv_limit_up"));
			hd.setMaxBrvLimitUp(data.getObject("brv_data").getInt("brv_damage_limit_up_direct"));
			hd.setMaxBrvLimitUp(data.getObject("brv_data").getInt("max_brv_limit_up_direct"));
			hd.setTargetId(data.getInt("target"));
			hd.setTarget(Target.get(hd.getTargetId()));
			hd.setEffectId(data.getInt("effect"));
			hd.setEffect(Constants.ABILITY_EFFECT.get(hd.getEffectId()));
			hd.setEffectValueType(data.getInt("effect_value_type"));
			a.getHitData().add(hd);
		}
		return a;
	}
}