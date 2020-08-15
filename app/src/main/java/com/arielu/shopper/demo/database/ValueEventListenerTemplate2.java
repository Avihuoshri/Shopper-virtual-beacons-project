package com.arielu.shopper.demo.database;

import com.arielu.shopper.demo.utilities.DelegateonDataChangeFunction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.rxjava3.core.ObservableEmitter;

public class ValueEventListenerTemplate2 implements ValueEventListener {

    protected DelegateonDataChangeFunction onDataChangeFunction;
    protected Firebase2.Task task;


    public ValueEventListenerTemplate2(DelegateonDataChangeFunction onDataChangeFunction, Firebase2.Task task)
    {
        setOnDataChangeFunction(onDataChangeFunction);
        setTask(task);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Object result = this.onDataChangeFunction.onDataChange(dataSnapshot);
        task.execute(result);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) { }

    public void setOnDataChangeFunction(DelegateonDataChangeFunction func)
    {
        this.onDataChangeFunction = func;
    }

    public DelegateonDataChangeFunction getOnDataChangeFunction()
    {
        return this.onDataChangeFunction;
    }

    public Firebase2.Task getTask() {
        return task;
    }

    public void setTask(Firebase2.Task task) {
        this.task = task;
    }
}
