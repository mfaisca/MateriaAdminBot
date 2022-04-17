package com.materiabot.commands.general;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.plugface.core.annotations.Plugin;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.IO.Spreadsheet.SpreadsheetLoader;
import com.materiabot.IO.Spreadsheet.SpreadsheetLoader.SpreadsheetBox.SpreadsheetLine;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.ImageUtils;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import com.materiabot.commands.general.MonsterSearchCommand.Stage.Wave.Monster;
import Shared.BotException;
import Shared.Dual;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

@Plugin(name = "Command.Monster")
public class MonsterSearchCommand extends _BaseCommand{	
	public static final long MONSTER_ROLE_ID = 942591617372676166L;
	private static final String SPREADSHEET = "172m8-_yqN0jz2jJLIFoXtZ8JNyhZ9XHRsjFAoAo4pIM";
	private static final List<String> PAGES = Arrays.asList("Arc 1", "Arc 2", "Arc 3"
															, "Lost Chapters", "World of Illusions", "Cycle Quests", "Intersecting Wills"
															//, "2x Cycle Quests", "Mog's Gym", "Abyss", "FEOD / DE (Tower)"
															);
	private static final String SEARCH_QUERY = 
			"SELECT sd.area, sd.nameGL, sd.nameJP, sd.location, sd.hardmode, sd.nodeType, sd.level, wc.waveCount, md.mob, md.mobCount, " + 
			"		(md.mobCount / (if(wc.waveCount < 1, 1, wc.waveCount) - 1)) ratio" + 
			"	FROM Stage_Data sd" + 
			"	INNER JOIN (SELECT stageArea, stageName, hardMode, max(waveNum) waveCount" + 
			"						FROM Stage_Waves" + 
			"						GROUP BY stageArea, stageName, hardMode) wc" + 
			"		ON sd.area = wc.stageArea AND sd.nameJP = wc.stageName AND sd.hardMode = wc.hardMode" + 
			"	INNER JOIN (SELECT stageArea, stageName, hardMode, {t} mob, COUNT(*) mobCount" + 
			"						FROM Stage_Waves" + 
			"						GROUP BY stageArea, stageName, hardMode, {t}" + 
			"						ORDER BY mobCount DESC) md" + 
			"		ON sd.area = md.stageArea AND sd.nameJP = md.stageName AND sd.hardMode = md.hardMode" + 
			"	WHERE mob = ?" + 
			"	ORDER BY {o} DESC, level ASC LIMIT 1";
	private static final String SEARCH_QUERY2 = 
			"SELECT waveNum, slot, monsterName, monsterType"
			+ "	FROM Stage_Waves"
			+ "	WHERE stageName = ? AND hardMode = ?"
			+ "	ORDER BY waveNum ASC, slot ASC";
	
	public static String getSearchQuery(String monster, boolean mobCount, boolean searchType) {
		String order = mobCount ? "mobCount" : "ratio";
		String type = searchType ? "monsterType" : "monsterName";
		return SEARCH_QUERY.replace("{o}", order).replace("{t}", type);
	}
	
	public static class Stage{
		public static class Wave{
			public static class Monster{
				private String name;
				private String type;
				
				public Monster(String n, String t) { name = n; type = t; }
				
				public String getName() { return name; }
				public String getType() { return type; }
			}
			private int waveNumber;
			private List<Dual<String, Monster>> monsters = new LinkedList<Dual<String, Monster>>();

			public int getWaveNumber() { return waveNumber; }
			public List<Dual<String, Monster>> getMonsters() { return monsters; }
		}
		
		private String nameGL, nameJP;
		private String area;
		private String generalLocation;
		private String nodeType;
		private boolean hardMode;
		private int level;
		public String note;
		private List<Wave> waves = new LinkedList<Wave>();
		
		public Stage() {}
		public Stage(ResultSet rs) throws BotException {
			try {
				nameGL = rs.getString("nameGL");
				nameJP = rs.getString("nameJP");
				area = rs.getString("area");
				generalLocation = rs.getString("location");
				nodeType = rs.getString("nodeType");
				hardMode = rs.getBoolean("hardmode");
				level = rs.getInt("level");
				ResultSet rs2 = SQLAccess.executeSelect(SEARCH_QUERY2, nameJP, hardMode);
				rs2.next();
				while(true) {
					note = rs.getString("mobCount") + " targets in " + rs.getString("waveCount") + " waves";
					Wave w = new Wave();
					w.waveNumber = rs2.getInt("waveNum");
					while(w.waveNumber == rs2.getInt("waveNum")) {
						w.monsters.add(new Dual<>(rs2.getString("slot"), new Monster(rs2.getString("monsterName"), rs2.getString("monsterType"))));
						if(!rs2.next()) {
							this.getWaves().add(w);
							throw new BotException("");
						}
					}
					this.getWaves().add(w);
				}
			} catch (SQLException e) {
				throw new BotException(e);
			} catch (BotException e) {
				//OK
			}
		}
		
