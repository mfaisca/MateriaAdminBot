package com.materiabot.Utils;
import java.util.concurrent.CompletableFuture;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;

public abstract class MessageUtils {
	public enum DefaultMessages{
		CHAR_NOT_FOUND("Character Not Found Error"),
		PASSIVE_NOT_FOUND("Passive Not Found Error"),
		UNKNOWN_ERROR("Unknown Error - Please contact Quetz!"),
		;

		private String msg;

		private DefaultMessages(String msg) { this.msg = msg; }
		public String getMessage() { return msg; }
	}

	public static final String E = "‎";
	public static final String S = "‎　";
	public static final int DISCORD_MESSAGE_LIMIT = 1900;

	public static final String empty(int l) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < l; i++)
			sb.append(S);
		return sb.toString();
	}
	public static final String tab() {
		return empty(1);
	}

	public static final CompletableFuture<Message> sendStatusMessageError(InteractionHook hook, String message) {
		return sendMessage(hook, ":no_entry: | " + message);
	}
	public static final CompletableFuture<Message> sendStatusMessageCrash(InteractionHook hook, String message) {
		return sendMessage(hook, ":ambulance: | " + message);
	}
	public static final CompletableFuture<Message> sendStatusMessageInfo(InteractionHook hook, String message) {
		return sendMessage(hook, ":information_source: | " + message);
	}
	public static final CompletableFuture<Message> sendStatusMessageWarn(InteractionHook hook, String message) {
		return sendMessage(hook, ":warning: | " + message);
	}

	public static final CompletableFuture<Message> sendWhisper(Long userId, String message){
		return Constants.getClient().retrieveUserById(userId).complete().openPrivateChannel().submit().thenApply(r -> r.sendMessage(message).complete());
	}
	public static final CompletableFuture<Message> sendMessage(InteractionHook hook, String message){
		return hook.sendMessage(message).submit();
	}
	public static final CompletableFuture<Message> sendMessageToChannel(MessageChannel channel, String message){
		return channel.sendMessage(message).submit();
	}
	public static final CompletableFuture<Message> sendImage(InteractionHook hook, String imageURL){
		return sendEmbed(hook, new EmbedBuilder().setImage(imageURL));
	}
	public static final CompletableFuture<Message> sendFile(InteractionHook hook, String name, byte[] f) {
		return hook.sendFile(f, name).submit();
	}
	public static final CompletableFuture<Message> sendEmbed(InteractionHook hook, String message, String emote) {
		EmbedBuilder builder = new EmbedBuilder();
		if(emote != null)
			builder.setThumbnail(emote);
		builder.setDescription(message);
		return sendEmbed(hook, builder);
	}
	public static final CompletableFuture<Message> sendEmbed(InteractionHook hook, EmbedBuilder embed){
		MessageEmbed eb = embed.build();
    	if(eb.getFooter() == null) {
    		String footer = "If you like the bot, consider becoming a Patron:" + System.lineSeparator() + "https://patreon.com/MateriaBot";
    		embed.setFooter(footer, ImageUtils.getEmoteClassByName("patreon").getImageUrl());
    		eb = embed.build();
    	}
    	MessageEmbed eb2 = eb;
    	return hook.sendMessageEmbeds(eb2).submit();
	}

	public static final CompletableFuture<Message> editMessage(CompletableFuture<Message> message, String msg) {
		return message.thenApply(m -> m.editMessage(msg).complete());
	}
	public static final CompletableFuture<Message> editMessage(CompletableFuture<Message> hook, EmbedBuilder embed) {
		MessageEmbed eb = embed.build();
		if(eb.getFooter() == null) {
    		String footer = "If you like the bot, consider becoming a Patron:" + System.lineSeparator() + "https://patreon.com/MateriaBot";
    		embed.setFooter(footer, ImageUtils.getEmoteClassByName("patreon").getImageUrl());
    		eb = embed.build();
    	}
    	MessageEmbed eb2 = eb;
		return hook.thenApply(r -> r.editMessage(eb2).complete());
	}

	public static final CompletableFuture<Message> addReactions(CompletableFuture<Message> hook, String... reactions) {
		return hook.thenApply(r -> {
			for(String emote : reactions)
				r.addReaction(ImageUtils.getEmoteClassByName(emote)).queue();
			return r;
		});
	}

	public static void removeUserReactions(CompletableFuture<Message> hook, User user) {
		hook.thenApply(r -> {
			if(user == null)
				r.clearReactions().queue();
			else
				for(MessageReaction mr : r.getReactions())
					r.removeReaction(mr.getReactionEmote().getEmote(), user).queue();
			return r;
		});
	}
	public static void removeReaction(CompletableFuture<Message> hook, User user, Emote reaction) {
		hook.thenApply(r -> {
			if(reaction == null)
				r.clearReactions().queue();
			else if(user == null)
				r.removeReaction(reaction).queue();
			else
				r.removeReaction(reaction, user).queue();
			return r;
		});
	}
	public static void removeEmojiReaction(CompletableFuture<Message> hook, User user, String emoji) {
		hook.thenApply(r -> {
			if(emoji == null)
				r.clearReactions().queue();
			else if(user == null)
				r.removeReaction(emoji).queue();
			else
				r.removeReaction(emoji, user).queue();
			return r;
		});
	}
}