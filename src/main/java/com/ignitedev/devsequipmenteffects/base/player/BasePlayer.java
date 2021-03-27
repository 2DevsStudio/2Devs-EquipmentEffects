package com.ignitedev.devsequipmenteffects.base.player;

import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class BasePlayer {
    
    private final UUID uuid;
    private final List<BaseEffect> activeBaseEffects = new ArrayList<>();
    private transient Player player = null;
    
    public void clearPlayerActiveEffects() {
        
        if (getPlayer() != null) {
            getActiveBaseEffects().forEach(
                    baseEffect -> getPlayer().removePotionEffect(baseEffect.getPotionEffectType()));
        }
        
        activeBaseEffects.clear();
    }
    
    public Player getPlayer() {
        
        if (this.player == null) {
            this.player = Bukkit.getPlayer(this.uuid);
        }
        
        return this.player;
    }
}
