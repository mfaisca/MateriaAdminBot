package com.materiabot.Utils;
import java.util.Optional;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Equipment;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators.Ailment.TargetType;
import net.dv8tion.jda.api.entities.Emote;

public abstract class ImageUtils {
	public enum Emotes{
		GTFO("gtfo"),
		INVISIBLE("invisible"),
		BASE_DESC("baseDesc"),
		UNKNOWN_EMOTE("unknownEmote"),
		GEAR_NO_LB("iconnolb"),
		GEAR_LB("iconlb"),
		BLUE_ORB("blue_orb"),
		BLUE_POP("blue_pop"),
		SILVER_ORB("silver_orb"),
		SILVER_POP("silver_pop"),
		GOLD_ORB("gold_orb"),
		GOLD_POP("gold_pop"),
		BURST_ORB("burst_orb"),
		BURST_POP("burst_pop"),
		
		;private String text;
		
		private Emotes(String s) { text = s; }
		public String get() { return text; }
	}
	public enum Images{
		BLUE_ANIM("https://cdn.discordapp.com/attachments/570156853296758794/719123410223104020/blue_1.gif"),
		GOLD_ANIM("https://cdn.discordapp.com/attachments/570156853296758794/719123419945369640/goldfast.gif"),
		BURST_ANIM("https://cdn.discordapp.com/attachments/570156853296758794/719123430124814374/burst_1.gif"),
		SORRY_MOOGLE_URL("https://dissidiadb.com/static/img/0002.0260da5.png"),
		SURPRISED_MOOGLE_URL("https://dissidiadb.com/static/img/0014.6ec7022.png"),
		
		;private String text;
		
		private Images(String s) { text = s; }
		public String get() { return text; }
	}
	
	public static String getCharacterGearURL(Unit u, Equipment.Rarity equip) {
		return "https://dissidiadb.com/static/img/gear/" + u.getEquipment(equip).getId() + ".png";
	}

	public static String getAilmentEmote(Unit u, int ailmentId) {
		return getAilmentEmote(u.getAilments().get(ailmentId));
	}
	public static String getAilmentEmote(Ailment a) {
		if(a == null)
			return ImageUtils.Emotes.UNKNOWN_EMOTE.get();
		return getAilmentEmote(a.getIconType(), a.getDispType(), 
					a.getTarget() == TargetType.Target || a.getTarget() == TargetType.AllEnemies, a.getMaxStacks() > 0 ? 1 : -1, a.getId());
	}
	public static String getAilmentEmote(int iconType, int dispType, boolean enemy, int stacks, int buffId) {
		String emote = null;
		if(iconType > 0 && iconType != 14) {
			if(stacks > 0)
				emote = enemy ? "ailment3" : "ailment17";
			else
				emote = "ailment" + iconType;
		}
		else if(iconType == 14 && dispType > 0)
			emote = "specialAilment" + dispType;
		else if(buffId > 0)
			switch(buffId) {
				case 88: emote = "ailmentNinjaOK"; break;
				case 89: emote = "ailmentSageOK"; break;
				case 115: emote = "ailmentAce"; break;
				case 1508: emote = "ailmentTrey"; break;
				case 3053: emote = "ailmentJegran"; break;
			}
		if(stacks > 0 && emote != null) {
			String ret = getEmoteText(emote + "_" + stacks);
			if(!ret.contains(ImageUtils.Emotes.UNKNOWN_EMOTE.get()))
				return ret;
		}
		if(emote == null || ImageUtils.Emotes.UNKNOWN_EMOTE.get().equals(emote))
			return getEmoteText("ailmentInvisible");
		return getEmoteText(emote);
	}
	
	public static Emote getEmoteClassByName(String name) {
		final String name2 = name.replace(" ", "").replace("'", "").replace("&", "").replace("(", "").replace(")", "");
		if(Constants.getClient() == null) return null;
		Emote emo = Constants.getClient().getGuilds().stream()
				.filter(s -> s.getOwnerIdLong() == Constants.QUETZ_ID && s.getIdLong() != Constants.MATERIABOT_SERVER_ID && s.getIdLong() != Constants.MATERIABOT_ADMIN_SERVER_ID)
				.flatMap(g -> g.getEmotes().stream())
				.filter(e -> e.getName().equalsIgnoreCase(name2))
				.findFirst()
				.orElse(name2.equalsIgnoreCase(ImageUtils.Emotes.UNKNOWN_EMOTE.get()) ? null : getEmoteClassByName(ImageUtils.Emotes.UNKNOWN_EMOTE.get()));
		return emo;
	}
	public static String getRegionEmoteText(String region) {
		switch(region.toUpperCase()) {
		case "GL": return getEmoteText("GL_Version");
		case "JP": return getEmoteText("JP_Version");
		case "GL & JP": return getEmoteText("GL_Version") + getEmoteText("JP_Version");
		}
		return getEmoteText2(ImageUtils.Emotes.UNKNOWN_EMOTE.get());
	}
	public static String getEmoteText(String name) {
		Optional<Emote> o = Optional.ofNullable(getEmoteClassByName(name));
		return o.isPresent() ? "<:" + o.get().getName() + ":" + o.get().getId() + ">" : getEmoteText2(ImageUtils.Emotes.UNKNOWN_EMOTE.get());
	}
	private static final String getEmoteText2(String name) {
		Optional<Emote> o = Optional.ofNullable(getEmoteClassByName(name));
		return o.isPresent() ? "<:" + o.get().getName() + ":" + o.get().getId() + ">" : null;
	}
	public static String getEmoteText(Emotes e) {
		return getEmoteText(e.get());
	}
}