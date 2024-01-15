package com.kafka.flink.demon.kafka;

import com.google.gson.Gson;
import com.kafka.flink.demon.model.Transaction;
import com.kafka.flink.demon.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private static final Logger logger = LogManager.getLogger(KafkaConsumer.class);
    private final TransactionService txnService;
    private final Gson gson;


    @KafkaListener(topics = "${spring.kafka.consumer.topic-id}")
    public void handleMessage(final String message, final Acknowledgment acknowledgment) {
        try {
            logger.info("Consume message: {}", message);
            acknowledgment.acknowledge();
            txnService.updateStatus(gson.fromJson(message, Transaction.class));
        } catch (final Exception e) {
            logger.error("Exception: {}", e.getMessage());
        }
    }
}
