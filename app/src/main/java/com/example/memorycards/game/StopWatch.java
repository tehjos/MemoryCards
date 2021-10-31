package com.example.memorycards.game;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import timber.log.Timber;

public class StopWatch {
    public static final int MAX_TIME_IN_SECONDS = 3600;
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
                            Timber.d("reset()");
                            return Observable.just(0L);
                        case START:
                            Timber.d("start()");
                            return Observable.interval(
                                    0,
                                    1,
                                    TimeUnit.SECONDS
                            ).take(MAX_TIME_IN_SECONDS);
                        case STOP:
                            Timber.d("stop()");
                            return Observable.just(-1L);
                        default:
                            return Observable.just(0L);
                    }
                })
                .filter(time -> time >= 0)
                .hide();
    }

    public void start() {
        startSubject.onNext(START);
    }

    public void stop() {
        startSubject.onNext(STOP);
    }

    public void reset() {
        startSubject.onNext(RESET);
    }
}
