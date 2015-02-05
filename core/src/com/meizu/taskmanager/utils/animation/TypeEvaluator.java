package com.meizu.taskmanager.utils.animation;

public interface TypeEvaluator<T> {
    public T evaluate(float fraction, T startValue, T endValue);

}