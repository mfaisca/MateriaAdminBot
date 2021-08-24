package com.materiabot.commands;
import com.materiabot.Utils.CooldownManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class _BaseCommand{
	public static _BaseCommand CLEVERBOT = null;
	protected String commandName;
	protected boolean materiaOnly = false;
	protected String help;
	
	public String getCommand() { return commandName; }

	protected _BaseCommand(String keyword, String help) { this(keyword, help, false); }
	protected _BaseCommand(String keyword, String help, boolean materiaOnly) {
		this.commandName = keyword;
		this.help = help;
		this.materiaOnly = materiaOnly;
	}
	public boolean isMateriaOnly() { return materiaOnly; }
	public CooldownManager.Type getCooldown(final Message message) { return CooldownManager.Type.REGULAR; }

	public void doStuff(SlashCommandEvent event) {};
	public void doStuff(ButtonClickEvent event) {};
	public void doStuff(MessageReceivedEvent message) {};

	public CommandData getCommandData() { return new CommandData(getCommand(), help); };
}