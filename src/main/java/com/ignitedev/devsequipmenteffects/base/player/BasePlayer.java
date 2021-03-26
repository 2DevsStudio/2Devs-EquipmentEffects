package com.ignitedev.devsequipmenteffects.base.player;

import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class BasePlayer {
    
    private final UUID uuid;
    private final List<BaseEffect> activeBaseEffects = new ArrayList<>();
    
}