		public String getNameGL() {
			return nameGL;
		}
		public void setNameGL(String nameGL) {
			this.nameGL = nameGL;
		}
		public String getNameJP() {
			return nameJP;
		}
		public void setNameJP(String nameJP) {
			this.nameJP = nameJP;
		}
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}
		public String getGeneralLocation() {
			return generalLocation;
		}
		public void setGeneralLocation(String generalLocation) {
			this.generalLocation = generalLocation;
		}
		public String getNodeType() {
			return nodeType;
		}
		public void setNodeType(String nodeType) {
			this.nodeType = nodeType;
		}
		public boolean isHardMode() {
			return hardMode;
		}
		public void setHardMode(boolean hardMode) {
			this.hardMode = hardMode;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public List<Wave> getWaves() {
			return waves;
		}
	}
	
	private static String getNodeEmote(String node) {
		if(node.contains("Multi"))
			node = "Multi";
		switch(node) {
		case "Green Node": return ImageUtils.getEmoteText("IronManikin");
		case "Blue Node": return ImageUtils.getEmoteText("SilverManikin");
		case "Gold Node": return ImageUtils.getEmoteText("GoldManikin");
		case "Red Node": return ImageUtils.getEmoteText("BronzeManikin");
		case "Multi": return ImageUtils.getEmoteText("CardManikin");
		}
		return ImageUtils.Emotes.UNKNOWN_EMOTE.get();
	}
	
	private List<Stage> searchMonster(String name){
		Stage big = null, better = null;
		try {
			ResultSet rs = SQLAccess.executeSelect(getSearchQuery(name, true, true), name);
			if(rs.next())
				big = new Stage(rs);
			else {
				rs = SQLAccess.executeSelect(getSearchQuery(name, true, false), name);
				if(rs.next())
					big = new Stage(rs);
			}
			rs = SQLAccess.executeSelect(getSearchQuery(name, false, true), name);
			if(rs.next())
				better = new Stage(rs);
			else {
				rs = SQLAccess.executeSelect(getSearchQuery(name, false, false), name);
				if(rs.next())
					better = new Stage(rs);
			}
		} catch (BotException | SQLException e) {
			return null;
		}
		return Arrays.asList(big, better);
	}
	
	public void updateTables(InteractionHook interactionHook) throws BotException {
		MessageUtils.sendMessage(interactionHook, "Obtaining data from Spreadsheet");
		List<Stage> stages = new LinkedList<>();
		for(String page : PAGES) {
			SpreadsheetLoader.SpreadsheetBox loader = new SpreadsheetLoader.SpreadsheetBox(SPREADSHEET, page);
			for(int idx = 3; idx < loader.getLines().size(); idx += 6) {
				SpreadsheetLine line = loader.getLines().get(idx);
				Stage s = new Stage();
				if(line.getString("A").isEmpty()) 
					break;
				s.setArea(line.getString("A").replace("'", "''"));
				s.setNameJP(line.getString("B").replace("'", "''"));
				s.setNodeType(line.getString("C"));
				if(s.getNameGL() == null && s.getNameJP() == null)
					continue;
				s.setLevel(Integer.parseInt(line.getString("G").split(" ")[1]));
				line = loader.getLines().get(idx+3);
				s.setHardMode(line.getString("A").equals("Hard Mode"));
				s.setNameGL(line.getString("B").replace("'", "''"));
				s.setGeneralLocation(line.getString("C").replace("'", "''"));
				for(char idx2 = 'H'-'A'; ; idx2+=2){
					Stage.Wave w = new Stage.Wave();
					w.waveNumber = (idx2 - ('H'-'A')) / 2 + 1;
					for(int idx3 = 0; idx3 < 3; idx3++) {
						line = loader.getLines().get(idx+idx3);
						if(!line.getString(idx2).isEmpty())
							w.getMonsters().add(new Dual<>(""+ (char)('A' + idx3), new Monster(line.getString(idx2).replace("'", "''"), line.getString(idx2+1).replace("'", "''"))));
					}
					if(w.getMonsters().isEmpty()) break;
					s.getWaves().add(w);
				}
				stages.add(s);
			}
		}
		MessageUtils.sendMessage(interactionHook, "Data read from Spreadsheet, creating SQL strings");
		StringBuilder sbD = new StringBuilder();
		StringBuilder sbW = new StringBuilder();
		for(Stage s : stages) {
			//String stageID = generateId(s);
			sbD.append(", ('" + /*stageID + "', '" +*/ s.getNameJP() + "', '" + s.getNameGL() + "', " +  (s.isHardMode() ? 1 : 0) + ", '" + 
							s.getArea() + "', '" + s.getNodeType() + "', '" + s.getGeneralLocation() + "', " + s.getLevel() + ")");
			for(Stage.Wave w : s.getWaves())
				for(Dual<String, Stage.Wave.Monster> m : w.getMonsters())
					sbW.append(", ('" + s.getArea() + "', '" + s.getNameJP() + "', " + (s.isHardMode() ? 1 : 0) + ", " + w.getWaveNumber() + ", '" + 
									m.getValue1() + "', '" + m.getValue2().getName() + "', '" + m.getValue2().getType() + "')");
		}
		MessageUtils.sendMessage(interactionHook, "Saving to database");
		SQLAccess.executeInsert("DELETE FROM Stage_Waves");
		SQLAccess.executeInsert("DELETE FROM Stage_Data");
		SQLAccess.executeInsert("INSERT INTO Stage_Data VALUES " + sbD.substring(2));
		SQLAccess.executeInsert("INSERT INTO Stage_Waves VALUES " + sbW.substring(2));
		MessageUtils.sendMessage(interactionHook, "All done!");
	}

