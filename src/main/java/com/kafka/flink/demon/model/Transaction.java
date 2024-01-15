package com.kafka.flink.demon.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Transaction implements Serializable {
    private long transId;
    private String userId;
    private Long amount;
    private String transTime;
    private String bank;
    private String status;
}
