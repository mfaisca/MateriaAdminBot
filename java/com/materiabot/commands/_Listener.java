package com.materiabot.commands;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.materiabot.PluginManager;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.BotException;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.CooldownManager;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import com.materiabot.commands.general.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public class _Listener extends ListenerAdapter{
	private static final class Analyze implements Runnable{
		public static enum Action{
			MESSAGE_RECEIVED{public void run(Event e) {
				MessageReceivedEvent event = (MessageReceivedEvent)e;
				for(Iterator<_BaseCommand> i = Constants.COMMANDS.iterator(); i.hasNext();) {
					_BaseCommand c = i.next();
					if(c.validateCommand(event.getMessage()))
						if(c.validatePermission(event.getMessage())) {
							int cd = -1;
							if((cd = CooldownManager.userCooldown(event.getAuthor(), c.getCooldown(event.getMessage()))) == -1)
								c.doStuff(event.getMessage());
							else {
								cd = (cd / 1000) + 1; cd = cd > 5 ? 5 : cd;
								CompletableFuture<Message> m = MessageUtils.sendStatusMessageWarn(event.getChannel(), "Please wait " + cd + " second" + (cd == 1 ? "" : "s") + " to use that command.");
								Constants.sleep(cd * 1000);
								try {
									MessageUtils.deleteMessage(m.get());
								} catch (InterruptedException | ExecutionException e1) {
									e1.printStackTrace();
								}
							}
							return;
						}
					}
				}
			},
			REACTION_ADDED{public void run(Event e) {
				MessageReactionAddEvent event = (MessageReactionAddEvent)e;
				Message originalMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
				if(!originalMessage.getAuthor().equals(Constants.getClient().getSelfUser()))
					return;
				for(Iterator<_BaseCommand> i = Constants.COMMANDS.iterator(); i.hasNext();) {
					_BaseCommand c = i.next();
					if(c.validateCommand(originalMessage))
						new Thread(() -> c.doAddReactionStuff(event)).start();
				}
			}},
			REACTION_REMOVED{public void run(Event e) {
				MessageReactionRemoveEvent event = (MessageReactionRemoveEvent)e;
				Message originalMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
				if(!originalMessage.getAuthor().equals(Constants.getClient().getSelfUser()))
					return;
				for(Iterator<_BaseCommand> i = Constants.COMMANDS.iterator(); i.hasNext();) {
					_BaseCommand c = i.next();
					if(c.validateCommand(originalMessage))
						new Thread(() -> c.doRemoveReactionStuff(event)).start();
				}
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
	private static final List<_BaseCommand> BASE_COMMANDS = new LinkedList<_BaseCommand>();
	private static final ThreadPoolExecutor THREAD_MANAGER = (ThreadPoolExecutor)Executors.newCachedThreadPool();

	static {
		BASE_COMMANDS.addAll(Arrays.asList(
				new StatusCommand(),
				new AuthorCommand(),
				new HelpCommand(),
				new PatreonCommand(),
				new AdminCommand()
				));
		unloadPluginCommands();
	}
	
	public static void unloadPluginCommands() {
		Constants.COMMANDS.clear();
		Constants.COMMANDS.addAll(BASE_COMMANDS);
		try {
			ResultSet rs = SQLAccess.executeSelect("SELECT * FROM Commands");
			while(rs.next()) {
				UpdateableSimpleCommand cmd = new UpdateableSimpleCommand(rs.getString("name").trim(), rs.getLong("guildId"), rs.getString("data"), rs.getString("owner"), rs.getString("help"));
				Constants.COMMANDS.add(cmd);
			}
		} catch (BotException | SQLException e) {
			MessageUtils.sendWhisper(Constants.QUETZ, "Unable to load simple commands");
			MessageUtils.sendWhisper(Constants.QUETZ, e.getMessage());
		}
	}

	@Override
	public final void onGuildMemberJoin(GuildMemberJoinEvent event) {
		//This method exists to update Tonberry Troupe Patreon roles on MateriaBot due to PatreonBot not working with multiple accounts
		if(event.getGuild().equals(Constants.MATERIABOT_SERVER))
			new Thread(() -> { PatreonCommand.joinServerTonberryTroupeUpdate(); }).start();
	}
	@Override
    public final void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		//This method exists to update Tonberry Troupe Patreon roles on MateriaBot due to PatreonBot not working with multiple accounts
		if(event.getGuild().equals(Constants.MATERIABOT_SERVER))
			new Thread(() -> { PatreonCommand.joinServerTonberryTroupeUpdate(); }).start();
	}
	
	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		//if(event.getAuthor().isBot()) return;
		if(Constants.COMMANDS.isEmpty()) {
			try {
				PluginManager.loadCommands();
				PluginManager.loadUnits();
			} catch(Exception e) {
				System.out.println("Error loading plugins");
			}
		}
		if(event.getAuthor().getIdLong() == Constants.QUETZ_ID)
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.MESSAGE_RECEIVED, event));
	}
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		//if(event.getUser().isBot()) return;
		if(event.getUser().getIdLong() == Constants.QUETZ_ID)
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.REACTION_ADDED, event));
	}
	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		//if(event.getUser().isBot()) return;
		if(event.getUser().getIdLong() == Constants.QUETZ_ID)
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.REACTION_REMOVED, event));
	}
}