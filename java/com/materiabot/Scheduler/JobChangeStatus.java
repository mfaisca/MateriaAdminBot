package com.materiabot.Scheduler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobChangeStatus implements Job{	
	public JobChangeStatus() {}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
	}
}