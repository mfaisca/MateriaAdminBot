package com.materiabot.commands.general;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.plugface.core.annotations.Plugin;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.ImageUtils;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.Utils.MessageUtils.Embed;
import com.materiabot.commands._BaseCommand;
import Shared.BotException;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

@Plugin(name = "Command.Timeline")
public class TimelineCommand extends _BaseCommand{	
	public TimelineCommand() { super("timeline", "Shows the expected timeline of GL."); }

	public Embed build(String timeline) throws BotException {
		try {
			Embed builder = new Embed();
			builder.addField("Other Timelines", 
					ImageUtils.getEmoteText("TonberryTroupe") + " [Tonberry Troupe Timeline](https://www.tonberrytroupe.com/timeline)" + System.lineSeparator() + 
					ImageUtils.getEmoteText("DissidiaDB") + 	" [DissidiaDB Timeline](https://events.dissidiadb.com/)" + System.lineSeparator() + 
					ImageUtils.getEmoteText("FFOOTip") + 	"[FFOOTip Timeline](https://ffootip.com/forecast?region=GL)", false);
			ResultSet rs = SQLAccess.executeSelect("SELECT value FROM Content WHERE name = ?", timeline);
			rs.next();
			builder.setImage(rs.getString("value"));
			if(timeline.equals("vashtimeline")) {
				User u = Constants.getClient().retrieveUserById(533861024835567644L).complete();
				builder.setFooter("Credits to " + u.getName() + "#" + u.getDiscriminator(), u.getAvatarUrl());
			}
			else if(timeline.equals("jakematttimeline"))
				builder.setFooter("Credits to /u/JakeMattAntonio on Reddit", ImageUtils.getEmoteClassByName(ImageUtils.Emotes.UNKNOWN_EMOTE.get()).getImageUrl());
			return builder;
		} catch (SQLException e) {
			throw new BotException(e);
		}
	}
	
	@Override
	public void doStuff(SlashCommandEvent event) {
		try {
			if(event.getOption("update") != null) {
				if(event.getUser().getIdLong() == Constants.QUETZ_ID) {
					SQLAccess.executeInsert("UPDATE Content SET value = ? WHERE name = ?", event.getOption("update").getAsString(), event.getOption("type").getAsString());
					MessageUtils.sendMessage(event.getHook(), "Updated");
				}
				else
					MessageUtils.sendGTFO(event.getHook());
				return;
			}
			
			Embed builder = build(event.getOption("type") != null ? event.getOption("type").getAsString() : "vashtimeline");
			builder.createRow(	Embed.createButton(ButtonStyle.SECONDARY, "Timeline", "vashtimeline", "Vash Timeline", null),
								Embed.createButton(ButtonStyle.SECONDARY, "Timeline", "jakematttimeline", "JakeMatt Timeline", null));
			MessageUtils.sendEmbed(event.getHook(), builder);
		} catch (BotException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isEtherealReply(SlashCommandEvent event) { 
		return event.getOption("update") != null && event.getUser().getIdLong() != Constants.QUETZ_ID;
	}
	@Override
	public void doStuff(ButtonClickEvent event) {
		String[] args = event.getButton().getId().split(MessageUtils.SEPARATOR);
		try {
			MessageUtils.editMessage(event.getMessage(), build(args[1]));
		} catch (BotException e) { ; }
	}

	@Override
	public CommandData getCommandData() {
		return super.getCommandData()
					.addOptions(new OptionData(OptionType.STRING, "type", "Type of Timeline")
						.addChoices(new Command.Choice("Vash Timeline", "vashtimeline"))
						.addChoices(new Command.Choice("JakeMatt Timeline", "jakematttimeline")));
	}

	@Override
	public CommandData getAdminCommandData() {
		return new CommandData("admin" + getCommand(), help)
					.addOptions(new OptionData(OptionType.STRING, "type", "Type of Timeline")
						.addChoices(new Command.Choice("Vash Timeline", "vashtimeline"))
						.addChoices(new Command.Choice("JakeMatt Timeline", "jakematttimeline")))
					.addOptions(new OptionData(OptionType.STRING, "update", "Look at you trying to do things you shouldn't"));
	}
}