package com.app85soft.qiqishop.schedule.service;


import com.app85soft.qiqishop.schedule.entity.SchedulerJobInfo;

public interface SchedulerService {

    void startAllSchedulers();

    void scheduleNewJob(SchedulerJobInfo jobInfo);

    void updateScheduleJob(SchedulerJobInfo jobInfo);

    boolean unScheduleJob(String jobName);

    boolean deleteJob(SchedulerJobInfo jobInfo);

    boolean pauseJob(SchedulerJobInfo jobInfo);

    boolean resumeJob(SchedulerJobInfo jobInfo);

    boolean startJobNow(SchedulerJobInfo jobInfo);

}