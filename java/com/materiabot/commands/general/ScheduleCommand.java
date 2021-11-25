package com.materiabot.commands.general;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.plugface.core.annotations.Plugin;
import com.materiabot.GameElements.Event;
import com.materiabot.GameElements.Region;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.ImageUtils;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.Utils.MessageUtils.Embed;
import com.materiabot.commands._BaseCommand;
import Shared.Methods;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

@Plugin(name = "Command.Schedule")
public class ScheduleCommand extends _BaseCommand{
	private static final long SCHEDULE_CREATOR_ROLE_ID = 894310346540609568L;

	public ScheduleCommand() { super("schedule", "Show the schedule of the month"); }
	
	@Override
	public void doStuff(SlashCommandEvent event) {
		if(Constants.userHasMateriaRole(event.getUser().getIdLong(), SCHEDULE_CREATOR_ROLE_ID))
			try {
				if(event.getSubcommandName() == null){
					boolean full = event.getOption("full") != null ? event.getOption("full").getAsBoolean() : false;
					MessageUtils.sendEmbed(event.getHook(), show("GL", false, false, full));
				}
				else switch(event.getSubcommandName()) {
					case "list": {
						boolean full = event.getOption("full") != null ? event.getOption("full").getAsBoolean() : false;
						MessageUtils.sendEmbed(event.getHook(), show("GL", false, false, full));
						break;
					}
					case "delete": {
						String eventname = event.getOption("event").getAsString();
						if(SQLAccess.Event.deleteEvent(eventname, "GL"))
							MessageUtils.sendStatusMessageInfo(event.getHook(), "Event deleted!");
						else
							MessageUtils.sendStatusMessageWarn(event.getHook(), "Event doesn't exist!");
						break;
					}
					case "new": {
						Event e = new Event();
						e.setName(event.getOption("event").getAsString());
						e.setRegion(Region.GL.name());
						try {
							e.setStartDate(Timestamp.valueOf(ZonedDateTime.parse(event.getOption("startdate").getAsString().trim(), 
																DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm z")).toLocalDateTime()));
						} catch(DateTimeParseException ex) {
							e.setStartDate(Timestamp.valueOf(ZonedDateTime.parse(event.getOption("startdate").getAsString().trim() + " 02:00 GMT", 
									DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm z")).toLocalDateTime()));
						}
						e.setEndDate(Timestamp.valueOf((
							event.getOption("enddate") != null ? 
								ZonedDateTime.parse(event.getOption("enddate").getAsString().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm z")) : 
								ZonedDateTime.ofInstant(e.getStartDate().toInstant().plus(14, ChronoUnit.DAYS), ZoneId.of("GMT"))
							).toLocalDateTime()));
						e.getUnits().addAll(Arrays.asList(event.getOption("unit1"), event.getOption("unit2"),
															event.getOption("unit3"), event.getOption("unit4"),
															event.getOption("unit5"), event.getOption("unit6")).stream().filter(u -> u != null)
														.map(u -> u.getAsString()).collect(Collectors.toList()));
						if(SQLAccess.Event.saveEvent(e))
							MessageUtils.sendStatusMessageInfo(event.getHook(), "Event saved!");
						else
							MessageUtils.sendStatusMessageWarn(event.getHook(), "Event already exists!");
						break;
					}
				}
			} catch(Exception e) {
				MessageUtils.sendMessage(event.getHook(), e.getMessage());
			}
		else
			MessageUtils.sendGTFO(event.getHook(), "Only users with 'Manage Messages' can use this command");
	}
	
	public static boolean isStreamTime() {
		List<Event> events = SQLAccess.Event.getCurrentAndFutureEvents("GL");
		Timestamp now = Timestamp.from(Instant.now());
		Timestamp streamBefore = Timestamp.from(Instant.ofEpochMilli(now.getTime() + (30 * 60 * 1000)));
		for(Event e : events) {
			if(e.getName().toLowerCase().contains("stream"))
				if(streamBefore.after(e.getStartDate()) && now.before(e.getEndDate()))
					return true;
		}
		return false;
	}
	
	public static Embed show(String region, boolean stream, boolean maintenance, boolean full) {
		if(region == null)
			region = "GL";
		List<Event> events = SQLAccess.Event.getCurrentAndFutureEvents(region);
		Embed embed = new Embed();
		if(region.equals("GL") && !stream && !maintenance) {
			embed.addField(ImageUtils.getEmoteText("TonberryTroupe") + " Tonberry Troupe", 
					"[Website](https://www.tonberrytroupe.com/home)" + System.lineSeparator() + 
					"[Infographs by Time](https://www.tonberrytroupe.com/infographics/infographics-by-time)" + System.lineSeparator() + 
					"[Timeline](https://www.tonberrytroupe.com/resources-hub/global-timeline-planner)", true);
			embed.addField(ImageUtils.getEmoteText("DissidiaInfo") + " DissidiaInfo", 
					"[Website](http://dissidiainfo.com/)" + System.lineSeparator() + 
					"[Call to Arms](http://dissidiainfo.com/call-to-arms/c2a-list/) | " +
					"[Boss Guides](http://dissidiainfo.com/boss-guides/)" + System.lineSeparator() + 
					"[Chocobo Panels](http://dissidiainfo.com/chocobo-panel-missions/)", true);
			embed.addField("Other Contributors", 
					ImageUtils.getEmoteText("ffootip") + "[FFOOTip](https://ffootip.com/)" + System.lineSeparator() + 
					//"[OOTracker](https://ootracker.com/)" + System.lineSeparator() + 
					"[" + ImageUtils.getEmoteText("OOT1") + ImageUtils.getEmoteText("OOT2") + ImageUtils.getEmoteText("OOT3") + ImageUtils.getEmoteText("OOT4") + 
					ImageUtils.getEmoteText("OOT5") + ImageUtils.getEmoteText("OOT6") + "](https://ootracker.com/)", true);
		}
		embed.setFooter("If you have other suggestions for the top, please DM Quetz" + System.lineSeparator() + 
						"Credits to all the Content Creators that do all this content for us all <3");
		Timestamp now = Timestamp.from(Instant.now());
		Timestamp streamBefore = Timestamp.from(Instant.ofEpochMilli(now.getTime() + (30 * 60 * 1000))); //30 minutes before stream
		Timestamp week = Timestamp.from(Instant.ofEpochMilli(now.getTime() + (7 * 24 * 60 * 60 * 1000)));
		for(Event e : events) {
			if(!full && e.getStartDate().after(week))
				continue;
			String links = "";
			if(now.before(e.getStartDate()))
				links = "Starts in " + Methods.timeDifference(e.getStartDate(), now);
			else
				links = "Ends in " + Methods.timeDifference(e.getEndDate(), now);
			if(e.getName().toLowerCase().contains("stream")) {
				if(streamBefore.after(e.getStartDate()) && now.before(e.getEndDate()))
					links = "[" + links + "](https://www.twitch.tv/squareenix)";
				embed.addField(ImageUtils.getEmoteText("SquareEnix") + " " + e.getName(), links, true);
				if(stream) {
					embed.setFooter("");
					break;
				}
			}
			else if(e.getName().toLowerCase().contains("maint")) {
				embed.addField(ImageUtils.getEmoteText("SquareEnix") + " " + e.getName(), links, true);
				if(maintenance) {
					embed.setFooter("");
					break;
				}
			}
			else if(!stream && !maintenance) { //region.equals("GL")
				String title = "";
				String units = "";
				for(String u : e.getUnits()) {
					title += ImageUtils.getEmoteText(u);
					units += " | " + "[" + u + "](https://www.tonberrytroupe.com/infographics/" + Methods.urlizeTT(u) + ")";
				}
				embed.addField(title + " " + e.getName(), links + System.lineSeparator() + (units.length() > 0 ? units.substring(3) : ""), true);
			}
		}
		return embed;
	}

	@Override
	public CommandData getCommandData() {
		return super.getCommandData()
				.addOptions(new OptionData(OptionType.BOOLEAN, "full", "Show the entire schedule instead of just the 7 next days"));
	}

	@Override
	public CommandData getAdminCommandData() {
		return super.getCommandData().addSubcommands(
					new SubcommandData("list", "Show the schedule of the month")
						.addOptions(new OptionData(OptionType.BOOLEAN, "full", "Show the entire schedule instead of just the 7 next days")),
					new SubcommandData("delete", "Delete an event")
						.addOptions(new OptionData(OptionType.STRING, "event", "Name of the Event", true)),
					new SubcommandData("new", "Create a new event")
						.addOptions(new OptionData(OptionType.STRING, "event", "Name of the Event", true))
						.addOptions(new OptionData(OptionType.STRING, "startdate", "Start Date (DD/MM/YYYY hh:mm TZ) (hh:mm TZ are default for event start)", true))
						.addOptions(new OptionData(OptionType.STRING, "unit1", "Unit 1"))
						.addOptions(new OptionData(OptionType.STRING, "unit2", "Unit 2"))
						.addOptions(new OptionData(OptionType.STRING, "unit3", "Unit 3"))
						.addOptions(new OptionData(OptionType.STRING, "unit4", "Unit 4"))
						.addOptions(new OptionData(OptionType.STRING, "unit5", "Unit 5"))
						.addOptions(new OptionData(OptionType.STRING, "unit6", "Unit 6"))
						.addOptions(new OptionData(OptionType.STRING, "enddate", "End Date (Defaults to 2 weeks if not used)")));
	}
}