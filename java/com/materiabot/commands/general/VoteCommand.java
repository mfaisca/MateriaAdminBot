package com.materiabot.commands.general;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.plugface.core.annotations.Plugin;
import com.materiabot.GameElements.Equipment;
import com.materiabot.GameElements.Unit;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.ImageUtils;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.Utils.MessageUtils.Embed;
import com.materiabot.commands._BaseCommand;
import com.materiabot.commands.general.PullCommand.Banner;
import Shared.BotException;
import Shared.Methods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

@Plugin(name = "Command.Vote")
public class VoteCommand extends _BaseCommand{
	private static final long VOTE_CREATOR_ROLE_ID = 894310298620678144L;
	private static final String SKIP_EMOTE = "Pull_Skip";
	private static final String TICKET_EMOTE = "Pull_Tickets";
	private static final String LD_PITY_EMOTE = "Pull_LD";
	private static final String FR_PITY_EMOTE = "Pull_FR";
	private static final String BT_PITY_EMOTE = "Pull_BT";

	public VoteCommand() { super("vote", "Create banner votings (This is a moderator command)"); }
	
	@Override
	public void doStuff(SlashCommandEvent event) {
		//if(Stream.concat(event.getMember().getPermissions().stream(), event.getMember().getPermissions(event.getGuildChannel()).stream())
		if(event.getMember().getPermissions(event.getGuildChannel()).stream().anyMatch(p -> p.equals(Permission.MESSAGE_MANAGE)))
			try {
				switch(event.getSubcommandName()) {
					case "list": {
						EmbedBuilder embed = showBanners();
						MessageUtils.sendEmbed(event.getHook(), embed);
						break;
					}
					case "create": {
						Embed embed = generate(null, event.getGuild().getIdLong(), event.getOption("bannerid").getAsLong());
						if(embed == null) {
							MessageUtils.sendStatusMessageWarn(event.getHook(), "The banner id used doesn't exist or is no longer valid.");
							return;
						}
						TextChannel outputChannel = event.getOption("channel") != null ? (TextChannel)event.getOption("channel").getAsGuildChannel() : event.getTextChannel();
						MessageUtils.sendEmbedToChannel(outputChannel, embed);
						MessageUtils.sendMessage(event.getHook(), "Voting created");
						break;
					}
					case "close":
						closeVoting(event.getOption("banner").getAsLong());
						MessageUtils.sendMessage(event.getHook(), "Voting closed");
						break;
					case "new": 
						if(Constants.userHasMateriaRole(event.getUser().getIdLong(), VOTE_CREATOR_ROLE_ID)) {
							createVoting(event.getOption("unitbt").getAsString(), event.getOption("unit1").getAsString(), event.getOption("unit2").getAsString(), 
									event.getOption("unit3").getAsString(), event.getOption("unitfr") != null ? event.getOption("unitfr").getAsString() : null, 
									event.getOption("imageurl").getAsString());
							MessageUtils.sendStatusMessageInfo(event.getHook(), "Banner has been created!");
						}
						else
							MessageUtils.sendGTFO(event.getHook());
						break;
				}
			} catch(Exception e) {
				MessageUtils.sendMessage(event.getHook(), e.getMessage());
			}
		else
			MessageUtils.sendGTFO(event.getHook(), "Only users with 'Manage Messages' can use this command");
	}
	
