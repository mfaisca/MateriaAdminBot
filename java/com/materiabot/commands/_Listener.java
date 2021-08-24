package com.materiabot.commands;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import com.materiabot.PluginManager;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands.general.*;
import Shared.BotException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class _Listener extends ListenerAdapter{
	private static final class Analyze implements Runnable{
		public enum Action{
			SLASH_COMMANDS{public void run(Event e) {
				SlashCommandEvent event = (SlashCommandEvent)e;
				for(_BaseCommand c : Constants.COMMANDS)
					if(c.getCommand().equals(event.getName())) {
						event.deferReply().queue();
						c.doStuff(event);
						return;
					}
			}},
			BUTTON_CLICK{public void run(Event e) {
				ButtonClickEvent event = (ButtonClickEvent)e;
				
			}},
			MESSAGE_RECEIVED{public void run(Event e) {
				MessageReceivedEvent event = (MessageReceivedEvent)e;
				if(!event.getMessage().getContentRaw().isEmpty())
					if(event.isFromType(ChannelType.PRIVATE))
						MessageUtils.sendWhisper(event.getAuthor().getIdLong(), "Using commands through DM's is no longer supported.");
					if(_BaseCommand.CLEVERBOT != null)
						_BaseCommand.CLEVERBOT.doStuff(event);
			}};
			public abstract void run(final Event event);
		}
		
		private final Action action;
		private final Event event;

		public Analyze(Action a, final Event event) {
			action = a;
			this.event = event;
		}

		@Override
		public void run() {
			action.run(event);
		}
	}
	private static final ThreadPoolExecutor THREAD_MANAGER = (ThreadPoolExecutor)Executors.newCachedThreadPool();
	
	public static void unloadPluginCommands() {
		Constants.COMMANDS.clear();
		Constants.COMMANDS.addAll(Arrays.asList(
									new AboutCommand(),
									new PatreonCommand(),
									new AdminCommand()
									));
		try {
			ResultSet rs = SQLAccess.executeSelect("SELECT * FROM Commands");
			while(rs.next())
				if(rs.getBoolean("simple"))
					Constants.COMMANDS.add(new SimpleCommand(rs.getString("name").split(";;")[0].trim(), rs.getString("data"), rs.getString("help")));
				else
					Constants.COMMANDS.add(new UpdateableSimpleCommand(rs.getString("name").split(";;")[0].trim(), rs.getString("data"), rs.getString("owner"), rs.getString("help")));
		} catch (BotException | SQLException e) {
			MessageUtils.sendWhisper(Constants.QUETZ_ID, "Unable to load simple commands");
			MessageUtils.sendWhisper(Constants.QUETZ_ID, e.getMessage());
		}
	}

	@Override
	public final void onGuildMemberJoin(GuildMemberJoinEvent event) {
		//This method exists to update Tonberry Troupe Patreon roles on MateriaBot due to PatreonBot not working with multiple accounts
		if(event.getGuild().getIdLong() == Constants.MATERIABOT_SERVER_ID)
			new Thread(() -> PatreonCommand.updateServerTonberryTroupe()).start();
	}
	@Override
    public final void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		//This method exists to update Tonberry Troupe Patreon roles on MateriaBot due to PatreonBot not working with multiple accounts
		if(event.getGuild().getIdLong() == Constants.MATERIABOT_SERVER_ID)
			new Thread(() -> PatreonCommand.updateServerTonberryTroupe()).start();
	}

	@Override
	public final void onSlashCommand(SlashCommandEvent event) {
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.SLASH_COMMANDS, event));
	}
	@Override
	public final void onButtonClick(ButtonClickEvent event) {
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.BUTTON_CLICK, event));
	}
	@Override
	public final void onMessageReceived(final MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		if(Constants.COMMANDS.isEmpty()) {
			try {
				PluginManager.loadCommands();
				PluginManager.loadUnits();
			} catch(Exception e) {
				MessageUtils.sendWhisper(Constants.QUETZ_ID, "Error loading plugins.");
				MessageUtils.sendWhisper(Constants.QUETZ_ID, e.getMessage());
			}
		}
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.MESSAGE_RECEIVED, event));
	}
}