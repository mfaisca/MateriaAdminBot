package com.materiabot.commands;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class SimpleCommand extends _BaseCommand{
	private final String message;
	private final boolean isImage;
	private final Long ownerID;
	private String owner = null;
	
	public SimpleCommand(String command, String message, String help) {
		super(command, help.replace("\n", System.lineSeparator()));
		this.message = message.replace("\n", System.lineSeparator());
		isImage = false;
		ownerID = null;
	}

	public SimpleCommand(String command, String owner, boolean image, String message, String help) {
		super(command, help.replace("\n", System.lineSeparator()));
		this.message = message.replace("\n", System.lineSeparator());
		isImage = image;
		this.ownerID = null;
		this.owner = owner;
	}

	@Override
	public void doStuff(SlashCommandEvent event) {
		if(isImage) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setImage(message);
			if(ownerID != null) {
				User u = Constants.getClient().getUserById(ownerID);
				embed.setFooter(u.getName() + "#" + u.getDiscriminator(), u.getAvatarUrl());
			}
			if(owner != null)
				embed.setFooter(owner);
			MessageUtils.sendEmbed(event.getHook(), embed);
		}else {
			if(ownerID != null) {
				User u = Constants.getClient().getUserById(ownerID);
		        MessageUtils.sendMessage(event.getHook(), message + System.lineSeparator() + "Credits to " + u.getName() + "#" + u.getDiscriminator() + " on Discord.");
			}
			else if(owner != null)
		        MessageUtils.sendMessage(event.getHook(), message + System.lineSeparator() + "Credits to " + owner + ".");
			else
		        MessageUtils.sendMessage(event.getHook(), message);
		}
	}
}