	private static Embed generate(Message m, Long guildId, Long bannerHash) throws BotException {
		Embed embed = new Embed();
		try {
			ResultSet rs0 = SQLAccess.executeSelect( //Banner Data
					"SELECT units, status, bannerImage FROM Vote_Banners WHERE bannerHash = ?", bannerHash);
			if(!rs0.next())
				return null;
			boolean status = rs0.getBoolean("status");
			if(!status && m == null)
				return null;
			String bannerImage = rs0.getString("bannerImage"), units = rs0.getString("units");
			ResultSet rs1 = SQLAccess.executeSelect( //TotalCount
				"SELECT vote, conts.serverCount, COUNT(DISTINCT userId) voteCount"
				+ "	FROM Vote_User_Data vud"
				+ "		LEFT JOIN (SELECT bannerHash, units, status, bannerImage"
				+ "							FROM Vote_Banners) status"
				+ "			ON vud.bannerHash = status.bannerHash"
				+ "		LEFT JOIN (SELECT bannerHash, COUNT(DISTINCT serverId) serverCount"
				+ "							FROM Vote_User_Data"
				+ "							GROUP BY bannerHash) conts"
				+ "			ON vud.bannerHash = conts.bannerHash"
				+ "	WHERE vud.bannerHash = ?"
				+ "	GROUP BY vote", bannerHash);
			ResultSet rs2 = SQLAccess.executeSelect( //ServerCount
					"SELECT vud.bannerHash, vud.serverId, vote, COUNT(DISTINCT userId) voteCount"
					+ "	FROM Vote_User_Data vud"
					+ "	WHERE vud.bannerHash = ? AND vud.serverId = ?"
					+ "	GROUP BY vud.bannerHash, vud.serverId, vote", bannerHash, guildId);
			int serverCount = 0;
			int globalSkip = 0, globalTicket = 0, globalLD = 0, globalFR = 0, globalBT = 0;
			int serverSkip = 0, serverTicket = 0, serverLD = 0, serverFR = 0, serverBT = 0;
			while(rs1.next()) {
				serverCount = rs1.getInt("serverCount");
				switch(rs1.getString("vote")) {
				case "SKIP": 	globalSkip += rs1.getInt("voteCount"); break;
				case "TICKET": 	globalTicket += rs1.getInt("voteCount"); break;
				case "LD": 		globalLD += rs1.getInt("voteCount"); break;
				case "FR": 		globalFR += rs1.getInt("voteCount"); break;
				case "BT": 		globalBT += rs1.getInt("voteCount"); break;
				}
			}
			while(rs2.next())
				switch(rs2.getString("vote")) {
				case "SKIP": 	serverSkip += rs2.getInt("voteCount"); break;
				case "TICKET": 	serverTicket += rs2.getInt("voteCount"); break;
				case "LD": 		serverLD += rs2.getInt("voteCount"); break;
				case "FR": 		serverFR += rs2.getInt("voteCount"); break;
				case "BT": 		serverBT += rs2.getInt("voteCount"); break;
				}
			if(!(units.contains(" fr ") || units.contains(" fr,") || units.endsWith(" fr")))
				serverFR = globalFR = -1;
			embed.addField(MessageUtils.S + System.lineSeparator() + "Units", buildUnitField(units), true);
			if(status) {
				embed.setTitle("Pull Poll (" + bannerHash + ")");
				embed.setDescription("Vote what your objective is with this banner." + System.lineSeparator() + "Use '/vote create' if you want to add it to your own server!");
			}
			else
				embed.setTitle("Pull Poll (Closed)");
			embed.addField(MessageUtils.S + System.lineSeparator() + "Server Results", buildVotingField(serverBT, serverFR, serverLD, serverTicket, serverSkip), true);
			embed.addField("On: " + serverCount + " servers" + System.lineSeparator()
							+ "Global Results", buildVotingField(globalBT, globalFR, globalLD, globalTicket, globalSkip), true);
			embed.setImage(bannerImage);
			if(m == null && status) {
				if(serverFR == -1)
					embed.createRow(
							Embed.createButton(ButtonStyle.SECONDARY, "Vote", bannerHash + MessageUtils.SEPARATOR + "SKIP", "Skip", SKIP_EMOTE),
							Embed.createButton(ButtonStyle.SECONDARY, "Vote", bannerHash + MessageUtils.SEPARATOR + "TICKET", "Tickets", TICKET_EMOTE),
							Embed.createButton(ButtonStyle.SECONDARY, "Vote", bannerHash + MessageUtils.SEPARATOR + "LD", "Pity", LD_PITY_EMOTE),
							Embed.createButton(ButtonStyle.SECONDARY, "Vote", bannerHash + MessageUtils.SEPARATOR + "BT", "Pity", BT_PITY_EMOTE));
				else
					embed.createRow(
							Embed.createButton(ButtonStyle.SECONDARY, "Vote", bannerHash + MessageUtils.SEPARATOR + "SKIP", "Skip", SKIP_EMOTE),
							Embed.createButton(ButtonStyle.SECONDARY, "Vote", bannerHash + MessageUtils.SEPARATOR + "TICKET", "Tickets", TICKET_EMOTE),
							Embed.createButton(ButtonStyle.SECONDARY, "Vote", bannerHash + MessageUtils.SEPARATOR + "LD", "Pity", LD_PITY_EMOTE),
							Embed.createButton(ButtonStyle.SECONDARY, "Vote", bannerHash + MessageUtils.SEPARATOR + "FR", "Pity", FR_PITY_EMOTE),
							Embed.createButton(ButtonStyle.SECONDARY, "Vote", bannerHash + MessageUtils.SEPARATOR + "BT", "Pity", BT_PITY_EMOTE));
			}
			else if(m != null && !status)
				embed.setDeleteActions(true);
		} catch (BotException | SQLException e) {
			throw new BotException(e);
		}
		return embed;
	}
	private static String buildUnitField(String unitL) throws BotException {
		Banner b = new Banner();
		for(String u : unitL.split(","))
			b.addUnit(u.trim().toLowerCase());
		String units = "";
		for(Unit u : b.allUnits) {
			units += System.lineSeparator() + ImageUtils.getEmoteText(u.getName()) + 
							"[" + u.getName() + "](https://www.tonberrytroupe.com/infographics/" + Methods.urlizeTT(u.getName()) + ")";
			units += " " + ImageUtils.getEmoteText(u.getCrystal().getEmote(6));
			units += b.btUnits.stream().anyMatch(u2 -> u2.getId() == u.getId()) ? ImageUtils.getEmoteText(Equipment.Rarity.W_BT.getEmoteName()) : "";
			units += b.frUnits.stream().anyMatch(u2 -> u2.getId() == u.getId()) ? ImageUtils.getEmoteText(Equipment.Rarity.W_FR.getEmoteName()) : "";
			units += b.ldUnits.stream().anyMatch(u2 -> u2.getId() == u.getId()) ? ImageUtils.getEmoteText(Equipment.Rarity.W_LD.getEmoteName()) : "";
			units += b.exUnits.stream().anyMatch(u2 -> u2.getId() == u.getId()) ? ImageUtils.getEmoteText(Equipment.Rarity.W_EX.getEmoteName()) : "";
			units = units.trim();
		}
		return units;
	}
	public static String buildVotingField(int bt, int fr, int ld, int t, int s) {
		String ret = "";
		ret += ImageUtils.getEmoteText(BT_PITY_EMOTE) + "Pity: " + bt + System.lineSeparator();
		if(fr != -1)
			ret += ImageUtils.getEmoteText(FR_PITY_EMOTE) + "Pity: " + fr + System.lineSeparator();
		ret += ImageUtils.getEmoteText(LD_PITY_EMOTE) + "Pity: " + ld + System.lineSeparator();
		ret += ImageUtils.getEmoteText(TICKET_EMOTE) + "Tickets: " + t + System.lineSeparator();
		ret += ImageUtils.getEmoteText(SKIP_EMOTE) + "Skip: " + s + System.lineSeparator();
		return ret.trim();
	}
	
