package com.materiabot.commands.general;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class AdminCommand extends _BaseCommand{	
	public AdminCommand() { super("admin", "Pretend this command doesn't exist!"); }

	@Override
	public void doStuff(final SlashCommandEvent event) {
		if(event.getMember().getIdLong() == Constants.QUETZ_ID || event.getMember().getIdLong() == Constants.CEL_ID)
			try {
				if(event.getOption("patreonkey") != null) {
					if(PatreonCommand.updatePatreonKey(event.getMember(), event.getOption("patreonkey").getAsString()))
						MessageUtils.sendMessage(event.getHook(), "Patreon API Key Updated");
					else
						MessageUtils.sendStatusMessageError(event.getHook(), "Error updating Patreon API Key");
				}
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
			OptionData od3 = new OptionData(OptionType.STRING, "patreonkey", "Insert Patreon Key Here");
		cd.addOptions(od3);
		return cd;
	}
}