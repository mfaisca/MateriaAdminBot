package com.materiabot;
import com.materiabot.Utils.Constants;
import com.materiabot.commands._BaseCommand;
import com.materiabot.commands._Listener;
import Units.*;
//import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class PluginManager {
	private PluginManager() {}

	public static void reset(boolean fullLoad) {
		Constants.UNITS.add(new Leonhart());
		Constants.UNITS.add(new Enna());
		Constants.UNITS.add(new LannReynn());
		Constants.COMMANDS.clear();
		PluginManager.loadCommands();
	}
	
	private static void loadCommands() {
		_Listener.unloadPluginCommands();
		//CommandListUpdateAction commands = Constants.getClient().updateCommands();
		for(_BaseCommand c : Constants.COMMANDS)
			if(c.getAdminCommandData() != null)
				Constants.getClient().getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID).upsertCommand(c.getAdminCommandData()).queue();
	}
}