	private static EmbedBuilder showBanners() throws BotException, SQLException {
		ResultSet rs = SQLAccess.executeSelect("SELECT * FROM Vote_Banners WHERE status = 1 ORDER BY id ASC");
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription("To create this voting on your server, use the command '/vote create' where <ID> is the title of the columns below.");
		while(rs.next())
			embed.addField(""+rs.getInt("bannerHash"), buildUnitField(rs.getString("units")), true);
		if(embed.getFields().isEmpty())
			embed.addField(MessageUtils.S, "No votings available at the moment", false);
		return embed;
	}
	private static void createVoting(String btUnit, String unit1, String unit2, String unit3, String frUnit, String imageURL) throws BotException {
		String chars = "";
		if(btUnit.equalsIgnoreCase(unit1)) {
			if(btUnit.equalsIgnoreCase(frUnit))
				chars = btUnit + " fr bt ld ex";
			else
				chars = btUnit + " bt ld ex";
		}
		else
			chars = unit1 + " ld ex";
		chars += ", " + unit2 + " ld ex";
		if(!unit3.equalsIgnoreCase("none")) {
			if(frUnit != null)
				chars += ", " + unit3 + " ld ex";
			else
				chars += ", " + unit3 + " ex";
		}
		if(frUnit != null && !frUnit.equalsIgnoreCase(unit1))
			chars = (chars + ", " + frUnit +  " fr").toLowerCase();
		if(btUnit != null && !btUnit.equalsIgnoreCase(unit1))
			chars = (chars + ", " + btUnit +  " bt").toLowerCase();
		Banner b = new Banner();
		for(String c : chars.split(","))
			b.addUnit(c);
		SQLAccess.executeInsert("INSERT INTO Vote_Banners(bannerHash, units, bannerImage) VALUES(?, ?, ?)", b.bannerHash(), chars, imageURL);
	}
	private static void closeVoting(long bannerHash) throws BotException {
		SQLAccess.executeInsert("UPDATE Vote_Banners SET status = 0 WHERE bannerHash = ?", bannerHash);
	}
	
