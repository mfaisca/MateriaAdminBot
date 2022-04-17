package com.materiabot.commands.general;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.plugface.core.annotations.Plugin;
import com.materiabot._Library;
import com.materiabot.GameElements.Unit;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.ImageUtils;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import Shared.BotException;
import Shared.Methods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

@Plugin(name = "Command.Infograph")
public class InfographCommand extends _BaseCommand{
	public static final long TONBERRY_TROUPE_ROLE_ID = 894310411288064041L;
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final class Infograph{
		public int id;
		public String name, link;
		public Date lastUpdated;
		public boolean random;
	}
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy"); //THE RIGHT WAY!!!!!
	//private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd"); //THE OK WAY
	//private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");
	public InfographCommand() { super("infograph", "Shows an infograph"); }

	private static EmbedBuilder build(Unit u) {
		Infograph infograph = getInfographURL(u.getName());
		if(infograph == null) return null;
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("Tonberry Troupe Website (Link)", 
				"https://www.tonberrytroupe.com/infographics/" + Methods.urlizeTT(u.getName()).toLowerCase(), 
				ImageUtils.getEmoteClassByName("TonberryTroupe").getImageUrl());
		builder.setColor(u.getCrystal().getColor());
		builder.setTitle("**Last Updated: **" +  StringUtils.capitalize(DATE_FORMAT.format(infograph.lastUpdated)));
		builder.setImage(infograph.link);
		builder.setFooter("If you like these, check how to support them on:" + System.lineSeparator() + 
							"https://www.tonberrytroupe.com/behind-the-scenes", ImageUtils.getEmoteClassByName(ImageUtils.Emotes.TONBERRY_TROUPE.get()).getImageUrl());
		return builder;
	}
	private static EmbedBuilder build(String s) {
		Infograph infograph = getInfographURL(s.toLowerCase());
		if(infograph == null) return null;
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("Tonberry Troupe Website (Link)", 
				"https://www.tonberrytroupe.com/home", 
				ImageUtils.getEmoteClassByName("TonberryTroupe").getImageUrl());
		builder.setTitle("**Last Updated: **" +  StringUtils.capitalize(DATE_FORMAT.format(infograph.lastUpdated)));
		builder.setImage(infograph.link);
		builder.setFooter("If you like these, check how to support them on:" + System.lineSeparator() + 
				"https://www.tonberrytroupe.com/behind-the-scenes", ImageUtils.getEmoteClassByName(ImageUtils.Emotes.TONBERRY_TROUPE.get()).getImageUrl());
		return builder;
	}
	private static EmbedBuilder buildList() {
		List<Infograph> infograph = getInfographs();
		if(infograph == null) return null;
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("Tonberry Troupe Website (Link)", 
				"https://www.tonberrytroupe.com/home", 
				ImageUtils.getEmoteClassByName("TonberryTroupe").getImageUrl());
		builder.addField("Non-Unit Infographs:", infograph.stream()
				.map(i -> i.name)
				.reduce((i1, i2) -> i1 + System.lineSeparator() + i2)
				.orElse("---"), false);
		builder.setFooter("If you like these, check how to support them on:" + System.lineSeparator() + 
				"https://www.tonberrytroupe.com/behind-the-scenes", ImageUtils.getEmoteClassByName(ImageUtils.Emotes.TONBERRY_TROUPE.get()).getImageUrl());
		return builder;
	}
	private static String saveInfograph(String unit, String link) throws BotException {
		Infograph old = getInfographURL(unit);
		int random = _Library.L.getUnit(unit) != null ? 0 : 1;
		String date = FORMATTER.format(new Date());
		if(link.equalsIgnoreCase("delete"))
			SQLAccess.executeInsert("DELETE FROM TT_Infographs WHERE unit = ? AND type = ?", unit, "TT");
		else
			SQLAccess.executeInsert("REPLACE INTO TT_Infographs(lastUpdated, unit, type, link, random) VALUES(?, ?, ?, ?, ?)", date, unit, "TT", link, random);
		return old != null ? old.link : null;
	}

