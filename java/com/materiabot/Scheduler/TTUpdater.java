package com.materiabot.Scheduler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.materiabot.commands.general.PatreonCommand;

public class TTUpdater implements Job{	
	public TTUpdater() {}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		PatreonCommand.updateServerTonberryTroupe();
	}
}