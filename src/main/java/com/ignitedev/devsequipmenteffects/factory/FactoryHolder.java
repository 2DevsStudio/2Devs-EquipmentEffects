package com.ignitedev.devsequipmenteffects.factory;

import java.util.HashMap;
import java.util.Map;

public abstract class FactoryHolder<T extends Factory> {
    
    private final Map<String, T> factoryMap = new HashMap<>();
    
    public void register(String identifier, T factory) {
        
        factoryMap.putIfAbsent(identifier, factory);
    }
    
    public T getFactory(String identifier) {
        
        return factoryMap.get(identifier);
    }
    
    public T getDefaultFactory() {
        
        return factoryMap.get("DEFAULT");
    }
    
}
