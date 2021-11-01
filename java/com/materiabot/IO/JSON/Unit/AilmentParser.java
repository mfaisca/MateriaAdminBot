package com.materiabot.IO.JSON.Unit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Enumerators.Ailment.ConditionBlock;
import com.materiabot.GameElements.Enumerators.Ailment.RankData;
import com.materiabot.GameElements.Enumerators.Ailment.TargetType;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.AuraTarget;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Required._AuraRequired;
import com.materiabot.GameElements.Enumerators.Passive.RequiredTarget;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Aura;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import com.materiabot.Utils.Constants;
import Shared.Methods;

public class AilmentParser {
	public static List<Ailment> parseAilments(MyJSONObject obj, String ailmentArray) {
		List<Ailment> ret = new LinkedList<>();
		for(MyJSONObject a : obj.getObjectArray(ailmentArray)) {
			Ailment aa = parseAilment(a);
			if(aa != null)
				ret.add(aa);
		}
		return ret;
	}
	private static Ailment parseAilment(MyJSONObject ailment) {
		Ailment ail = new Ailment();
		ail.setId(ailment.getInt("id"));
		if(ail.getId() == -1)
			return null;
		ail.setCastId(ailment.getInt("cast_id"));
		ail.setName(ailment.getText("name"));
		ail.setDesc(ailment.getText("desc"));
		ail.setRate(ailment.getObject("meta_data").getInt("rate"));
		ail.setRank(ailment.getObject("meta_data").getInt("rank")-1); //Indexing on source files starts at 1 instead of 0
		ail.setTargetId(ailment.getObject("meta_data").getInt("target"));
		if(ailment.getObject("meta_data").getInt("hit_order") != null)
			ail.setHitOrder(ailment.getObject("meta_data").getInt("hit_order"));
		else
			ail.setHitOrder(-10); //Default Ailments
		ail.setTarget(TargetType.get(ail.getTargetId()));
		ail.setDuration(ailment.getObject("meta_data").getInt("duration"));
		ail.setAilmentConditionId(ailment.getObject("meta_data").getInt("condition"));
		ail.setAilmentCondition(Constants.AILMENT_REQUIRED.get(ail.getAilmentConditionId()));
		try {
			ail.setAilmentConditionValue(ailment.getObject("meta_data").getInt("condition_argument"));
		}catch(Exception e) {
			ail.setAilmentConditionBlock(parseConditionBlocks(ail, ailment.getObject("meta_data"), "condition_argument", true));
		}
		ail.setGroupId(ailment.getIntArray("group"));
		ail.setArgs(ailment.getObject("meta_data").getIntArray("arguments"));
		ail.setBuffType(ailment.getObject("type_data").getInt("buff_type"));
		ail.setIconType(ailment.getObject("type_data").getInt("icon_type"));
		ail.setDispType(ailment.getObject("type_data").getInt("disp_type"));
		ail.setExtendable(ailment.getObject("type_data").getBoolean("extendable"));
		ail.setBurstExtendable(ailment.getObject("type_data").getBoolean("burst_ext"));
		ail.setFramed(ailment.getObject("type_data").getInt("removable") == 0);
		ail.setMaxStacks(ailment.getObject("type_data").getInt("max_stacks"));
		if(ail.getMaxStacks() == -1) //Example: Ardyn Spectral Charge overhead
			ail.setMaxStacks(ail.getArgs()[1]);
		ail.setEffects(ailment.getObject("type_data").getIntArray("effects"));
		ail.setValTypes(ailment.getObject("type_data").getIntArray("val_types"));
		ail.setValEditTypes(ailment.getObject("type_data").getIntArray("val_edit_types"));
		ail.setValSpecify(ailment.getObject("type_data").getIntArray("val_specify"));
		ail.setRankTables(ailment.getObject("type_data").getIntArray("rank_tables"));
		ail.setConditions(parseConditionBlocks(ail, ailment.getObject("type_data"), "conditions"));
		for(Integer i : ail.getRankTables()) {
			if(i == -1) continue;
			RankData rd = new RankData(i.intValue());
			try {
				rd.setValues(Methods.splitRankData(ailment.getObject("rank_data").getIntArray("" + i)));
			} catch(Exception e) {
				rd.setValues(Methods.splitRankData(ailment.getObject("rank_data").getStringArray("" + i)));
			}
			ail.getRankData().put(i, rd);
		}
		for(MyJSONObject aura : ailment.getObjectArray("auras")) {
			Aura a = new Aura();
			a.setAilment(ail);
			a.setId(aura.getInt("id"));
			a.setRequiredConditionsIds(aura.getIntArray("required_conditions"));
			_AuraRequired[] aurasConditions = new _AuraRequired[3];
			boolean isSelf = false;
			for(int i = 0; i < 3; i++) {
				if(a.getRequiredConditionsIds()[i].intValue() == -1){
					aurasConditions[i] = null; 
					continue;
				}
				else if(a.getRequiredConditionsIds()[i].intValue() == 4 || (a.getRequiredConditionsIds()[i].intValue() == 3 && a.getId() == 576)) //Caius Special Weird Effect
					isSelf = true;
				_AuraRequired ar = Constants.AURA_REQUIRED.get(a.getRequiredConditionsIds()[i]);
				aurasConditions[i] = ar;
			}
			a.setRequiredConditions(aurasConditions);
			a.setRequiredValues(aura.getIntArray("required_values", 0));
			a.setRequiredConditionObjectValues(parseConditionBlocks(ail, aura, "required_values"));
			a.setEffectDataId(aura.getObject("effect_data").getInt("id"));
			a.setEffectId(aura.getObject("effect_data").getInt("ailment_effect"));
			a.setTypeId(aura.getObject("effect_data").getInt("type_id"));
			if(a.getEffectId() == -1 || a.getEffectId() == 0)
				a.setEffect(Constants.AURA_EFFECT.get(a.getTypeId()));
			else
				a.setEffect(Constants.AURA_EFFECT.get(a.getEffectId()));
			if(isSelf)
				a.setTargetId(99);
			else
				a.setTargetId(aura.getObject("effect_data").getInt("target_id"));
			a.setTarget(AuraTarget.get(a.getTargetId()));
			a.setValueType(aura.getObject("effect_data").getInt("value_type"));
			a.setRankData(aura.getObject("effect_data").getIntArray("rank_data"));
			ail.getAuras().add(a);
		}
		return ail;
	}

