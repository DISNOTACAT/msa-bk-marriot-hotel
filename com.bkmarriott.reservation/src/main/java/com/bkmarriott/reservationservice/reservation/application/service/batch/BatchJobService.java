package com.bkmarriott.reservationservice.reservation.application.service.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchJobService {

    private final JobLauncher jobLauncher;
    private final Job inventoryHistoryAndSyncJob;

//    @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0 0 4 * * *")
    public void runJob(){
        runJob(inventoryHistoryAndSyncJob, "inventoryHistoryAndSyncJob");
    }

    private void runJob(Job job, String jobName) {
        try {
            log.info("Starting {}...", jobName);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
            log.info("{} has finished.", jobName);
        } catch (Exception e) {
            log.error("{} failed", jobName, e);
        }
    }

}
