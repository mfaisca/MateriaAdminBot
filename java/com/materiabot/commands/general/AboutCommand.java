package com.materiabot.commands.general;
import java.util.concurrent.ExecutionException;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class AboutCommand extends _BaseCommand{	
	public AboutCommand() { super("about", "Shows info about who created this bot."); }

	@Override
	public void doStuff(SlashCommandEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("About MateriaBot", "https://discord.gg/materiabot", event.getJDA().getSelfUser().getAvatarUrl());
		builder.addField("Developer", 
				"**Quetz** - Owner and developer of [MateriaBot](https://discord.gg/XCTC7jY)", false);
		builder.addField("Main Contributors", 
				"**Rem** - Owner of [DissidiaDB](https://dissidiadb.com/) - For giving all the information that the bot is based on, the bot wouldn't be as it is now without him" + System.lineSeparator() +
				"**Dreamy** - Unit Images and Emotes - Friend and designer of the edited character images and most of the bot's custom emotes" + System.lineSeparator() + 
				"", false);
		builder.addField("Friends & Other Contributors", 
				"**Tonberry Troupe** - [Infographs and more](https://www.tonberrytroupe.com/) - Testers of the bot during development, did a lot of ideas bouncing on them to figure out the best way to present information" + System.lineSeparator() + 
				"**Caius**, **Shinri** - Monster Info - These are the guys that provide all the information regarding the monsters" + System.lineSeparator() + 
				"**KupoSlash** - Schedule Updating - Administrator of [Maincord](http://discord.gg/dffoo), he's usually the person that updates and adds all the new content to the $schedule" + System.lineSeparator() + 
				"**JdotA** - Vote Updating - Moderator on TCC server and Subreddit Moderator, he's usually the person that creates the banners for $vote so I don't have to :D" + System.lineSeparator() + 
				"", false);
		builder.addField("Friends & Other Contributors", 
				"**The Crystal Chronicles** - [Podcast](https://thecrystalchronicles.simplecast.com/)" + System.lineSeparator() + 
				"**Macnol** - [Call to Arms and more @ DissidiaInfo](http://dissidiainfo.com/)" + System.lineSeparator() + 
				"**[Vash1306](https://old.reddit.com/user/Vash1306/submitted/)** - \"$timeline\" First Design" + System.lineSeparator() +
				"**[JakeMattAntonio](https://old.reddit.com/user/JakeMattAntonio/submitted/)** - \"$timeline\" Second Design" + System.lineSeparator() +
				"**Animagnam/dommyc** - [OO Tracker](https://ootracker.com/)" + System.lineSeparator() +
				"", false);
		builder.addField("Special Mentions", "All the patrons that monetarily support the bot's cost through [Patreon](https://www.patreon.com/MateriaBot)! Check them out with $patreon", false);
		builder.setFooter("");
		try {
			MessageUtils.sendEmbed(event.getHook(), builder).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}