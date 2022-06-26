package com.materiabot.Utils;
import java.util.Optional;
import net.dv8tion.jda.api.entities.Emote;

public abstract class ImageUtils {
	public enum Emotes{
		GTFO("gtfo"),
		INVISIBLE("invisible"),
		UNKNOWN_EMOTE("unknownEmote"),
		TONBERRY_TROUPE("tonberrytroupe"),
		
		;private String text;
		
		private Emotes(String s) { text = s; }
		public String get() { return text; }
	}
	
	public static Emote getEmoteClassByName(String name) {
		final String name2 = name.replace(" ", "").replace("'", "").replace("&", "").replace("(", "").replace(")", "");
		if(Constants.getClient() == null) return null;
		Emote emo = Constants.getClient().getGuilds().stream()
				.filter(s -> s.getOwnerIdLong() == Constants.QUETZ2_ID && s.getIdLong() != Constants.MATERIABOT_SERVER_ID && s.getIdLong() != Constants.MATERIABOT_ADMIN_SERVER_ID)
				.flatMap(g -> g.getEmotes().stream())
				.filter(e -> e.getName().equalsIgnoreCase(name2))
				.findFirst()
				.orElse(name2.equalsIgnoreCase(ImageUtils.Emotes.UNKNOWN_EMOTE.get()) ? null : getEmoteClassByName(ImageUtils.Emotes.UNKNOWN_EMOTE.get()));
		return emo;
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