package com.materiabot.Scheduler;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.materiabot._Library;
import com.materiabot.IO.SQL.SQLAccess;

public class ScheduleUnitsInMemory implements Job{	
	public ScheduleUnitsInMemory() {}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println(new Date().toGMTString() + " Memory used: " + (((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024f)/1024f) + " MB");
		SQLAccess.Event.getCurrentAndFutureEvents("GL").stream()
			.flatMap(e -> e.getUnits().stream()).distinct()
			.forEach(u -> _Library.L.getUnit(u));
	}
}