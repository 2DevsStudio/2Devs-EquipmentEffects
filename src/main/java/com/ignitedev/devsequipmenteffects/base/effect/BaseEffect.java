package com.ignitedev.devsequipmenteffects.base.effect;

import lombok.Data;
import org.bukkit.potion.PotionEffectType;

@Data
public class BaseEffect {
    
    private final PotionEffectType potionEffectType;
    private final int amplifier;
    
}
