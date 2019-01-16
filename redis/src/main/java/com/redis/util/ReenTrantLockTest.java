package com.redis.util;

import sun.misc.Unsafe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReenTrantLockTest {
    public static void main(String[] args) {
        Unsafe unsafe = Unsafe.getUnsafe();
        System.out.println("start do it ");
        unsafe.park(false, 5l*1000*1000*1000);
        System.out.println("end ...");
    }
}
