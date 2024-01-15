package com.kafka.flink.demon.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaProducer {

    private static final Logger logger = LogManager.getLogger(KafkaProducer.class);

    @Value("${spring.kafka.producer.topic-id}")
    private String kafkaTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(final KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(final String message) {
        try {
            logger.info("Send message to Kafka: {}", message);
            final CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(kafkaTopic, message);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Message sent successfully {}", result);
                } else {
                    logger.error("Error sending message", ex);
                }
            });
        } catch (final Exception e) {
            logger.error("Exception message: {}", e.getMessage());
        }
    }
}
