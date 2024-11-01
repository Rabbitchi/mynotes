package com.test.www.service;

import org.junit.Test;
import com.test.www.ut.JNILib;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value={JNILib.class, UseMockDemo.class,System.class})
@PowerMockIgnore({"javax.management.*","javax.net.ssl.*","jdk.internal.reflect.*"})
public class UseMockDemoTest {

    UseMockDemo useMockDemo =  new UseMockDemo();;

    @Test
    public  void testUseMock() {
        PowerMockito.mockStatic(System.class);
        PowerMockito.mockStatic(JNILib.class);
        when(JNILib.nativeMultiply(anyInt(),anyInt())).thenReturn(6);
        when(JNILib.nativeAdd(anyInt(),anyInt())).thenReturn(5);

        assertEquals(6, useMockDemo.useNativeMultiply(2,3));
        assertEquals(5, useMockDemo.useNativeAdd(2,3));
    }
}