	private static void updateServerData(long bannerHash, long serverId, long userId, String vote) throws BotException {
		SQLAccess.executeInsert("REPLACE INTO Vote_User_Data VALUES(?, ?, ?, ?)", userId, serverId, bannerHash, vote);
		SQLAccess.executeInsert("UPDATE Vote_User_Data SET vote = ? WHERE bannerHash = ? AND userId = ?", vote, bannerHash, userId);
	}
	
	@Override
	public boolean isEtherealReply(SlashCommandEvent event) { return false; }
	@Override
	public boolean isEtherealReply(ButtonClickEvent event) { return false; }
	@Override
	public boolean isEditButton(ButtonClickEvent event) { return false; }
	
	@Override
	public void doStuff(ButtonClickEvent event) {
		String[] args = event.getButton().getId().split(MessageUtils.SEPARATOR);
		Long bannerHash = Long.parseLong(args[1]);
		String vote = args[2];
		try {
			updateServerData(bannerHash, event.getGuild().getIdLong(), event.getUser().getIdLong(), vote);
			MessageUtils.sendMessage(event.getHook(), "Vote Registered");
			MessageUtils.editMessage(event.getMessage(), generate(event.getMessage(), event.getGuild().getIdLong(), bannerHash));
		} catch (BotException e) {
			e.printStackTrace();
		}
	}

	@Override
	public CommandData getAdminCommandData() {
		return new CommandData("admin" + getCommand(), help)
					.addSubcommands(new SubcommandData("list", "List voting options"),
									new SubcommandData("close", "Close a voting")
										.addOptions(new OptionData(OptionType.INTEGER, "banner", "Banner Code", true)),
									new SubcommandData("new", "Create a new banner")
										.addOptions(new OptionData(OptionType.STRING, "unitbt", "BT Unit", true))
										.addOptions(new OptionData(OptionType.STRING, "unit1", "Unit 1 (Write BT Unit again if applicable)", true))
										.addOptions(new OptionData(OptionType.STRING, "unit2", "Unit 2", true))
										.addOptions(new OptionData(OptionType.STRING, "unit3", "Unit 3 ('None' if not applicable)", true))
										.addOptions(new OptionData(OptionType.STRING, "imageurl", "Banner URL", true))
										.addOptions(new OptionData(OptionType.STRING, "unitfr", "FR Unit")));
	}
}