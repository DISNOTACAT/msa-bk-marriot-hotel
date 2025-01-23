package com.bkmarriott.reservationservice.reservation.infrastructure.batch.step;

import com.bkmarriott.reservationservice.reservation.infrastructure.batch.config.JpaItemListWriter;
import com.bkmarriott.reservationservice.reservation.domain.event.RoomInventoryEvent;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.InventoryHistoryEntity;
import com.bkmarriott.reservationservice.reservation.presentation.event.config.KafkaConsumerConfig;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InventoryHistoryStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Step insertInventoryHistoryStep(){
        return new StepBuilder("insertHistory", jobRepository)
                .<RoomInventoryEvent, List<InventoryHistoryEntity>>chunk(10, transactionManager)
                .reader(inventoryEventKafkaItemReader())
                .processor(inventoryEventToHistoryProcessor())
                .writer(inventoryHistoryWriter())
                .build();
    }

    @Bean
    @StepScope
    public KafkaItemReader<String, RoomInventoryEvent> inventoryEventKafkaItemReader() {
        log.info("[BatchConfig] [inventoryEventKafkaItemReader] start ");
        Properties props = new Properties();
        props.putAll(kafkaConsumerConfig.consumeConfig());

        return new KafkaItemReaderBuilder<String, RoomInventoryEvent>()
                .name("kafkaItemReader")
                .topic("room.inventory.change")
                .partitions(0)
                .partitionOffsets(new HashMap<>())
                .consumerProperties(props)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<RoomInventoryEvent, List<InventoryHistoryEntity>> inventoryEventToHistoryProcessor() {
        return InventoryHistoryEntity::toEntity;
    }

    @Bean
    @StepScope
    public JpaItemListWriter<InventoryHistoryEntity> inventoryHistoryWriter() {
        JpaItemWriter<InventoryHistoryEntity> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        JpaItemListWriter<InventoryHistoryEntity> writer = new JpaItemListWriter<>(jpaItemWriter);
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
