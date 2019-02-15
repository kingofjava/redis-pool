package com.redis.util.pool;

public enum POSITION {
    BEFORE(1, "before"),
    AFTER(2, "after");
    private int flag;
    private String desc;

    POSITION(int flag, String desc) {
        this.flag = flag;
        this.desc = desc;
    }

    public int getFlag() {
        return flag;
    }

    public String getDesc() {
        return desc;
    }
}
