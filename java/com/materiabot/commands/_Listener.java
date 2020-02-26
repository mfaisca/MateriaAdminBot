package com.materiabot.commands;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
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
				for(_BaseCommand c : COMMANDS)
					if(c.validateCommand(event.getMessage()))
						if(c.validatePermission(event.getMessage())) {
							int cd = -1;
							if((cd = CooldownManager.userCooldown(event.getAuthor(), c.getCooldown(event.getMessage()))) == -1)
								c.doStuff(event.getMessage());
							else {
								cd = (cd / 1000) + 1; cd = cd > 5 ? 5 : cd;
								Message m = MessageUtils.sendStatusMessageWarn(event.getChannel(), "Please wait " + cd + " second" + (cd == 1 ? "" : "s") + " to use that command.");
								Constants.sleep(cd * 1000);
								MessageUtils.deleteMessage(m);
							}
							return;
						}
				}
			},
			REACTION_ADDED{public void run(Event e) {
				MessageReactionAddEvent event = (MessageReactionAddEvent)e;
				Message originalMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
				if(!originalMessage.getAuthor().equals(Constants.getClient().getSelfUser()))
					return;
				for(_BaseCommand c : COMMANDS)
					if(c.validateCommand(originalMessage))
						new Thread(() -> c.doAddReactionStuff(event)).start();
				}
			},
			REACTION_REMOVED{public void run(Event e) {
				MessageReactionRemoveEvent event = (MessageReactionRemoveEvent)e;
				Message originalMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
				if(!originalMessage.getAuthor().equals(Constants.getClient().getSelfUser()))
					return;
				for(_BaseCommand c : COMMANDS)
					if(c.validateCommand(originalMessage))
						new Thread(() -> c.doRemoveReactionStuff(event)).start();
				}
			};
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
	public static final List<_BaseCommand> COMMANDS = new LinkedList<_BaseCommand>();
	private static final ThreadPoolExecutor THREAD_MANAGER = (ThreadPoolExecutor)Executors.newCachedThreadPool();

	static {
		BASE_COMMANDS.addAll(Arrays.asList(
				new StatusCommand(),
				new AuthorCommand(),
				new HelpCommand(),
				new PatreonCommand()
				));
		COMMANDS.addAll(BASE_COMMANDS);
		Constants.COMMANDS.addAll(COMMANDS);
	}
	
	public static void unloadPluginCommands() {
		COMMANDS.clear();
		COMMANDS.addAll(BASE_COMMANDS);
		Constants.COMMANDS.clear();
		Constants.COMMANDS.addAll(COMMANDS);
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		//This method exists to update Tonberry Troupe Patreon roles on MateriaBot due to PatreonBot not working with multiple accounts
		if(event.getGuild().equals(Constants.MATERIABOT_SERVER))
			PatreonCommand.joinServerTonberryTroupeUpdate();
	}
	@Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		//This method exists to update Tonberry Troupe Patreon roles on MateriaBot due to PatreonBot not working with multiple accounts
		if(event.getGuild().equals(Constants.MATERIABOT_SERVER))
			PatreonCommand.joinServerTonberryTroupeUpdate();
	}
	
	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.MESSAGE_RECEIVED, event));
	}
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if(event.getUser().isBot()) return;
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.REACTION_ADDED, event));
	}
	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		if(event.getUser().isBot()) return;
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.REACTION_REMOVED, event));
	}
}