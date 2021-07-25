package com.materiabot.GameElements.Enumerators.Passive;
import java.util.Arrays;
import com.materiabot.GameElements.Passive;
import com.materiabot.GameElements.Enumerators.Passive.Effect._PassiveEffect;
import com.materiabot.Utils.Constants;

public class PassiveEffect {
	private Passive passive;
	private Integer effectId;
	private _PassiveEffect effect;
	private Integer targetId;
	private EffectTarget target;
	private Integer[] values;
	
	public PassiveEffect(Passive p, Integer effectId, Integer target, Integer... requiredValues) {
		this.passive = p;
		this.effectId = effectId;
		this.targetId = target;
		this.values = requiredValues;
		this.effect = Constants.PASSIVE_EFFECT.get(effectId);
		this.target = EffectTarget.get(targetId);
	}
	
	public Passive getPassive() { return passive; }
	public Integer getEffectId() { return effectId; }
	public _PassiveEffect getEffect() { return effect; }
	public Integer getTargetId() { return targetId; }
	public EffectTarget getTarget() { return target; }
	public Integer[] getValues() { return values; }

	public String getDescription() {
		if(effect == null)
			return "Unknown Effect " + effectId + Arrays.toString(values) + " T:" + targetId;
		else
			return effect.getDescription(this);
	}
}