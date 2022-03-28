package com.materiabot;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.plugface.core.PluginRef;
import org.plugface.core.factory.PluginManagers;
import org.plugface.core.factory.PluginSources;
import com.materiabot.Utils.Constants;
import com.materiabot.commands._BaseCommand;
import com.materiabot.commands._Listener;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators._Plugin;
import com.materiabot.IO.SQL.SQLAccess;

public class PluginManager {
	private PluginManager() {}

	public static void reset(boolean fullLoad) throws Exception {
		/* Version Setup:
		 * Major: New Commands
		 * Minor: Change in parameters  
		 * Patch: Code changes (doesn't require UI change)
		 */
		
		_Library.L.clearUnitCache();
		Constants.UNITS.clear();
		Constants.ABILITY_EFFECT.clear();
		Constants.AILMENT_EFFECT.clear();
		Constants.AILMENT_REQUIRED.clear();
		Constants.AURA_EFFECT.clear();
		Constants.AURA_REQUIRED.clear();
		Constants.LABELS.clear();
		PluginManager.loadUnits();
		PluginManager.loadEffects();
		if(fullLoad) {
			Constants.COMMANDS.clear();
			PluginManager.loadCommands();
		}
	}
	
	private static boolean effectsFilter(PluginRef<?> p) {
		return p.getName().contains("Passive.") || 
				p.getName().contains("Ability.") || 
				p.getName().contains("Ailment.") || 
				p.getName().contains("Aura.") || 
				p.getName().contains("Misc.");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void loadEffects() throws Exception {
		org.plugface.core.PluginManager manager = PluginManagers.defaultPluginManager();
		for(PluginRef p : manager.getAllPlugins().stream().filter(PluginManager::effectsFilter).collect(Collectors.toList()))
			manager.removePlugin(p.getName());
		manager.loadPlugins(PluginSources.jarSource("file:///" + new java.io.File("plugins").getAbsolutePath().replace("\\", "/")));
		//LoadCommands
		_Listener.unloadPluginCommands();
		for(PluginRef p : manager.getAllPlugins().stream().filter(PluginManager::effectsFilter).collect(Collectors.toList())) {
			_Plugin eff = (_Plugin) p.get();
			HashMap map = null;
			switch(p.getName().substring(0, p.getName().lastIndexOf("."))) {
				case "Passive.Effect":
					map = Constants.PASSIVE_EFFECT; break;
				case "Passive.Required":
					map = Constants.PASSIVE_REQUIRED; break;
				case "Ability.Effect":
					map = Constants.ABILITY_EFFECT; break;
				case "Ailment.Effect":
					map = Constants.AILMENT_EFFECT; break;
				case "Ailment.Required":
					map = Constants.AILMENT_REQUIRED; break;
				case "Aura.Effect":
					map = Constants.AURA_EFFECT; break;
				case "Aura.Required":
					map = Constants.AURA_REQUIRED; break;
				case "Misc.Condition":
					map = Constants.LABELS; break;
				default: ;
			}
			if(map != null)
				map.put(eff.getId(), eff);
		}
	}
	
	@SuppressWarnings({ "rawtypes"})
	private static void loadCommands() throws Exception {
		org.plugface.core.PluginManager manager = PluginManagers.defaultPluginManager();
		for(PluginRef p : manager.getAllPlugins().stream().filter(p -> p.getName().contains("Command.")).collect(Collectors.toList()))
			manager.removePlugin(p.getName());
		manager.loadPlugins(PluginSources.jarSource("file:///" + new java.io.File("plugins").getAbsolutePath().replace("\\", "/")));
		//LoadCommands
		_Listener.unloadPluginCommands();
//        Constants.getClient().getGuildById(Constants.MATERIABOT_SERVER_ID).retrieveCommands().complete().stream()
//    		.forEach(c -> Constants.getClient().getGuildById(Constants.MATERIABOT_SERVER_ID).deleteCommandById(c.getId()).queue());
//        Constants.getClient().getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID).retrieveCommands().complete().stream()
//    		.forEach(c -> Constants.getClient().getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID).deleteCommandById(c.getId()).queue());
		manager.getAllPlugins().stream()
			.filter(p -> p.getName().contains("Command."))
			.filter(p -> !p.getName().contains("Disabled"))
			.map(p -> (_BaseCommand)p.get())
			.forEach(c -> Constants.COMMANDS.add(c));
		

		File plugins = new File(new java.io.File("plugins").getAbsolutePath().replace("\\", "/"));
		String newVersion = Arrays.asList(plugins.listFiles()).stream()
								.filter(f -> f.getName().contains("Commands"))
								.map(f -> f.getName())
								.filter(f -> f.contains("-"))
								.map(f -> f.split("-")[1])
								.map(f -> f.substring(0, f.lastIndexOf(".")))
								.map(f -> f.substring(0, f.lastIndexOf(".")))
								.findFirst().orElse(null);
		if(newVersion != null) {
			String currentVersion = SQLAccess.getKeyValue("COMMAND_VERSION");
			System.out.println("Checking command revision. Cur:" + currentVersion + " | New:" + newVersion);
			if(!currentVersion.equals(newVersion)){
				CommandListUpdateAction commands = Constants.getClient().updateCommands();
				commands.queue();
				for(_BaseCommand c : Constants.COMMANDS) {
					if(c.getAdminCommandData() != null)
						Constants.getClient().getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID).upsertCommand(c.getAdminCommandData()).queue();
				}
				if(!Constants.DEBUG) {
					for(_BaseCommand c : Constants.COMMANDS) {
						if(c.getCommandData() == null)
							;
						else
							commands.addCommands(c.getCommandData());
					}
			        commands.queue();
					for(_BaseCommand c : Constants.COMMANDS) {
						if(c.getAdminCommandData() != null)
							Constants.getClient().getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID).upsertCommand(c.getAdminCommandData()).queue();
					}
				}
				else {
					for(_BaseCommand c : Constants.COMMANDS) {
						if(c.getCommandData() != null)
							Constants.getClient().getGuildById(Constants.MATERIABOT_SERVER_ID).upsertCommand(c.getCommandData()).queue();
						if(c.getAdminCommandData() != null)
							Constants.getClient().getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID).upsertCommand(c.getAdminCommandData()).queue();
					}
				}
				SQLAccess.setKeyValue("COMMAND_VERSION", newVersion);
//			} else {
//				for(_BaseCommand c : Constants.COMMANDS) {
//					if(c.getAdminCommandData() != null)
//						Constants.getClient().getGuildById(Constants.MATERIABOT_ADMIN_SERVER_ID).upsertCommand(c.getAdminCommandData()).complete();
//				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private static void loadUnits() throws Exception {
		org.plugface.core.PluginManager manager = PluginManagers.defaultPluginManager();
		for(PluginRef p : manager.getAllPlugins().stream().filter(p -> p.getName().contains("Unit.")).collect(Collectors.toList()))
			manager.removePlugin(p.getName());
		manager.loadPlugins(PluginSources.jarSource("file:///" + new java.io.File("plugins").getAbsolutePath().replace("\\", "/")));
		//LoadUnits
		Constants.UNITS.clear();
		Constants.UNITS.addAll(manager.getAllPlugins().stream()
							.filter(p -> p.getName().contains("Unit."))
							.map(p -> (Unit)p.get())
							.peek(p -> System.out.println("Unit Override: " + p.getName()))
							.collect(Collectors.toList()));
	}
}