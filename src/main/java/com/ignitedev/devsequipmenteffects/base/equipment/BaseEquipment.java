package com.ignitedev.devsequipmenteffects.base.equipment;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import com.ignitedev.devsequipmenteffects.base.effect.BaseTrigger;
import com.ignitedev.devsequipmenteffects.base.player.BasePlayer;
import com.ignitedev.devsequipmenteffects.interfaces.Applicable;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
public class BaseEquipment implements Applicable {
    
    private final String identifier;
    private final String name;
    
    private final boolean mustWear;
    private final boolean mustHoldMainHand;
    private final boolean mustHoldOffHand;
    
    private final List<BaseEffect> effectList;
    private final BaseTrigger baseTrigger;
    
    private final ItemStack itemStack;
    
    /*
     * this method needs checks if item have meta, lore etc
     */
    public boolean isSimilar(@Nullable ItemStack targetItemStack) {
        
        if (targetItemStack == null) {
            return false;
        } else {
            if (targetItemStack == this.itemStack) {
                return true;
            } else {
                Material comparisonType = this.itemStack.getType().isLegacy() ?
                                          Bukkit.getUnsafe().fromLegacy(this.itemStack.getData(), true) :
                                          this.itemStack.getType();
                
                return comparisonType == targetItemStack.getType() &&
                       this.itemStack.hasItemMeta() == targetItemStack.hasItemMeta() &&
                       (!this.itemStack.hasItemMeta() ||
                        Bukkit.getItemFactory().equals(this.itemStack.getItemMeta(), targetItemStack.getItemMeta())
                       );
            }
        }
    }
    
    @Override
    public void apply(Player player) {
        
        String playerUUID = player.getUniqueId().toString();
        BasePlayer basePlayer = EquipmentEffects.INSTANCE.basePlayerRepository.findById(playerUUID);
        
        getBaseTrigger().apply(player);
        
        basePlayer.getActiveEquipment().add(this);
        
        getEffectList().forEach(baseEffect -> baseEffect.apply(player));
    }
    
    @Override
    public void unApply(Player player) {
        
        String playerUUID = player.getUniqueId().toString();
        BasePlayer basePlayer = EquipmentEffects.INSTANCE.basePlayerRepository.findById(playerUUID);
        
        basePlayer.getActiveEquipment().remove(this);
        
        getBaseTrigger().unApply(player);
        getEffectList().forEach(baseEffect -> baseEffect.unApply(player));
    }
}
