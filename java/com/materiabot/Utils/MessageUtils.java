package com.materiabot.Utils;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import Shared.Methods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;

public abstract class MessageUtils {
	public static final class Embed extends EmbedBuilder{
		private List<ActionRow> rows = new LinkedList<>();
		private boolean deleteActions = false;
		
		public boolean isDeleteActions() { return deleteActions; }
		public void setDeleteActions(boolean deleteActions) { this.deleteActions = deleteActions; }
		
		public static Button createEmptyButton(ButtonStyle style) {
			return createButton(style, "donotuse", ""+Methods.RNG.nextInt(500), "", ImageUtils.Emotes.INVISIBLE.get());
		}
		public static Button createButton(ButtonStyle style, String command, String id, String text, String emote) {
			id = command + SEPARATOR + id;
			if(style == null) style = ButtonStyle.SECONDARY;
			if(text != null) {
				if(emote != null)
					return Button.of(style, id, text).withEmoji(Emoji.fromEmote(ImageUtils.getEmoteClassByName(emote)));
				else
					return Button.of(style, id, text);
			}
			return Button.of(style, id, Emoji.fromEmote(ImageUtils.getEmoteClassByName(emote)));
		}
		public static SelectOption createMenuOption(String label, String value) {
			return createMenuOption(label, value, null);
		}
		public static SelectOption createMenuOption(String label, String value, Emoji emote) {
			return SelectOption.of(label, value).withEmoji(emote);
		}
		public Embed createRow(ActionRow row) {
			rows.add(row); 
			return this;
		}
		public Embed createRow(Button... buttons) {
			rows.add(ActionRow.of(buttons)); 
			return this;
		}
		public Embed createMenu(SelectOption... options) {
			createMenu(""+Arrays.hashCode(options), options);
			return this;
		}
		public Embed createMenu(String id, SelectOption... options) {
			rows.add(ActionRow.of(SelectionMenu.create(id).addOptions(options).build()));
			return this;
		}
	}

	/** Invisible Character */
	public static final String E = "‎";
	/** Invisible Space Character */
	public static final String S = "‎　"; //Invisible Space Character
	public static final String SEPARATOR = ";;";
	public static final int DISCORD_MESSAGE_LIMIT = 2000;
	public static final int FIELD_MESSAGE_LIMIT = 1024;
	public static final String NOTEXT = "~~NoText~~";

	private MessageUtils() {}
	
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
	public static final CompletableFuture<Message> sendImage(InteractionHook hook, String text, String imageURL){
		return sendEmbed(hook, new EmbedBuilder().setFooter("").setTitle(text).setImage(imageURL));
	}
	public static final CompletableFuture<Message> sendImage(InteractionHook hook, String imageURL){
		return sendEmbed(hook, new EmbedBuilder().setImage(imageURL));
	}
	public static CompletableFuture<Message> sendGTFO(InteractionHook hook) {
		return sendGTFO(hook, "You don't have permission to use this.");
	}
	public static CompletableFuture<Message> sendGTFO(InteractionHook hook, String msg) {
		Embed embed = new Embed();
		embed.setFooter(msg).setImage(ImageUtils.getEmoteClassByName(ImageUtils.Emotes.GTFO.get()).getImageUrl());
		return sendEmbed(hook, embed);
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
	public static final CompletableFuture<Message> sendEmbedToChannel(MessageChannel channel, EmbedBuilder embed){
		MessageEmbed eb = embed.build();
    	if(eb.getFooter() == null) {
    		String footer = "If you like the bot, consider becoming a Patron:" + System.lineSeparator() + "https://patreon.com/MateriaBot";
    		embed.setFooter(footer, ImageUtils.getEmoteClassByName("patreon").getImageUrl());
    		eb = embed.build();
    	}
    	MessageAction ret = channel.sendMessageEmbeds(eb);
    	if(embed.getClass().equals(Embed.class))
    		ret.setActionRows(((Embed)embed).rows);
    	return ret.submit();
	}
	public static final CompletableFuture<Message> sendEmbed(InteractionHook hook, EmbedBuilder embed){
		MessageEmbed eb = embed.build();
    	if(eb.getFooter() == null) {
    		String footer = "If you like the bot, consider becoming a Patron:" + System.lineSeparator() + "https://patreon.com/MateriaBot";
    		embed.setFooter(footer, ImageUtils.getEmoteClassByName("patreon").getImageUrl());
    		eb = embed.build();
    	}
    	WebhookMessageAction<Message> ret = hook.sendMessageEmbeds(eb);
    	if(embed.getClass().equals(Embed.class))
    		ret.addActionRows(((Embed)embed).rows);
    	return ret.submit();
	}

	public static final CompletableFuture<Message> editMessage(CompletableFuture<Message> message, String msg) {
		return message.thenApply(m -> editMessage(m, msg).join());
	}
	public static final CompletableFuture<Message> editMessage(Message message, String msg) {
		return message.editMessage(msg).submit();
	}
	public static final CompletableFuture<Message> editMessage(CompletableFuture<Message> hook, Embed embed) {
		return hook.thenApply(r -> editMessage(r, embed).join());
	}
	public static final CompletableFuture<Message> editMessage(Message message, Embed embed) {
		MessageEmbed eb = embed.build();
		if(eb.getFooter() == null) {
    		String footer = "If you like the bot, consider becoming a Patron:" + System.lineSeparator() + "https://patreon.com/MateriaBot";
    		embed.setFooter(footer, ImageUtils.getEmoteClassByName("patreon").getImageUrl());
    		eb = embed.build();
    	}
    	MessageAction eb2 = message.editMessageEmbeds(eb);
    	if(embed.isDeleteActions()) {
	    	eb2.override(true);
	    	eb2.setActionRows(ActionRow.of());
    	}
		return eb2.submit();
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