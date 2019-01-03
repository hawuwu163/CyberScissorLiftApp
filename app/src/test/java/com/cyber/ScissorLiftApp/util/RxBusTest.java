package com.cyber.ScissorLiftApp.util;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Observable;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.util
 * @date 2018/12/29 14:27
 * @Description:
 * PowerMock有两个重要的注解：@PrepareForTest注解和@RunWith注解是结合使用的，不要单独使用它们中的任何一个，
 * 否则不起作用。当使用PowerMock去mock静态，final或者私有方法时，需要加上这两个注解。
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class RxBusTest {

    @Test
    public void test(){
        System.out.println("hello world!");
        Observable<String> observable = RxBus.getInstance().register(String.class);
        observable.map(s -> {
            try {
                int v = Integer.valueOf(s);
                System.out.println("map变换成功, source = " + s);
                return v;
            } catch (Exception e) {
                System.out.println("map变换失败, source = " + s);
                return s;
            }
        }).subscribe(value -> System.out.println("订阅 " + value));

        RxBus.getInstance().post("888");
        RxBus.getInstance().post("发发发");
        RxBus.getInstance().unregister(String.class, observable);
    }

}