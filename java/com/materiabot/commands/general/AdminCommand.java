package com.materiabot.commands.general;
import com.materiabot.PluginManager;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class AdminCommand extends _BaseCommand{
	public AdminCommand() { super("admin", "Pretend this command doesn't exist!"); }

	@Override
	public void doStuff(final SlashCommandEvent event) {
		if(event.getMember().getIdLong() == Constants.QUETZ_ID || event.getMember().getIdLong() == Constants.INK_ID || event.getMember().getIdLong() == Constants.DREAMY_ID)
			try {
				if(event.getOption("op") != null) {
					if(event.getOption("op").getAsString().equals("reload") && event.getMember().getIdLong() == Constants.QUETZ_ID) {
						PluginManager.reset(); 
						MessageUtils.sendMessage(event.getHook(), "Done");
					} else if(event.getOption("op").getAsString().equals("ttpatreon")) {
						PatreonCommand.updateServerTonberryTroupe(); 
						MessageUtils.sendMessage(event.getHook(), "Done");
					}
					else
						MessageUtils.sendStatusMessageError(event.getHook(), "You fucked up");
				} else if(event.getOption("patreonkey") != null) {
					if(PatreonCommand.updatePatreonKey(event.getMember(), event.getOption("patreonkey").getAsString()))
						MessageUtils.sendMessage(event.getHook(), "Patreon API Key Updated");
					else
						MessageUtils.sendStatusMessageError(event.getHook(), "Error updating Patreon API Key");
				} else if(event.getOption("status") != null && event.getMember().getIdLong() == Constants.QUETZ_ID) {
					Activity a = null;
					switch(event.getOption("status").getAsString()) {
						case "playing": a = Activity.playing("Opera Omnia"); break;
						case "servers": a = Activity.watching("Watching " + Constants.getClient().getGuilds().size() + " servers"); break;
						case "stream": a = Activity.streaming("DFFOO Livestream", "https://www.twitch.tv/squareenix"); break;
					}
					if(a != null) {
						Constants.getClient().getPresence().setPresence(OnlineStatus.ONLINE, a);
						MessageUtils.sendMessage(event.getHook(), "Status updated to " + a.getName());
					}else
						MessageUtils.sendStatusMessageError(event.getHook(), "Something went wrong with changing status");
				}
				else
					MessageUtils.sendStatusMessageError(event.getHook(), "You fucked up");
			} catch(Exception e) {
				MessageUtils.sendStatusMessageError(event.getHook(), "You fucked up");
			}
		else
			MessageUtils.sendGTFO(event.getHook());
	}
	
	@Override
	public CommandData getCommandData() {
		return null;
	}
	
	@Override
	public CommandData getAdminCommandData() {
		CommandData cd = new CommandData("admin", help);
			OptionData od2 = new OptionData(OptionType.STRING, "op", "Operation");
				od2.addChoices(new Command.Choice("reload", "reload"));
				od2.addChoices(new Command.Choice("ttpatreon", "ttpatreon"));
			OptionData od3 = new OptionData(OptionType.STRING, "patreonkey", "Insert Patreon Key Here");
			OptionData od4 = new OptionData(OptionType.STRING, "status", "MateriaBot status");
				od4.addChoices(new Command.Choice("playing", "playing"));
				od4.addChoices(new Command.Choice("servers", "servers"));
				od4.addChoices(new Command.Choice("stream", "stream"));
		cd.addOptions(od2);
		cd.addOptions(od3);
		cd.addOptions(od4);
		return cd;
	}
	
//	private void onetimemessagetoallserverowners() {
//		String messageToSend = "Hello Server Owner\r\n" + 
//				"I apologize for DMing, and I promise this will be the only DM sent. Please share this message with your server/community.\r\n" + 
//				"\r\n" + 
//				"Hi, I'm Quetzalma, the developer of MateriaBot. I'm so happy that people have been using my bot and I hope it has helped everyone, both in utility and in fun with the pull command that everyone loves to use. I've been developing and maintaining MateriaBot for over 2 years now and I'm so glad that the community likes it as much as they do. I owe a big thank you to everyone!\r\n" + 
//				"\r\n" + 
//				"For the last 4~5 months I've been working on a new bot to replace the old code that MateriaBot had, I'm finally done with it and as of receiving this message, the bot has already been updated.\r\n" + 
//				"__**You don't have to do anything, the new bot replaced the old one so everything is working smoothly.**__\r\n" + 
//				"\r\n" + 
//				"If you want to know more about the new bot, please check this link where I explain everything: https://old.reddit.com/r/DissidiaFFOO/comments/iaf4o7/materiabot_v2_on_discord/\r\n" + 
//				"If there's any question you might have regarding the bot, I'll answer any and all questions, just DM me on Quetzalma#9999 or join my Discord: <https://discord.gg/XCTC7jY>\r\n\r\n" + 
//				"**As a server owner, I want to bring extra attention to one of the new features that the bot has, the Vote Command**, I'll copy-paste what I wrote on the subreddit thread, but its better to read it there as it contains more images showing what it does visually.";
//		String messageToSend2 = "\r\n" + 
//				"\r\n" + 
//				"This is a very experimental feature due to the sheer number of people that might end up using it, and me being very limited during testing. For the people that visit TCC Podcast Discord or have listened to it, you'll know that they have a [Poll] on their server for every banner, this serves to gauge the interest of their community for discussion during the podcast. Initially I had a plan to simply remake it again and leave it as is, but during testing of the bot, some other server owners showed interest in having a similar thing in their server, with that information I changed things up a bit and made it a global thing!\r\n" + 
//				"Server Moderators(Manage Message permission) will be able to create votings on their servers using a code*(I'll explain below)*, all the votings will be stored on a shared database so that you can both see your server opinions/plans on a server, but also the plans of everyone that voted on all servers, thus giving an even bigger picture for all the content creators that wish to use this information as a talking point or whatever.\r\n" + 
//				"**How to Use:**\r\n" + 
//				"Type \"$vote list\" and you'll see a [list of banners], along with its ID(bolded above the units), that ID is what identifies that specific banner in order to connect it with every other banner. After you have the ID you type \"$vote <ID>\" (without the <>), see [this image] and the bot will create the vote, notice that even though I just created it, that the Global Results already have the votings I made on 2 other servers and that the voting is active on 3 servers. These votings serve to represent what your plan or expectation is, not to say what you actually got, as such, each person can only vote once per banner and the voting will close shortly after its released on the game, removing the voting options.";
//		
//		List<User> l = Constants.getClient().getGuilds().stream().skip(4)
//			.map(g -> g.retrieveOwner().complete().getUser())
//			.distinct()
//			.collect(Collectors.toList());
//		for(User o : l) {
//			try {
//				System.out.println("Sending to " + o.getName());
//				MessageUtils.sendWhisper(o.getIdLong(), messageToSend).join();
//				MessageUtils.sendWhisper(o.getIdLong(), messageToSend2).join();
//			} catch(Exception e) {
//				System.out.println("Error sending to user ID: " + o);
//			}
//		}
//	}
}