	public static Infograph getInfographURL(String name) {
		try(ResultSet rs = SQLAccess.executeSelect("SELECT * FROM TT_Infographs WHERE unit = ?", name)){
			if(rs.next()) {
				Infograph ig = new Infograph();
				ig.id = rs.getInt("id");
				ig.name = rs.getString("unit");
				ig.link = rs.getString("link");
				ig.lastUpdated = rs.getDate("lastUpdated");
				ig.random = rs.getBoolean("random");
				return ig;
			}
			return null;
		} catch (BotException | SQLException e) {
			return null;
		}
	}
	public static List<Infograph> getInfographs() {
		List<Infograph> ret = new LinkedList<>();
		try(ResultSet rs = SQLAccess.executeSelect("SELECT * FROM TT_Infographs WHERE random = 1")){
			while(rs.next()) {
				Infograph ig = new Infograph();
				ig.id = rs.getInt("id");
				ig.name = rs.getString("unit");
				ig.link = rs.getString("link");
				ig.lastUpdated = rs.getDate("lastUpdated");
				ig.random = rs.getBoolean("random");
				ret.add(ig);
			}
			return ret;
		} catch (BotException | SQLException e) {
			return null;
		}
	}
	
//	@Override
//	public boolean isEtherealReply(SlashCommandEvent event) {
//		return event.getSubcommandName().equals("save") && !Constants.userHasMateriaRole(event.getIdLong(), TONBERRY_TROUPE_ROLE_ID);
//	}

	@Override
	public void doStuff(SlashCommandEvent event) {
		EmbedBuilder embed = null;
		switch(event.getSubcommandName()) {
			case "list": {
				embed = buildList();
				break;
			}
			case "show": {
				String name = event.getOption("name").getAsString();
				Unit u = _Library.L.getQuickUnit(name);
				if(u != null) //Unit IG
					embed = build(u);
				else //Non-unit IG
					embed = build(name);
				break;
			}
			case "save": {
				boolean isTTMember = event.getJDA().getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID)
										.retrieveMemberById(event.getUser().getIdLong()).complete()
										.getRoles().stream().anyMatch(r -> r.getIdLong() == TONBERRY_TROUPE_ROLE_ID);
				if(!isTTMember){
					MessageUtils.sendGTFO(event.getHook(), "Only Tonberry Troupe members can update infographs");
					return;
				}
				String name = event.getOption("name").getAsString();
				String url = event.getOption("url").getAsString();
				try {
					String oldIG = saveInfograph(name, url);
					MessageUtils.sendMessage(event.getHook(), (oldIG != null ? "Old link for reference: <" + oldIG + ">" : "") 
											+ System.lineSeparator() + "Infograph " + (url.equalsIgnoreCase("delete") ? "Deleted" : "Updated"));
					return;
				} catch (BotException e) {
					MessageUtils.sendStatusMessageError(event.getHook(), "Error saving IG");
				}
				break;
			}
		}
		if(embed != null)
			MessageUtils.sendEmbed(event.getHook(), embed);
		else
			MessageUtils.sendStatusMessageWarn(event.getHook(), "No infograph is available");
	}

	@Override
	public CommandData getCommandData() {
		return super.getCommandData()
					.addSubcommands(new SubcommandData("list", "List infographs"),
									new SubcommandData("show", "Show infograph")
										.addOptions(new OptionData(OptionType.STRING, "name", "Infograph Name", true)));
	}

	@Override
	public CommandData getAdminCommandData() {
		return new CommandData("admin" + getCommand(), help)
					.addSubcommands(new SubcommandData("list", "List infographs"),
									new SubcommandData("save", "Save infograph (Only useable by Tonberry Troupe members)")
										.addOptions(new OptionData(OptionType.STRING, "name", "Infograph Name", true))
										.addOptions(new OptionData(OptionType.STRING, "url", "Image URL (write \"delete\" to remove the IG)", true)),
									new SubcommandData("show", "Show infograph")
										.addOptions(new OptionData(OptionType.STRING, "name", "Infograph Name", true)));
	}
}