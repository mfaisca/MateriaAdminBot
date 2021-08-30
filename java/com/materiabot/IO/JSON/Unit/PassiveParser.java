package com.materiabot.IO.JSON.Unit;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Passive;
import com.materiabot.GameElements.Enumerators.Passive.PassiveCondition;
import com.materiabot.GameElements.Enumerators.Passive.PassiveEffect;
import com.materiabot.GameElements.Enumerators.Passive.PassiveTarget;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;

public class PassiveParser {
	public List<Passive> parsePassives(MyJSONObject obj, String passiveArray) {
		List<Passive> passives = new LinkedList<Passive>();
		for(MyJSONObject s : obj.getObjectArray(passiveArray)) {
			Passive p = parsePassive(s);
			if(p != null)
				passives.add(p);
		}
		return passives;
	}
	public Passive parsePassive(MyJSONObject s){
		Passive p = new Passive();
		if(s.getInt("error") != null) return null;
		p.setId(s.getInt("id"));
		p.setName(s.getText("name"));
		p.setDesc(s.getText("desc"));
		p.setShortDesc(s.getText("short_desc"));
		p.setCp(s.getObject("meta_data").getInt("cp"));
		p.setLevel(s.getObject("meta_data").getInt("level"));
		p.setTargetId(s.getObject("meta_data").getInt("target"));
		p.setTarget(PassiveTarget.get(s.getObject("meta_data").getInt("target")));

		for(MyJSONObject o : s.getObjectArray("effects")) {
			Integer eid = o.getInt("effect_id");
			Integer tid = o.getInt("effect_target");
			Integer[] val = o.getIntArray("effect_values");
			p.getEffects().add(new PassiveEffect(p, eid, tid, val));
		}
		for(MyJSONObject o : s.getObjectArray("conditions")) {
			Integer eid = o.getInt("required_id");
			Integer tid = o.getInt("required_target");
			Integer[] val = o.getIntArray("required_values");
			Integer tv = o.getInt("required_target_value");
			p.getConditions().add(new PassiveCondition(p, eid, tid, tv, val));
		}
		return p;
	}
}