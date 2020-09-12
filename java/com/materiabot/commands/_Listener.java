package com.materiabot.commands;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.materiabot.PluginManager;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.CooldownManager;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands.general.*;
import Shared.BotException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class _Listener extends ListenerAdapter{
	private static final class Analyze implements Runnable{
		public static enum Action{
			MESSAGE_RECEIVED{public void run(Event e) {
					MessageReceivedEvent event = (MessageReceivedEvent)e;
//					try {
						for(_BaseCommand c : Constants.COMMANDS)
							if(c.validateCommand(event.getMessage()))
								if(c.validatePermission(event.getMessage()) || event.getAuthor().getIdLong() == Constants.QUETZ_ID) {
									int cd = -1;
									if((cd = CooldownManager.userCooldown(event.getAuthor(), c.getCooldown(event.getMessage()))) == -1)
											c.doStuff(event.getMessage());
									else {
										cd = (cd / 1000) + 1; 
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
//				} catch(InsufficientPermissionException ee) {
//					MessageUtils.sendWhisper(event.getAuthor().getIdLong(), "Missing Permission on the channel \"" + event.getChannel().getName() + "\"");
//				}
			}
			},
			REACTION_ADDED{public void run(Event e) {
				MessageReactionAddEvent event = (MessageReactionAddEvent)e;
				if(event.getUserIdLong() == event.getJDA().getSelfUser().getIdLong()) return;
				Message originalMessage = null;
				try {
					originalMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
				} catch(InsufficientPermissionException ee) {
					try {
						MessageUtils.sendStatusMessageError(event.getChannel(), "Missing Permission \"Read Message History\". "
								+ "This permission is needed to obtain a message from the past to respond to clickable reactions").join().delete().queueAfter(30, TimeUnit.SECONDS);
					} catch(InsufficientPermissionException eee) {
						try {
							MessageUtils.sendWhisper(event.getGuild().getOwnerIdLong(), "I'm missing the permissions \"Read Message History\" and \"Send Message\". Please add these permissions or the bot cannot work properly");
						}catch(Exception eeee) {
							event.getGuild().leave().queue();
							return;
						}
					}
					return;
				}
				if(originalMessage.getAuthor().getIdLong() != e.getJDA().getSelfUser().getIdLong())
					return;
				if(event.getReaction().getReactionEmote().isEmoji()){
					MessageUtils.removeEmojiReaction(originalMessage, event.getUser(), event.getReactionEmote().getEmoji());
					return;
				}
				if(originalMessage.getReactions().stream()
						.filter(r -> r.getReactionEmote().getIdLong() == event.getReactionEmote().getIdLong())
						.noneMatch(r -> r.isSelf())) {
					MessageUtils.removeReaction(originalMessage, event.getUser(), event.getReactionEmote().getEmote());
					return;
				}
				for(_BaseCommand c : Constants.COMMANDS)
					if(c.validateReaction(originalMessage))
						c.doAddReactionStuff(event);
			}},
			REACTION_REMOVED{public void run(Event e) {
				MessageReactionRemoveEvent event = (MessageReactionRemoveEvent)e;
				if(event.getUserIdLong() == event.getJDA().getSelfUser().getIdLong()) return;
				Message originalMessage = null;
				try {
					originalMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
				} catch(InsufficientPermissionException ee) {
					MessageUtils.sendStatusMessageError(event.getChannel(), "Missing Permission \"Read Message History\". "
							+ "This permission is needed to obtain a message from the past to respond to clickable reactions").join().delete().queueAfter(30, TimeUnit.SECONDS);
					return;
				}
				if(originalMessage.getAuthor().getIdLong() != e.getJDA().getSelfUser().getIdLong())
					return;
				for(_BaseCommand c : Constants.COMMANDS)
					if(c.validateReaction(originalMessage))
						c.doRemoveReactionStuff(event);
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
				new AboutCommand(),
				new HelpCommand(),
				new PatreonCommand(),
				new AdminCommand()
				));
	}
	
	public static void unloadPluginCommands() {
		Constants.COMMANDS.clear();
		Constants.COMMANDS.addAll(BASE_COMMANDS);
		try {
			ResultSet rs = SQLAccess.executeSelect("SELECT * FROM Commands");
			while(rs.next()) {
				if(rs.getBoolean("simple"))
					Constants.COMMANDS.add(new SimpleCommand(rs.getString("name").trim(), rs.getString("data"), rs.getString("help")));
				else
					Constants.COMMANDS.add(new UpdateableSimpleCommand(rs.getString("name").trim(), rs.getLong("guildId"), rs.getString("data"), rs.getString("owner"), rs.getString("help")));
			}
		} catch (BotException | SQLException e) {
			MessageUtils.sendWhisper(Constants.QUETZ_ID, "Unable to load simple commands");
			MessageUtils.sendWhisper(Constants.QUETZ_ID, e.getMessage());
		}
	}

	@Override
	public final void onGuildMemberJoin(GuildMemberJoinEvent event) {
		//This method exists to update Tonberry Troupe Patreon roles on MateriaBot due to PatreonBot not working with multiple accounts
		if(event.getGuild().getIdLong() == Constants.MATERIABOT_SERVER_ID)
			new Thread(() -> { PatreonCommand.joinServerTonberryTroupeUpdate(); }).start();
	}
	@Override
    public final void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		//This method exists to update Tonberry Troupe Patreon roles on MateriaBot due to PatreonBot not working with multiple accounts
		if(event.getGuild().getIdLong() == Constants.MATERIABOT_SERVER_ID)
			new Thread(() -> { PatreonCommand.joinServerTonberryTroupeUpdate(); }).start();
	}
	
	@Override
	public final void onMessageReceived(final MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		if(Constants.COMMANDS.isEmpty()) {
			try {
				PluginManager.loadCommands();
				PluginManager.loadUnits();
			} catch(Exception e) {
				System.out.println("Error loading plugins");
				MessageUtils.sendStatusMessageCrash(event.getChannel(), "Unknown unrecoverable Error. Quetz has been notified.");
				MessageUtils.sendWhisper(Constants.QUETZ_ID, "Error loading plugins.");
				MessageUtils.sendWhisper(Constants.QUETZ_ID, e.getMessage());
			}
		}
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.MESSAGE_RECEIVED, event));
	}
	@Override
	public final void onMessageReactionAdd(MessageReactionAddEvent event) {
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.REACTION_ADDED, event));
	}
	@Override
	public final void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.REACTION_REMOVED, event));
	}
}