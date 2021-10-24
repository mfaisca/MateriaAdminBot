package com.materiabot.Scheduler;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;

public class _Scheduler {
	private enum TRIGGER{
		T_1MINUTE(TriggerBuilder.newTrigger().withIdentity("1 Minute").withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * ? * * *")).build()),
		T_10MINUTES(TriggerBuilder.newTrigger().withIdentity("10 Minutes").withSchedule(CronScheduleBuilder.cronSchedule("0 0/10 * ? * * *")).build()),
		T_20MINUTES(TriggerBuilder.newTrigger().withIdentity("20 Minutes").withSchedule(CronScheduleBuilder.cronSchedule("0 0/20 * ? * * *")).build()),
		T_30MINUTES(TriggerBuilder.newTrigger().withIdentity("30 Minutes").withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * ? * * *")).build()),
		T_1HOUR(TriggerBuilder.newTrigger().withIdentity("1 Hour").withSchedule(CronScheduleBuilder.cronSchedule("0 0 * ? * * *")).build()),
		T_24HOURS(TriggerBuilder.newTrigger().withIdentity("24 Hours").withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 ? * * *")).build()),
		;
		
		private Trigger trigger;
		
		private TRIGGER(Trigger trigger) {
			this.trigger = trigger;
		}
		public Trigger get() { return trigger; }
	}
	public static Scheduler SCHEDULER;
	
	private _Scheduler() {}
	
	public static void setup() {
		try {
			SCHEDULER = StdSchedulerFactory.getDefaultScheduler();
			/////REPEAT THIS LINE AND CREATE A NEW CLASS FOR EACH SCHEDULED THING/////
			SCHEDULER.scheduleJob(JobBuilder.newJob(JobChangeStatus.class).withIdentity("Change Status").build(), TRIGGER.T_1MINUTE.get());
			SCHEDULER.scheduleJob(JobBuilder.newJob(TTUpdater.class).withIdentity("TT Job Updater").build(), TRIGGER.T_24HOURS.get());
			SCHEDULER.scheduleJob(JobBuilder.newJob(ScheduleUnitsInMemory.class).withIdentity("Keep schedule units in memory").build(), TRIGGER.T_20MINUTES.get());
		} catch (SchedulerException e) {
			MessageUtils.sendWhisper(Constants.QUETZ_ID, "Error starting Scheduler: " + e.getMessage());
		}
	}
	public static void start() {
		try {
			if(SCHEDULER != null && !SCHEDULER.isStarted())
				SCHEDULER.start();
		} catch (SchedulerException e) {}
	}
	public static void shutdown() {
		try {
			if(SCHEDULER != null && SCHEDULER.isStarted())
				SCHEDULER.shutdown();
		} catch (SchedulerException e) {}
	}
}