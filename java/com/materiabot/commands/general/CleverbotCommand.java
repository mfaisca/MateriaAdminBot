package com.materiabot.commands.general;
import java.util.HashMap;
import java.util.Map;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.CooldownManager;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import com.michaelwflaherty.cleverbotapi.CleverBotQuery;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CleverbotCommand extends _BaseCommand {
	private static final CleverBotQuery BOT = new CleverBotQuery(SQLAccess.getKeyValue(SQLAccess.CLEVERBOT_TOKEN_KEY), null);
	private static final Map<String, String> serverConversationID = new HashMap<>();
	
	public CleverbotCommand() { super("cleverbot", null); }

	@Override
	public void doStuff(final MessageReceivedEvent messageEvent) {
		Message message = messageEvent.getMessage();
		String msg = message.getContentDisplay().replace("@MateriaBot", "").replace("@SpiritusBot", "").replace("@", "").replace("#", "").trim();
		try {
			if(msg.isBlank()) {
				serverConversationID.remove(message.getGuild().getId());
				MessageUtils.sendMessageToChannel(message.getChannel(), "Conversation Resetted");
				return;
			}
			String conversationID = serverConversationID.get(message.getGuild().getId());
			BOT.setConversationID(conversationID == null ? "" : conversationID);
			BOT.setPhrase(msg);
			BOT.sendRequest();
			String response = BOT.getResponse();
			MessageUtils.sendMessageToChannel(message.getChannel(), response);
			if(conversationID == null)
				serverConversationID.put(message.getGuild().getId(), BOT.getConversationID());
		} catch (Exception e) {
			MessageUtils.sendMessageToChannel(message.getChannel(), "Error accessing CleverBot API. Please try again later.");
			MessageUtils.sendWhisper(Constants.QUETZ_ID, "Error using CleverBot. Error: " + e.getMessage());
		}
	}
	
	@Override
	public CooldownManager.Type getCooldown(Message message) { return CooldownManager.Type.CLEVERBOT; }
}