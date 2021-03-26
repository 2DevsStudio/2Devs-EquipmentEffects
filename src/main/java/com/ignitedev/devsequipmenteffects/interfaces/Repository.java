package com.ignitedev.devsequipmenteffects.interfaces;

import org.jetbrains.annotations.Nullable;

public interface Repository<T> {
    
    @Nullable T findById(String identifier);
    
    void remove(String identifier);
    
    void add(T value);
    
}
