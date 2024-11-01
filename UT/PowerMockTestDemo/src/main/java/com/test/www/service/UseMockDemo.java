package com.test.www.service;

import com.test.www.ut.JNILib;



public class UseMockDemo {

    public int useNativeMultiply(int a,int b) {
        return JNILib.nativeMultiply(a, b);
    }

    public int useNativeAdd(int a, int b) {
        return JNILib.nativeAdd(a, b);
    }

}
