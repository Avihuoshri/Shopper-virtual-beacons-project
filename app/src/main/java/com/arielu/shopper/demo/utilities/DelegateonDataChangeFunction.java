package com.arielu.shopper.demo.utilities;

import com.google.firebase.database.DataSnapshot;

import io.reactivex.rxjava3.core.ObservableEmitter;

public interface DelegateonDataChangeFunction {
    public Object onDataChange(DataSnapshot dataSnapshot);
}
