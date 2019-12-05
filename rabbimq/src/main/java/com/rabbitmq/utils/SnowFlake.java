package com.rabbitmq.utils;



public class SnowFlake {
    private final long workerId;
    private final long snsEpoch = 1330328109047L;
    private long sequence = 0L;
    private final long workerIdBits = 10L;
    private final long maxWorkerId = -1L ^ -1L << this.workerIdBits;
    private final long sequenceBits = 12L;
    private final long workerIdShift = this.sequenceBits;
    private final long timestampLeftShift = this.sequenceBits + this.workerIdBits;
    private final long sequenceMask = -1L ^ -1L << this.sequenceBits;
    private long lastTimestamp = -1L;


    public SnowFlake(long workerId) {
        super();
        if (workerId > this.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1 & this.sequenceMask;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }

        if (timestamp < this.lastTimestamp) {
            try {
                throw new Exception(String.format("Clock moved backwards.Refusing to generate id for %d milliseconds", (this.lastTimestamp - timestamp)));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }


        this.lastTimestamp = timestamp;
        return timestamp - this.snsEpoch << this.timestampLeftShift | this.workerId << this.workerIdShift | this.sequence;
    }

    /**
     * 保证返回的毫秒数在参数之后
     *
     * @param lastTimestamp
     * @return
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    /**
     * 获得系统当前毫秒数
     *
     * @return
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }


    public static void main(String[] args) {

    }
}
