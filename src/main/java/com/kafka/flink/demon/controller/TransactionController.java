package com.kafka.flink.demon.controller;

import com.kafka.flink.demon.model.Transaction;
import com.kafka.flink.demon.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/txn")
    public ResponseEntity<Transaction> processTxn(@RequestBody final Transaction txn) {
        return ResponseEntity.of(Optional.of(transactionService.processTxn(txn)));
    }

    @GetMapping("/txn/{id}")
    public ResponseEntity<Transaction> getStatus(@PathVariable(value = "id", required = true) final Long transId) {
        return ResponseEntity.of(Optional.of(transactionService.getFinalStatus(transId)));
    }
}

