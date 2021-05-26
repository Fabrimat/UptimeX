package me.fabrimat.uptimex.job;

import me.fabrimat.uptimex.job.step.Step;

import java.util.ArrayList;
import java.util.List;

public class Job implements Runnable {
    
    private final String jobName;
    private final List<Step> stepList;
    private final JobScheduleInfo scheduleInfo;
    
    public Job(String jobName, JobScheduleInfo scheduleInfo) {
        this.jobName = jobName;
        this.scheduleInfo = scheduleInfo;
        stepList = new ArrayList<>();
    }
    
    public void run() {
        this.runSteps();
    }
    
    public boolean runSteps() {
        boolean success = true;
        
        for (Step step : getStepListCopy()) {
            if (!step.isConditional() || success) {
                success = step.execute();
            } else {
                break;
            }
        }
        return success;
    }
    
    public synchronized void addStep(Step step) {
        this.stepList.add(step);
    }
    
    public synchronized void clearSteps() {
        this.stepList.clear();
    }
    
    public synchronized List<Step> getStepListCopy() {
        return new ArrayList<>(this.stepList);
    }
    
    public String getJobName() {
        return jobName;
    }
    
    public JobScheduleInfo getScheduleInfo() {
        return scheduleInfo;
    }
}
