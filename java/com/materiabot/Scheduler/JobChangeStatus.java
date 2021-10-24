package com.materiabot.Scheduler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.materiabot.Utils.Constants;
import com.materiabot.commands.general.PatreonCommand;
import com.materiabot.commands.general.ScheduleCommand;
import Shared.Methods;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;

public class JobChangeStatus implements Job{	
	private static int counter = 0;
	
	public JobChangeStatus() {}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Activity a = null;
		if(ScheduleCommand.isStreamTime())
			a = Activity.streaming("Opera Omnia Monthly Stream", "https://www.twitch.tv/squareenix");
		else
			switch(counter++) {
				case 0:
					a = Activity.playing("Opera Omnia"); break;
				case 1:
					long guildCount = Constants.getClient().getGuildCache().size();
					if(guildCount == 0 || Methods.RNG.nextBoolean())
						guildCount = Constants.getClient().getGuilds().size();
					a = Activity.watching(guildCount + " servers"); break;
				case 2:
					long pc = PatreonCommand.getPatreonCount();
					if(pc == -1) { 
						execute(context); return; }
					a = Activity.of(ActivityType.DEFAULT, "Thanking " + pc + " patrons.");
					break;
				default: 
					counter = 0;
					execute(context); return;
			}
		Constants.getClient().getPresence().setActivity(a);
	}
}