package com.example.memorycards;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import timber.log.Timber;

public class StopWatch {
    private static final int RESET = 0;
    private static final int START = 1;
    private static final int STOP = 2;

    private final BehaviorSubject<Integer> startSubject;

    public StopWatch() {
        startSubject = BehaviorSubject.createDefault(RESET);
    }

    public Observable<Long> observeTime() {
        return startSubject
                .distinctUntilChanged()
                .switchMap(state -> {
                    switch (state) {
                        case RESET:
                            return Observable.just(0L);
                        case START:
                            return Observable.interval(0, 1, TimeUnit.SECONDS);
                        case STOP:
                            return Observable.just(-1L);
                        default:
                            return Observable.just(0L);
                    }
                })
                .hide();
    }

    public void start() {
        Timber.d("start()");
        startSubject.onNext(START);
    }

    public void stop() {
        Timber.d("stop()");
        startSubject.onNext(STOP);
    }

    public void reset() {
        Timber.d("reset()");
        startSubject.onNext(RESET);
    }
}
