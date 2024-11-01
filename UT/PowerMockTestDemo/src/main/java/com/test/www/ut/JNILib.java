package com.test.www.ut;



public class JNILib {

    static {
        System.loadLibrary("native-lib");
    }
    public static native int nativeMultiply(int a, int b);
    public static native int nativeAdd(int a, int b);
}
