package com.app85soft.qiqishop.schedule.entity;


import com.app85soft.qiqishop.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "qrtz_scheduler_job_info")
public class SchedulerJobInfo extends BaseEntity {

    private String jobName;

    private String jobGroup;

    private String jobClass;

    private String cronExpression;

    private Long repeatTime;

    private boolean cronJob;

    public SchedulerJobInfo() {
    }

    public SchedulerJobInfo(String jobName, String jobClass, Long repeatTime) {
        this.jobName = jobName;
        this.jobClass = jobClass;
        this.repeatTime = repeatTime;
        this.cronJob = false;
    }

    public SchedulerJobInfo(String jobName, String jobClass, String cronExpression) {
        this.jobName = jobName;
        this.jobClass = jobClass;
        this.cronExpression = cronExpression;
        this.cronJob = true;
    }

    public SchedulerJobInfo(String jobName, String jobGroup, String jobClass, Long repeatTime) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobClass = jobClass;
        this.repeatTime = repeatTime;
        this.cronJob = false;
    }

    public SchedulerJobInfo(String jobName, String jobGroup, String jobClass, String cronExpression) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobClass = jobClass;
        this.cronExpression = cronExpression;
        this.cronJob = true;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Long getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(Long repeatTime) {
        this.repeatTime = repeatTime;
    }

    public boolean getCronJob() {
        return cronJob;
    }

    public void setCronJob(boolean cronJob) {
        this.cronJob = cronJob;
    }
}