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
				"**Rem** - Owner of [DissidiaDB](https://dissidiadb.com/)" + System.lineSeparator() +
				"**Dreamy** - Unit Images and Emotes" + System.lineSeparator() + 
				"", false);
		builder.addField("Friends & Other Contributors", 
				"**Tonberry Troupe** - [Infographs and more](https://www.tonberrytroupe.com/)" + System.lineSeparator() + 
				"**Caius**, **Shinri** - Monster Info" + System.lineSeparator() + 
				"**KupoSlash** - Schedule Updating" + System.lineSeparator() + 
				"**JdotA** - Vote Updating" + System.lineSeparator() + 
				"", false);
		builder.addField("Friends & Other Contributors", 
				"**The Crystal Chronicles** - [Podcast](https://thecrystalchronicles.simplecast.com/)" + System.lineSeparator() + 
				"**Macnol** - [Call to Arms and more @ DissidiaInfo](http://dissidiainfo.com/)" + System.lineSeparator() + 
				"**[Vash1306](https://old.reddit.com/user/Vash1306/submitted/)** - \"/timeline\" First Design" + System.lineSeparator() +
				"**[JakeMattAntonio](https://old.reddit.com/user/JakeMattAntonio/submitted/)** - \"/timeline\" Second Design" + System.lineSeparator() +
				"**Animagnam/dommyc** - [OO Tracker](https://ootracker.com/)" + System.lineSeparator() +
				"", false);
		builder.addField("Special Mentions", "All the patrons that monetarily support the bot's cost through [Patreon](https://www.patreon.com/MateriaBot)! Check them out with /patreon", false);
		builder.setFooter("");
		try {
			MessageUtils.sendEmbed(event.getHook(), builder).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}