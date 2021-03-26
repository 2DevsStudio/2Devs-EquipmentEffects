package com.ignitedev.devsequipmenteffects.base.effect;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.effect.factory.BaseEffectFactory;
import com.ignitedev.devsequipmenteffects.interfaces.Applicable;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@Data
public class BaseEffect implements Applicable {
    
    private final PotionEffectType potionEffectType;
    private final int amplifier;
    
    @Override
    public void apply(Player player) {
        
        BaseEffectFactory defaultFactory = EquipmentEffects.INSTANCE.baseEffectFactories.getDefaultFactory();
        
        player.addPotionEffect(defaultFactory.convertToPotionEffect(this));
    }
}
