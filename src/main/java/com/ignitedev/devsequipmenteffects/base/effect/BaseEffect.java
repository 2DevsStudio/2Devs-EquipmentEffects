package com.ignitedev.devsequipmenteffects.base.effect;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.effect.factory.BaseEffectFactory;
import com.ignitedev.devsequipmenteffects.interfaces.Applicable;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Data
public class BaseEffect implements Applicable {
    
    private final PotionEffectType potionEffectType;
    private final int amplifier;
    
    @Override
    public void apply(Player player) {
        
        player.addPotionEffect(getPotionEffect());
    }
    
    @Override
    public void unApply(Player player) {
        
        player.removePotionEffect(potionEffectType);
    }
    
    public PotionEffect getPotionEffect(BaseEffectFactory baseEffectFactory) {
        
        return baseEffectFactory.convertToPotionEffect(this);
    }
    
    public PotionEffect getPotionEffect() {
        
        return getPotionEffect(EquipmentEffects.INSTANCE.baseEffectFactories.getDefaultFactory());
    }
}
