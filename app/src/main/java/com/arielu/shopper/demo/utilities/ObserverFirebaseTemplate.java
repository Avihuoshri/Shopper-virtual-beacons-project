package com.arielu.shopper.demo.utilities;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class ObserverFirebaseTemplate<T> implements Observer<T> {
    @Override
     public void onSubscribe(Disposable d) {
        //?
    }

    @Override
    public abstract void onNext(@NonNull T o);

    @Override
    public void onError(Throwable e) {
        //?
    }

    @Override
    public void onComplete() {
        //?
    }
}
