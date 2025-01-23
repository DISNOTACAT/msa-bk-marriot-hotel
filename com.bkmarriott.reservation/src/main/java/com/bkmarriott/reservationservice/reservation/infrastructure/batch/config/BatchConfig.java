package com.bkmarriott.reservationservice.reservation.infrastructure.batch.config;

import com.bkmarriott.reservationservice.reservation.infrastructure.batch.step.InventoryHistoryStepConfig;
import com.bkmarriott.reservationservice.reservation.infrastructure.batch.step.InventorySyncStepConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final InventoryHistoryStepConfig inventoryHistoryStepConfig;
    private final InventorySyncStepConfig inventorySyncStepConfig;

    @Bean
    public Job inventoryHistoryAndSyncJob(){
        log.info("[BatchConfig] [inventoryHistoryAndSyncJob] START ");
        return new JobBuilder("inventoryHistoryAndSyncJob", jobRepository)
                .start(inventoryHistoryStepConfig.insertInventoryHistoryStep())
                .next(inventorySyncStepConfig.validateAndSyncInventoryStep())
                .build();
    }
}
