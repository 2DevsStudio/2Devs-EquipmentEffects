package com.ignitedev.devsequipmenteffects.base.effect.factory;

import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import org.apache.commons.lang.Validate;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultBaseEffectFactory implements BaseEffectFactory {
    
    @Override
    public BaseEffect convertToBaseEffect(String string) {
        
        String[] split = string.split(":");
        PotionEffectType potionEffectType = PotionEffectType.getByName(split[0]);
        int amplifier = Integer.parseInt(split[1]);
        
        Validate.notNull(potionEffectType, "PotionEffectType for " + Arrays.toString(split) + " is null");
        
        return new BaseEffect(potionEffectType, amplifier);
    }
    
    @Override
    public List<BaseEffect> convertToBaseEffects(List<String> effects) {
        
        List<BaseEffect> effectList = new ArrayList<>();
        
        for (String effect : effects) {
            effectList.add(convertToBaseEffect(effect));
        }
        
        return effectList;
    }
    
    @Override
    public PotionEffect convertToPotionEffect(BaseEffect baseEffect) {
        
        return new PotionEffect(baseEffect.getPotionEffectType(), Integer.MAX_VALUE, baseEffect.getAmplifier());
    }
}
