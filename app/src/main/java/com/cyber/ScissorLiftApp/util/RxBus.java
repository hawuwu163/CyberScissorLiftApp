package com.cyber.ScissorLiftApp.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * creat_user: Heshuai
 * creat_date: 2018/12/29 13:52
 * description: https://juejin.im/entry/58ff2e26a0bb9f0065d2c5f2
 * RxJava实现一个发布/订阅事件总线：RxBus
 * 利用 PublishSubject的特性：与普通的Subject不同，在订阅时并不立即触发订阅事件，
 * 而是允许我们在任意时刻手动调用onNext(),onError(),onCompleted来触发事件。
 **/
public class RxBus {
    private static final String TAG = "RxBus";
    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    private RxBus() {

    }

    @NonNull
    public static RxBus getInstance() {
        return Holder.instance;
    }

    @NonNull
    public <T> Observable<T> register(@NonNull Class<T> clz) {
        return register(clz.getName());
    }

    @NonNull
    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        Subject<T> subject = PublishSubject.create();
        subjectList.add(subject);
        Log.d(TAG, "register: 注册到rxbus:"+tag.getClass().getName());

        return subject;
    }

    public <T> void unregister(@NonNull Class<T> clz, @NonNull Observable observable) {
        Log.d(TAG, "unregister: class: "+clz.getName()+"observable :"+observable.getClass().getName());
        unregister(clz.getName(), observable);
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove(observable);
            if (subjects.isEmpty()) {
                subjectMapper.remove(tag);
                Log.d(TAG, "unregister: 从rxbus取消注册:"+tag.getClass().getName());
            }
        }
    }

    public void post(@NonNull Object content) {
        Log.d(TAG, "post: content:"+content.getClass().getName());
        post(content.getClass().getName(), content);
    }

    public void post(@NonNull Object tag, @NonNull Object content) {
        Log.d(TAG, "post, tag:"+tag.getClass().getName()+" content:"+content.getClass().getName());
        List<Subject> subjects = subjectMapper.get(tag);
        if (!subjects.isEmpty()) {
            for (Subject subject : subjects) {
                subject.onNext(content);
            }
        }
    }

    private static class Holder {
        private static RxBus instance = new RxBus();
    }
}
