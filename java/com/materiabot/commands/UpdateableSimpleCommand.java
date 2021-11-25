package com.materiabot.commands;
import java.sql.ResultSet;
import org.apache.commons.lang3.StringUtils;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class UpdateableSimpleCommand extends _BaseCommand{
	private final String reply, ownerStr;
	
	public UpdateableSimpleCommand(String command, String reply, String owner, String help) {
		super(command, help);
		this.reply = reply;
		ownerStr = owner;
	}

	@Override
	public void doStuff(SlashCommandEvent event) {
		String text = null;
		try {
			OptionMapping map = event.getOption("update");
			if(map != null && event.getMember().getIdLong() == Constants.QUETZ_ID) {
				String msg = event.getOption("update").getAsString();
				if(msg.equalsIgnoreCase("clear")){
					SQLAccess.executeInsert("UPDATE Commands SET data = NULL WHERE name = ?", this.getCommand());
					MessageUtils.sendMessage(event.getHook(), "Cleared");
				}else{
					SQLAccess.executeInsert("UPDATE Commands SET data = ? WHERE name = ?", msg, this.getCommand());
					MessageUtils.sendMessage(event.getHook(), "Updated");
				}
			}
			else{
				ResultSet rs = SQLAccess.executeSelect("SELECT data FROM Commands WHERE name = ?", this.getCommand());
				if(!rs.next()) {
					MessageUtils.sendMessage(event.getHook(), "Nothing has been set for this command yet!");
					return;
				}else
					text = rs.getString("data").trim();
			}
			if(text == null){
				MessageUtils.sendMessage(event.getHook(), "Nothing has been set for this command yet!");
				return;
			}
			if(isImage()) {
				EmbedBuilder embed = new EmbedBuilder();
				embed.setImage(text);
				if(ownerStr != null && StringUtils.isNumeric(ownerStr)) {
					User owner = event.getJDA().retrieveUserById(ownerStr).complete();
					embed.setFooter("Credits to " + owner.getName() + "#" + owner.getDiscriminator(), owner.getAvatarUrl());
				}
				else if(ownerStr != null)
					embed.setFooter("Credits to " + ownerStr);
				MessageUtils.sendEmbed(event.getHook(), embed);
			}else {
				if(ownerStr != null) {
					if(StringUtils.isNumeric(ownerStr)) {
						User owner = event.getJDA().retrieveUserById(ownerStr).complete();
						text += System.lineSeparator() + "Credits to " + owner.getName() + "#" + owner.getDiscriminator();
					}
				} else
					text += System.lineSeparator() + "Credits to " + ownerStr;
				MessageUtils.sendMessage(event.getHook(), text);
			}
		} catch(Exception e) {
			MessageUtils.sendStatusMessageCrash(event.getHook(), "Unexpected error executing command");
		}
	}
	
	private boolean isImage() {
		final String[] imageExtensions = new String[]{".png", ".jpg", ".jpeg", ".gif"};
		for(String a : imageExtensions)
			if(reply.toLowerCase().endsWith(a))
				return true;
		return false;
	}
	
	@Override
	public CommandData getAdminCommandData() {
		CommandData cmd = new CommandData(super.getCommand(), help);
		cmd.addOption(OptionType.STRING, "update", "Ignored if you don't have permissions");
		return cmd;
	}
}