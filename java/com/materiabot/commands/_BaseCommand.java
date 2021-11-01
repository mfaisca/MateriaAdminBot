package com.materiabot.commands;
import com.materiabot.Utils.CooldownManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class _BaseCommand{
	public static _BaseCommand CLEVERBOT = null;
	protected String commandName;
	protected String help;
	
	public String getCommand() { return commandName; }

	protected _BaseCommand(String keyword, String help) {
		this.commandName = keyword.toLowerCase();
		this.help = help;
	}
	public static final boolean canPost(SlashCommandEvent event) {
		try {
			if(!event.getChannelType().equals(ChannelType.TEXT))
				return false;
			return event.getGuild().getSelfMember().getPermissions(event.getTextChannel()).stream()
					.anyMatch(p -> Permission.MESSAGE_READ.equals(p));
		} catch(Exception e) {
			return false;
		}
	}
	public boolean isEtherealReply(SlashCommandEvent event) { return false; }
	public boolean isEtherealReply(ButtonClickEvent event) { return false; }
	public boolean isEditButton(ButtonClickEvent event) { return true; }
	public CooldownManager.Type getCooldown(SlashCommandEvent event) { return CooldownManager.Type.REGULAR; }

	public void doStuff(SlashCommandEvent event) {};
	public void doStuff(ButtonClickEvent event) {};
	public void doStuff(SelectionMenuEvent event) {};
	public void doStuff(MessageReceivedEvent message) {};

	public CommandData getCommandData() { return new CommandData(getCommand(), help); };
	public CommandData getAdminCommandData() { return null; };
}