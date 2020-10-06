package com.arielu.shopper.demo.database;

import com.arielu.shopper.demo.utilities.DelegateonDataChangeFunction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;



import io.reactivex.rxjava3.core.ObservableEmitter;

public class ValueEventListenerTemplate implements ValueEventListener {

    protected ObservableEmitter emitter;
    protected DelegateonDataChangeFunction onDataChangeFunction;


    public ValueEventListenerTemplate(ObservableEmitter e, DelegateonDataChangeFunction onDataChangeFunction)
    {
        setEmitter(e);
        setOnDataChangeFunction(onDataChangeFunction);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Object result = this.onDataChangeFunction.onDataChange(dataSnapshot);
        emitter.onNext(result);
        emitter.onComplete();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        emitter.onError(databaseError.toException());
    }

    public void setEmitter(ObservableEmitter emit)
    {
        this.emitter = emit;
    }

    public ObservableEmitter getEmitter()
    {
        return this.emitter;
    }

    public void setOnDataChangeFunction(DelegateonDataChangeFunction func)
    {
        this.onDataChangeFunction = func;
    }

    public DelegateonDataChangeFunction getOnDataChangeFunction()
    {
        return this.onDataChangeFunction;
    }


}