	private static EmbedBuilder build(List<Stage> stages, String mob) throws BotException {
		if(stages == null) return null;
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Monster Search");
		builder.setDescription("Searching for '" + mob + "'");
		Stage mostTarg = stages.get(0);
		Stage bestRatio = stages.get(1);
		if(mostTarg.getNameJP().equals(bestRatio.getNameJP()))
			bestRatio = null;
		Stage stage = mostTarg;
		builder.addField(MessageUtils.S, ("__" + stage.note + "__"), false);
		builder.addField("Name (Lv. " + stage.getLevel() + ")", 
				(stage.getNameGL() != null ? stage.getNameGL() : "") + System.lineSeparator() + 
				(stage.getNameJP() != null ? stage.getNameJP() : ""), true);
		builder.addField("Location" + " (" + (stage.isHardMode() ? "Hard" : "Normal") + " Mode)", 
				stage.getArea() + System.lineSeparator() + 
				stage.getGeneralLocation() + getNodeEmote(stage.getNodeType()) + "(" + stage.getNodeType() + ")", true);
		if(bestRatio != null) {
			stage = bestRatio;
			builder.addField(MessageUtils.S, ("__" + stage.note + "__"), false);
			builder.addField("Name (Lv. " + stage.getLevel() + ")", 
					(stage.getNameGL() != null ? stage.getNameGL() : "") + System.lineSeparator() + 
					(stage.getNameJP() != null ? stage.getNameJP() : ""), true);
			builder.addField("Location" + " (" + (stage.isHardMode() ? "Hard" : "Normal") + " Mode)", 
					stage.getArea() + System.lineSeparator() + 
					stage.getGeneralLocation() + getNodeEmote(stage.getNodeType()) + "(" + stage.getNodeType() + ")", true);
		}
		builder.setFooter("All credits to Caius, Shinri and Eons for collecting the data." + System.lineSeparator() + 
				"Spreadsheet Link: https://tinyurl.com/y2hevkkx");
		return builder;
	}
	
	public MonsterSearchCommand() { super("monster", "Search best quest to find a specific monster."); }
	
	@Override
	public void doStuff(SlashCommandEvent event) {
		try {
			if(event.getSubcommandName() != null && event.getSubcommandName().equals("update")) {
				boolean isValid = event.getJDA().getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID)
										.retrieveMemberById(event.getUser().getIdLong()).complete()
										.getRoles().stream().anyMatch(r -> r.getIdLong() == MONSTER_ROLE_ID);
				if(!isValid){
					MessageUtils.sendGTFO(event.getHook(), "Only Caius and Shinri can force an update");
					return;
				}
				updateTables(event.getHook());
			}else {
				List<Stage> stages = searchMonster(event.getOption("name").getAsString());
				EmbedBuilder embed;
					embed = build(stages, event.getOption("name").getAsString());
				if(embed == null) {
					MessageUtils.sendStatusMessageInfo(event.getHook(), "No monster info available");
					return;
				}
				MessageUtils.sendEmbed(event.getChannel(), embed);
			}
		} catch (BotException e) {
			e.printStackTrace();
		}
	}

	@Override
	public CommandData getCommandData() {
		return super.getCommandData()
					.addOptions(new OptionData(OptionType.STRING, "name", "Monster Name"));
	}

	@Override
	public CommandData getAdminCommandData() {
		SubcommandData scd = new SubcommandData("update", "Forces an updates on the tables");
		return new CommandData("admin" + getCommand(), help)
				.addSubcommands(scd);
	}
}