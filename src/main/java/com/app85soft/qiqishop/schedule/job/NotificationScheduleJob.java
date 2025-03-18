package com.app85soft.qiqishop.schedule.job;

import com.app85soft.qiqishop.schedule.entity.SchedulerJobInfo;
import com.app85soft.qiqishop.schedule.service.SchedulerService;
import jakarta.annotation.PostConstruct;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduleJob extends QuartzJobBean {

    @Autowired
    private SchedulerService schedulerService;

    final String SCHEDULE_0_HOUR_EVERYDAY_JOB = "SCHEDULE_0_HOUR_EVERYDAY_JOB";

    final String SCHEDULE_PER_5_MINUTES_JOB = "SCHEDULE_PER_5_MINUTES_JOB";

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        switch (context.getJobDetail().getKey().getName()) {
            case SCHEDULE_0_HOUR_EVERYDAY_JOB:
//                moneyStatementService.generateMoneyStatement();
                break;
            case SCHEDULE_PER_5_MINUTES_JOB:
                getSeatExpires();
                break;
        }
    }

    @PostConstruct
    void init() {
        schedulerService.scheduleNewJob(new SchedulerJobInfo(SCHEDULE_PER_5_MINUTES_JOB,
                NotificationScheduleJob.class.getName(), "0 0/5 * * * ?"));
    }

    private void getSeatExpires() {

    }

}