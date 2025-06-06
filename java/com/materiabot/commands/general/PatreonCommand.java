package com.materiabot.commands.general;
import java.awt.Color;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.ImageUtils;
import com.materiabot.Utils.ImageUtils.Emotes;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import com.patreon.PatreonAPI;
import com.patreon.resources.Campaign;
import com.patreon.resources.Pledge;
import Shared.BotException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class PatreonCommand extends _BaseCommand{
	private static final String PATREON_LINK = "https://www.patreon.com/MateriaBot";
	private static final int NUMBER_OF_VISIBLE_PATRONS = 10;
	private static HashMap<String, String> REPLACES = new HashMap<>();
	
	static {
		REPLACES.put("Sae-Hon Kim", "Alhazard");
	}
	private static final String replaceName(String name) {
		return REPLACES.containsKey(name) ? REPLACES.get(name) : name;
	}

	public PatreonCommand() { 
		super("patreon", "Shows Patreon Supporters for MateriaBot <3");
	}
	public static boolean isUserPatreon(User u) {
		Guild materiaServer = u.getJDA().getGuildById(Constants.MATERIABOT_SERVER_ID);
		return u.getIdLong() == Constants.QUETZ_ID || 
				Stream.of(materiaServer.getMembersWithRoles(materiaServer.getRoleById(554660297927819264L)), 
						materiaServer.getMembersWithRoles(materiaServer.getRoleById(554660182093987869L)))
				.flatMap(l -> l.stream()).distinct().anyMatch(m -> m.getUser().equals(u));
	}

	private static EmbedBuilder build(String bannerURL, List<String> patrons, String oldest, String mostRecent, int totalPatrons) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("MateriaBot Patreon");
		builder.setThumbnail(Constants.getClient().getSelfUser().getAvatarUrl());
		builder.setColor(new Color(249,104,84));
		builder.setImage(bannerURL);
		builder.addField("Link", PATREON_LINK, false);
		builder.addField("Special Mentions", 
				"Oldest Patron: " + oldest + System.lineSeparator() + 
				"Newest Patron: " + mostRecent + System.lineSeparator(), true);
		builder.addField("Patron Count", totalPatrons + " Patreons", true);
		int i = 0;
		while((i * NUMBER_OF_VISIBLE_PATRONS) < patrons.size()) {
			builder.addField(i == 0 ? "Patrons" : MessageUtils.E, 
					patrons.subList(
							i * NUMBER_OF_VISIBLE_PATRONS, 
							(i * NUMBER_OF_VISIBLE_PATRONS + NUMBER_OF_VISIBLE_PATRONS) >= 
							patrons.size() ? patrons.size() : 
								i * NUMBER_OF_VISIBLE_PATRONS + NUMBER_OF_VISIBLE_PATRONS)
					.stream().reduce((o1,  o2) -> o1 + System.lineSeparator() + o2).orElse("---"), false);
			i++;
		}
		return builder;
	}

	//This method exists to update Tonberry Troupe Patreon roles on MateriaBot Server due to PatreonBot not working with multiple accounts
	public static final void updateServerTonberryTroupe() {
		Guild materiaServer = Constants.getClient().getGuildById(Constants.MATERIABOT_SERVER_ID);
		final MessageChannel ttPatreonChannel = materiaServer.getTextChannelById(679820064639287357L);
		final MessageChannel ttPrivateChannel = materiaServer.getTextChannelById(636300936993701917L);
		try {
			//MessageUtils.sendMessageToChannel(troupeNotes, "Executing a TT Patreon update...");
			final PatreonAPI apiClient = new PatreonAPI(SQLAccess.getKeyValue(SQLAccess.PATREON_TT_ACCESS_TOKEN));
			Campaign campaign = apiClient.fetchCampaigns().get().get(0);
			final List<Pledge> pledges = apiClient.fetchAllPledges(campaign.getId());
			final List<Long> patreonDiscordIdsT2 = new LinkedList<>();
			final List<Long> patreonDiscordIdsT3 = new LinkedList<>();
			final List<String> patronsMissingDiscordTag = new LinkedList<>();
			final List<Long> patreonDiscordLongSub = new LinkedList<>();
			for(Pledge p : pledges)
				if(p.getPatron().getSocialConnections() != null) {
					if(p.getPatron().getSocialConnections().getDiscord() != null) {
						long monthsPatron = ChronoUnit.MONTHS.between(LocalDate.parse(p.getCreatedAt().substring(0, 10), 
																		DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalDate.now());
						if(monthsPatron >= 6) {
							patreonDiscordLongSub.add(Long.parseLong(p.getPatron().getSocialConnections().getDiscord().getUser_id()));
						}
						if(p.getReward().getTitle().equals("Tonberry Chef"))
							patreonDiscordIdsT2.add(Long.parseLong(p.getPatron().getSocialConnections().getDiscord().getUser_id()));
						else if(p.getReward().getTitle().equals("Tonberry King")) {
							patreonDiscordIdsT2.add(Long.parseLong(p.getPatron().getSocialConnections().getDiscord().getUser_id()));
							patreonDiscordIdsT3.add(Long.parseLong(p.getPatron().getSocialConnections().getDiscord().getUser_id()));
						}
					}
					else if(p.getReward().getTitle().equals("Tonberry Chef") || p.getReward().getTitle().equals("Tonberry King"))
						patronsMissingDiscordTag.add(p.getPatron().getFullName() + " - " + p.getReward().getTitle());
				}
			final Role tonberryChef = materiaServer.getRoleById(744314490946060320L);
			final Role tonberryKing = materiaServer.getRoleById(744314477490470932L);
			final Role tonberryLongSub = materiaServer.getRoleById(953392758763163750L);
			materiaServer.findMembersWithRoles(tonberryKing).onSuccess(lm ->
				lm.stream().filter(m -> !patreonDiscordIdsT3.contains(m.getIdLong())).forEach(m -> 
					materiaServer.removeRoleFromMember(m, tonberryKing).submit()
					.thenAccept(v -> MessageUtils.sendMessage(ttPrivateChannel, m.getEffectiveName() + " is no longer a Tonberry King."))
			)).onSuccess(v1 -> 
			materiaServer.findMembersWithRoles(tonberryChef).onSuccess(lm ->
				lm.stream().filter(m -> !patreonDiscordIdsT2.contains(m.getIdLong())).forEach(m -> 
					materiaServer.removeRoleFromMember(m, tonberryChef).submit()
					.thenAccept(v -> MessageUtils.sendMessage(ttPrivateChannel, m.getEffectiveName() + " is no longer a Tonberry Chef."))
			))).onSuccess(v1 -> 
			materiaServer.retrieveMembersByIds(patreonDiscordIdsT2).onSuccess(lm ->
				lm.stream().filter(m -> !m.getRoles().contains(tonberryChef))
				.forEach(m -> materiaServer.addRoleToMember(m, tonberryChef).submit()
					.thenAccept(v -> MessageUtils.sendMessage(ttPrivateChannel, m.getEffectiveName() + " is a new Tonberry Chef."))
					.thenAccept(v -> MessageUtils.sendMessage(ttPatreonChannel, m.getEffectiveName() + " is a new Tonberry Chef."))
			))).onSuccess(v1 -> 
			materiaServer.retrieveMembersByIds(patreonDiscordIdsT3).onSuccess(lm ->
				lm.stream().filter(m -> !m.getRoles().contains(tonberryKing))
				.forEach(m -> materiaServer.addRoleToMember(m, tonberryChef).submit()
					.thenAccept(v -> materiaServer.addRoleToMember(m, tonberryKing).submit())
					.thenAccept(v -> MessageUtils.sendMessage(ttPrivateChannel, m.getEffectiveName() + " is a new Tonberry King."))
					.thenAccept(v -> MessageUtils.sendMessage(ttPatreonChannel, m.getEffectiveName() + " is a new Tonberry King."))
			))).onSuccess(v1 -> 
			materiaServer.retrieveMembersByIds(patreonDiscordLongSub).onSuccess(lm ->
				lm.stream().filter(m -> !m.getRoles().contains(tonberryLongSub))
				.forEach(m -> materiaServer.addRoleToMember(m, tonberryLongSub).submit()
					.thenAccept(v -> MessageUtils.sendMessage(ttPrivateChannel, m.getEffectiveName() + " is now a Permanent Tonberry."))
					.thenAccept(v -> MessageUtils.sendMessage(ttPatreonChannel, m.getEffectiveName() + " is now a Permanent Tonberry."))
			))).onSuccess(v1 -> {
				;//MessageUtils.sendMessageToChannel(troupeNotes, "TT Patreon successfully executed");
			});
		} catch (IOException e) {
			MessageUtils.sendWhisper(Constants.CEL_ID, "Patreon Key is probably dead, please refresh." + System.lineSeparator() + "https://www.patreon.com/portal/registration/register-clients" + "Use the command /admin patreonkey <CreatorAccessToken> on MateriaBot Admin Server");
			MessageUtils.sendMessage(materiaServer.getTextChannelById(623171253796077589L), "**Tonberry Troupe** Patreon Key is probably dead, please refresh." + System.lineSeparator() + "https://www.patreon.com/portal/registration/register-clients" + "Use the command /admin patreonkey <CreatorAccessToken> on MateriaBot Admin Server");
			MessageUtils.sendWhisper(Constants.QUETZ_ID, "**Tonberry Troupe** Patreon Key is probably dead, please refresh." + System.lineSeparator() + "https://www.patreon.com/portal/registration/register-clients" + "Use the command /admin patreonkey <CreatorAccessToken> on MateriaBot Admin Server");
			e.printStackTrace();
		}
	}

	public static long getPatreonCount() {
		try {
			PatreonAPI apiClient = new PatreonAPI(SQLAccess.getKeyValue(SQLAccess.PATREON_ACCESS_TOKEN));
			Campaign campaign = apiClient.fetchCampaigns().get().get(0);
			List<Pledge> pledges = apiClient.fetchAllPledges(campaign.getId());
			return pledges.size();
		} catch (IOException e) {
			return -1;
		}
	}

	@Override
	public void doStuff(final SlashCommandEvent event) {
		try {
			PatreonAPI apiClient = new PatreonAPI(SQLAccess.getKeyValue(SQLAccess.PATREON_ACCESS_TOKEN));
			final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			Campaign campaign = apiClient.fetchCampaigns().get().get(0);
			List<Pledge> pledges = apiClient.fetchAllPledges(campaign.getId());
			int totalPatrons = pledges.size();
			String mostRecent = pledges.stream().sorted((p1, p2) -> {
				try { return f.parse(p2.getCreatedAt()).compareTo(f.parse(p1.getCreatedAt())); }
				catch (ParseException e) { ; } return 0;
			}).findFirst().map(p -> (p.getReward() == null ? ImageUtils.getEmoteText(Emotes.UNKNOWN_EMOTE.get()) : ImageUtils.getEmoteText(p.getReward().getTitle())) + " " + p.getPatron().getFullName()).orElse(null);
			String oldest = pledges.stream().sorted((p1, p2) -> {
				try { return f.parse(p1.getCreatedAt()).compareTo(f.parse(p2.getCreatedAt()));}
				catch (ParseException e) { ; } return 0;
			}).findFirst().map(p -> (p.getReward() == null ? ImageUtils.getEmoteText(Emotes.UNKNOWN_EMOTE.get()) : ImageUtils.getEmoteText(p.getReward().getTitle())) + " " + p.getPatron().getFullName()).orElse(null);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			List<String> patrons = pledges.stream()
					.sorted((p1, p2) -> p2.getAmountCents() - p1.getAmountCents())
					.filter(p -> replaceName(p.getPatron().getFullName()) != null)
					.map(p -> (p.getReward() == null ? 
							ImageUtils.getEmoteText(Emotes.UNKNOWN_EMOTE.get()) : ImageUtils.getEmoteText(p.getReward().getTitle()))
							+ " " + replaceName(p.getPatron().getFullName()) + 
							" (" + ChronoUnit.MONTHS.between(LocalDate.parse(p.getCreatedAt().substring(0, 10), formatter), LocalDate.now()) + " months)")
					.collect(Collectors.toList());
			MessageUtils.sendEmbed(event.getHook(), build(campaign.getImageUrl(), patrons, oldest, mostRecent, totalPatrons));
		} catch (IOException e) {
			MessageUtils.sendWhisper(Constants.QUETZ_ID, "Patreon Key is dead, please refresh." + System.lineSeparator() + "https://www.patreon.com/portal/registration/register-clients" + System.lineSeparator() + "Use '$patreon <key>' to update the key");
			MessageUtils.sendMessage(event.getHook(), "Error connecting with Patreon API. Try again later.");
			e.printStackTrace();
		}
	}
	public static final boolean updatePatreonKey(Member member, String key) throws BotException {
		if(member.getIdLong() == Constants.QUETZ_ID) {
			SQLAccess.setKeyValue(SQLAccess.PATREON_ACCESS_TOKEN, key);
			return true;
		}
		else if(member.getIdLong() == Constants.CEL_ID) {
			SQLAccess.setKeyValue(SQLAccess.PATREON_TT_ACCESS_TOKEN, key);
			return true;
		}
		return false;
	}
}