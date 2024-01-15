package com.kafka.flink.demon.service;

import com.google.gson.Gson;
import com.kafka.flink.demon.kafka.KafkaProducer;
import com.kafka.flink.demon.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final String PROCESSING = "PROCESSING";
    private static final String SUCCESS = "SUCCESS";
    private final KafkaProducer kafkaProducer;
    private final Gson gson;
    private final RedissonClient redissonClient;

    public Transaction processTxn(final Transaction txn) {
        txn.setStatus(PROCESSING);
        final String message = gson.toJson(txn);
        RBucket<String> bucket = redissonClient.getBucket(String.valueOf(txn.getTransId()));
        bucket.set(message, 10000, TimeUnit.SECONDS);
        kafkaProducer.send(message);
        return txn;
    }

    public Transaction getFinalStatus(final long id) {
        final String message = (String) redissonClient.getBucket(String.valueOf(id)).get();
        return gson.fromJson(message, Transaction.class);
    }

    public void updateStatus(final Transaction txn) {
        RBucket<String> bucket = redissonClient.getBucket(String.valueOf(txn.getTransId()));
        final String message = bucket.get();
        Transaction transaction = gson.fromJson(message, Transaction.class);
        transaction.setStatus(SUCCESS);
        bucket.set(gson.toJson(transaction), 10000, TimeUnit.SECONDS);
    }
}
