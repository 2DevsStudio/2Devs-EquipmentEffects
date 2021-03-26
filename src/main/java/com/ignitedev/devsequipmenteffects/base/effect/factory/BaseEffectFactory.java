package com.ignitedev.devsequipmenteffects.base.effect.factory;

import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import com.ignitedev.devsequipmenteffects.factory.Factory;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public interface BaseEffectFactory extends Factory {
    
    BaseEffect convertToBaseEffect(String effect);
    
    List<BaseEffect> convertToBaseEffects(List<String> effects);
    
    PotionEffect convertToPotionEffect(BaseEffect baseEffect);
    
}