	@Deprecated
	private static ConditionBlock[] parseConditionBlocks(Ailment a, MyJSONObject root, String name, boolean fix) {
		List<ConditionBlock> conds = new LinkedList<>();
		ConditionBlock c = new ConditionBlock(a);
		try {
			MyJSONObject cond = new MyJSONObject(root.getJSON().getJSONObject(name));
			c.setId(cond.getInt("id"));
			for(MyJSONObject cond2 : cond.getObjectArray("conds")) {
				ConditionBlock c2 = new ConditionBlock(a);
				c2.setAilment(a);
				c2.setConditionId(cond2.getInt("require_id"));
				c2.setCondition(Constants.PASSIVE_REQUIRED.get(c2.getConditionId()));
				c2.setValues(Arrays.asList(cond2.getInt("require_value1"),0,0).toArray(new Integer[3]));
				c2.setTargetId(cond2.getInt("require_target"));
				c2.setTarget(RequiredTarget.get(c2.getTargetId()));
				c.getConditions().add(c2);
			}
		} catch(Exception e) {
			//-1
		}
		conds.add(c);
		c = new ConditionBlock(a);
		try {
			MyJSONObject cond = new MyJSONObject(root.getJSON().getJSONObject(name));
			c.setId(cond.getInt("id"));
			for(MyJSONObject cond2 : cond.getObjectArray("conds")) {
				ConditionBlock c2 = new ConditionBlock(a);
				c2.setAilment(a);
				c2.setConditionId(cond2.getInt("require_id_1"));
				c2.setCondition(Constants.PASSIVE_REQUIRED.get(c2.getConditionId()));
				c2.setValues(Arrays.asList(cond2.getInt("require_value1_1"),0,0).toArray(new Integer[3]));
				c2.setTargetId(cond2.getInt("require_target_1"));
				c2.setTarget(RequiredTarget.get(c2.getTargetId()));
				c.getConditions().add(c2);
			}
		} catch(Exception e) {
			//-1
		}
		conds.add(c);
		return conds.toArray(new ConditionBlock[conds.size()]);
	}
	public static ConditionBlock[] parseConditionBlocks(Ailment a, MyJSONObject root, String name) {
		List<ConditionBlock> conds = new LinkedList<>();
		for(int i = 0; i < root.getJSON().getJSONArray(name).length(); i++) {
			ConditionBlock c = new ConditionBlock(a);
			try {
				MyJSONObject cond = new MyJSONObject(root.getJSON().getJSONArray(name).getJSONObject(i));
				c.setId(cond.getInt("id"));
				for(MyJSONObject cond2 : cond.getObjectArray("conds")) {
					ConditionBlock c2 = new ConditionBlock(a);
					c2.setAilment(a);
					c2.setConditionId(cond2.getInt("id"));
					c2.setCondition(Constants.PASSIVE_REQUIRED.get(c2.getConditionId()));
					c2.setTargetId(cond2.getInt("target"));
					c2.setTarget(RequiredTarget.get(c2.getTargetId()));
					c2.setValues(cond2.getIntArray("values"));
					c.getConditions().add(c2);
				}
			} catch(Exception e) {
				//-1
			}
			conds.add(c);
		}
		return conds.toArray(new ConditionBlock[conds.size()]);
	}
}