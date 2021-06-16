package com.materiabot.IO.JSON.Unit;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Enumerators.Ailment.ConditionBlock;
import com.materiabot.GameElements.Enumerators.Ailment.RankData;
import com.materiabot.GameElements.Enumerators.Ailment.TargetType;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.AuraTarget;
import com.materiabot.GameElements.Enumerators.Ailment.Aura.Required._AuraRequired;
import com.materiabot.GameElements.Enumerators.Ailment.Condition.ConditionTarget;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Aura;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import com.materiabot.Utils.Constants;
import Shared.Methods;

public class AilmentParser {
	public static List<Ailment> parseAilments(MyJSONObject obj, String ailmentArray) {
		List<Ailment> ret = new LinkedList<Ailment>();
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
		ail.setName(ailment.getText(ailment.getObject("name")));
		ail.setDesc(ailment.getText(ailment.getObject("desc")));
		ail.setRate(ailment.getObject("meta_data").getInt("rate"));
		ail.setRank(ailment.getObject("meta_data").getInt("rank")-1); //Indexing on source files starts at 1 instead of 0
		ail.setTargetId(ailment.getObject("meta_data").getInt("target"));
		ail.setTarget(TargetType.get(ail.getTargetId()));
		ail.setDuration(ailment.getObject("meta_data").getInt("duration"));
		ail.setArgs(ailment.getObject("meta_data").getIntArray("arguments"));
		ail.setBuffType(ailment.getObject("type_data").getInt("buff_type"));
		ail.setIconType(ailment.getObject("type_data").getInt("icon_type"));
		ail.setExtendable(ailment.getObject("type_data").getBoolean("extendable"));
		ail.setFramed(ailment.getObject("type_data").getInt("removable") == 0);
		ail.setMaxStacks(ailment.getObject("type_data").getInt("max_stacks"));
		ail.setEffects(ailment.getObject("type_data").getIntArray("effects"));
		ail.setValTypes(ailment.getObject("type_data").getIntArray("val_types"));
		ail.setValEditTypes(ailment.getObject("type_data").getIntArray("val_edit_types"));
		ail.setValSpecify(ailment.getObject("type_data").getIntArray("val_specify"));
		ail.setRankTables(ailment.getObject("type_data").getIntArray("rank_tables"));
		ail.setConditions(parseConditionBlocks(ailment.getObject("type_data"), "conditions", -1));
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
			a.setId(aura.getInt("id"));
			a.setRequiredConditionsIds(aura.getIntArray("required_conditions"));
			_AuraRequired[] aurasConditions = new _AuraRequired[3];
			for(int i = 0; i < 3; i++) {
				if(a.getRequiredConditionsIds()[i].intValue() == -1){
					aurasConditions[i] = null; 
					continue;
				}
				_AuraRequired ar = Constants.AURA_REQUIRED.get(a.getRequiredConditionsIds()[i]);
				aurasConditions[i] = ar;
			}
			a.setRequiredConditions(aurasConditions);
			a.setRequiredValues(parseConditionBlocks(aura, "required_values", 0));
			a.setEffectDataId(aura.getObject("effect_data").getInt("id"));
			a.setEffectId(aura.getObject("effect_data").getInt("ailment_effect"));
			a.setTypeId(aura.getObject("effect_data").getInt("type_id"));
			if(a.getEffectId() == -1 || a.getEffectId() == 0)
				a.setEffect(Constants.AURA_EFFECT.get(a.getTypeId()));
			else
				a.setEffect(Constants.AURA_EFFECT.get(a.getEffectId()));
			a.setTargetId(aura.getObject("effect_data").getInt("target_id"));
			a.setTarget(AuraTarget.get(a.getTargetId()));
			a.setValueType(aura.getObject("effect_data").getInt("value_type"));
			a.setRankData(aura.getObject("effect_data").getIntArray("rank_data"));
			a.setAilment(ail);
			ail.getAuras().add(a);
		}
		return ail;
	}
	
	private static ConditionBlock[] parseConditionBlocks(MyJSONObject root, String name, int nullValue) {
		List<ConditionBlock> conds = new LinkedList<ConditionBlock>();
		for(int i = 0; i < root.getJSON().getJSONArray(name).length(); i++) {
			ConditionBlock c = new ConditionBlock();
			try { //IsNumber
				c.setSimpleValue(root.getJSON().getJSONArray(name).getInt(i));
			} catch(Exception e) { //IsConditionBlock
				MyJSONObject cond = new MyJSONObject(root.getJSON().getJSONArray(name).getJSONObject(i));
				c.setId(cond.getInt("id"));
				for(MyJSONObject cond2 : cond.getObjectArray("conds")) {
					ConditionBlock c2 = new ConditionBlock();
					c2.setConditionId(cond2.getInt("id"));
					c2.setCondition(Constants.AILMENT_CONDITION.get(c2.getConditionId()));
					c2.setTargetId(cond2.getInt("target"));
					c2.setTarget(ConditionTarget.get(c2.getTargetId()));
					c2.setValues(cond2.getIntArray("values"));
					c.getConditions().add(c2);
				}
			}
			conds.add(c);
		}
		return conds.toArray(new ConditionBlock[conds.size()]);
	}
}