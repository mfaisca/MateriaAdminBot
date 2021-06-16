package com.materiabot.GameElements.Enumerators.Ability.HitData.Effect;
import java.util.Arrays;
import java.util.List;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.HitData;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators.Ability.HitData.Target;

public class _AbilityEffect {
	public static enum TAG{
		PRE_EFFECT, POST_EFFECT, BRV, HP, FULLHP, SPLASH, REPEAT;
	}
	
	protected int id;
	protected String baseDescription;
	protected List<TAG> tags;
	public static _AbilityEffect ERROR = null;
	
	public _AbilityEffect(int id, String desc, TAG... tags) {
		this.id = id;
		this.baseDescription = desc;
		this.tags = Arrays.asList(tags);
		if(id == -100)
			ERROR = this;
	}
	
	public final int getId() { return id; }
	public final String getBaseDescription() { return baseDescription; }

	public String getDescription(HitData hd) {
		return getDescription(getBaseDescription(), hd, hd.getTarget(), hd.getArguments());
	}
	protected static final String getDescription(String description, HitData hd, Target target, int... values) {
		description = description.replace("{t}", target.getDescription());
		for(int i = 0; i < values.length; i++)
			description = description.replace("{" + i + "}", ""+values[i]);
		return description;
	}

	public final boolean isPreEffect() { return tags.contains(TAG.PRE_EFFECT); }
	public final boolean isPostEffect() { return tags.contains(TAG.POST_EFFECT); }
	public boolean isPreEffect(int evt) { return isPreEffect(); }
	public boolean isPostEffect(int evt) { return isPostEffect(); }
	public boolean isBRV() { return tags.contains(TAG.BRV); }
	public boolean isHP() { return tags.contains(TAG.HP); }
	public boolean allowRepeats() { return tags.contains(TAG.REPEAT); }
	public List<TAG> getTags() { return tags; }

	protected static Ability convertIdToAbility(Unit u, int id) {
		return u.getAbilities().get(id);
	}
}