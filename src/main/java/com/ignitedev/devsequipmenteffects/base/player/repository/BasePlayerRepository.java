package com.ignitedev.devsequipmenteffects.base.player.repository;

import com.ignitedev.devsequipmenteffects.base.player.BasePlayer;
import com.ignitedev.devsequipmenteffects.interfaces.Repository;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class BasePlayerRepository implements Repository<BasePlayer> {
    
    private final Map<String, BasePlayer> basePlayerCache = new ConcurrentHashMap<>();
    
    @Override
    public @Nullable BasePlayer findById(String identifier) {
        
        BasePlayer basePlayer = basePlayerCache.get(identifier);
        
        if (basePlayer == null) {
            BasePlayer newBasePlayer = new BasePlayer(UUID.fromString(identifier));
            
            add(newBasePlayer);
            
            return newBasePlayer;
        }
        
        return basePlayer;
    }
    
    @Override
    public void remove(String identifier) {
        
        basePlayerCache.remove(identifier);
    }
    
    @Override
    public void add(BasePlayer value) {
        
        basePlayerCache.put(value.getUuid().toString(), value);
    }
}
