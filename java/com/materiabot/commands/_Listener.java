package com.materiabot.commands;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands.general.*;
import Shared.BotException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class _Listener extends ListenerAdapter{
	private static final class Analyze implements Runnable{
		public enum Action{
			SLASH_COMMANDS{public void run(Event e) {
				SlashCommandEvent event = (SlashCommandEvent)e;
				if(_BaseCommand.canPost(event)) {
					for(_BaseCommand c : Constants.COMMANDS)
						if(c.getCommand().equalsIgnoreCase(event.getName().contains("admin") && event.getName().length() > 5 ? event.getName().replace("admin", "") : event.getName())) {
							event.deferReply(c.isEtherealReply(event)).queue();
							c.doStuff(event);
							return;
						}
				}
				else {
					if(event.getChannelType().equals(ChannelType.TEXT))
						event.reply("Cannot reply in this channel due to missing permissions.").queue();
					else if(event.getChannelType().equals(ChannelType.PRIVATE) || event.getChannelType().equals(ChannelType.GROUP))
						event.reply("Using commands through DM's is no longer supported.").queue();
					else if(event.getChannelType().equals(ChannelType.UNKNOWN))
						event.reply("Currently not working in threads.").queue();
					else
						event.reply("Unable to post reply for some unexpected reason.").queue();
				}
			}},
			BUTTON_CLICK{public void run(Event e) {
				ButtonClickEvent event = (ButtonClickEvent)e;
				String cmd = event.getButton().getId().split(MessageUtils.SEPARATOR)[0].trim().toLowerCase();
				for(_BaseCommand c : Constants.COMMANDS)
					if(c.getCommand().equalsIgnoreCase(cmd)) {
						if(c.isEditButton(event))
							event.deferEdit().queue();
						else
							event.deferReply(c.isEtherealReply(event)).queue();
						c.doStuff(event);
						return;
					}
			}},
			SELECT_MENU{public void run(Event e) {
				SelectionMenuEvent event = (SelectionMenuEvent)e;
				String cmd = event.getSelectedOptions().get(0).getValue().split(MessageUtils.SEPARATOR)[0].trim().toLowerCase();
				for(_BaseCommand c : Constants.COMMANDS)
					if(c.getCommand().equals(cmd)) {
						event.deferEdit().queue();
						c.doStuff(event);
						return;
					}
			}},
 			MESSAGE_RECEIVED{
			public void run(Event e) {
 				MessageReceivedEvent event = (MessageReceivedEvent)e;
 				
// 				try {
//					if(!SQLAccess.isRegisteredServer(event.getGuild().getIdLong())){
//						for(_BaseCommand c : Constants.COMMANDS) {
//							if(c.getCommandData() != null)
//								Constants.getClient().getGuildById(event.getGuild().getIdLong()).upsertCommand(c.getCommandData()).submit().exceptionally(ex -> {
//									try {
//										SQLAccess.registerServer(event.getGuild(), "Error");
//									} catch (BotException e1) {
//										// TODO Auto-generated catch block
//										e1.printStackTrace();
//									}
//								    return null;
//								}).thenApply(f -> {
//									if(f != null)
//										try {
//											SQLAccess.registerServer(event.getGuild(), "OK");
//										} catch (BotException e1) {
//											// TODO Auto-generated catch block
//											e1.printStackTrace();
//										}
//									return f;
//								});
//						}
//						System.out.println("Commands given to " + event.getGuild().getName());
//						SQLAccess.registerServer(event.getGuild(), "OK");
//					}
//				} catch (SQLException e1) {
//					MessageUtils.sendWhisper(Constants.QUETZ_ID, "Error assigning Commands to server " + event.getGuild().getName() + "(" + event.getGuild().getIdLong() + ")");
//				} catch (BotException e1) {
//					MessageUtils.sendWhisper(Constants.QUETZ_ID, "Error assigning Commands to server " + event.getGuild().getName() + "(" + event.getGuild().getIdLong() + ")");
//					Member m = event.getGuild().retrieveOwner().complete();
//					String r = null;
//					if(m != null)
//						r = m.getUser().getName() + "#" + m.getUser().getDiscriminator();
//					MessageUtils.sendWhisper(Constants.QUETZ_ID, "The server owner is " + r);
//				}
 				
 				if(event.getAuthor().getIdLong() != event.getJDA().getSelfUser().getIdLong() && 
 					event.getMessage().getContentRaw().startsWith("$") && 
 					event.getMessage().getContentRaw().length() > 1 && Character.isAlphabetic(event.getMessage().getContentRaw().charAt(1))) {
 						MessageUtils.sendMessage(event.getChannel(), "It seems you might be trying to use a MateriaBot command (sorry for the spam if you aren't)" + System.lineSeparator() + 
 																	"Please use the new commands by typing / and following the discord menu. If nothing shows up, please re-add the bot to this server(Admins/Mods) through the following link: " + System.lineSeparator() + 
 																	"https://discord.com/api/oauth2/authorize?client_id=531416878011383808&permissions=139653860416&scope=applications.commands%20bot" + System.lineSeparator() + 
 																	"If nothing shows up after a few minutes, please DM Quetzalma#9999, apologies for the issue!" + System.lineSeparator() + System.lineSeparator() + 
 																"Due to Discord changes, commands have been changed to use the new Slash Command system. Please read more on the links below: " + System.lineSeparator() + 
 																"<https://support.discord.com/hc/en-us/articles/1500000368501-Slash-Commands-FAQ>" + System.lineSeparator() + "<https://support-dev.discord.com/hc/en-us/articles/4404772028055>");
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
	private static final ThreadPoolExecutor THREAD_MANAGER = (ThreadPoolExecutor)Executors.newCachedThreadPool();
	
	public static void unloadPluginCommands() {
		Constants.COMMANDS.clear();
		Constants.COMMANDS.addAll(Arrays.asList(
									new AboutCommand(),
									new PatreonCommand(),
									new AdminCommand(),
									new ScheduleCommand()
									));
		try {
			ResultSet rs = SQLAccess.executeSelect("SELECT * FROM Commands");
			while(rs.next())
				if(rs.getBoolean("simple"))
					Constants.COMMANDS.add(new SimpleCommand(rs.getString("name").split(";;")[0].trim(), rs.getString("data"), rs.getString("help"), rs.getBoolean("subCommand")));
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
		//TODO Make this work for my own patreons as well and ditch PatreonBot
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
	public final void onSelectionMenu(SelectionMenuEvent event) {
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.SELECT_MENU, event));
	}
	@Override
	public final void onMessageReceived(MessageReceivedEvent event) {
		THREAD_MANAGER.execute(new Analyze(Analyze.Action.MESSAGE_RECEIVED, event));
	}
}