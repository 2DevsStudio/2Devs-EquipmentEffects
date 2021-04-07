package com.ignitedev.devsequipmenteffects.base.equipment;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import com.ignitedev.devsequipmenteffects.base.player.BasePlayer;
import com.ignitedev.devsequipmenteffects.interfaces.Applicable;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Data
public class BaseEquipment implements Applicable {
    
    private final String identifier;
    private final String name;
    
    private final boolean mustWear;
    private final boolean mustHoldMainHand;
    private final boolean mustHoldOffHand;
    
    private final List<BaseEffect> effectList;
    
    private final ItemStack itemStack;
    
    /*
     * this method needs checks if item have meta, lore etc
     */
    public boolean isSimilar(ItemStack targetItemStack) {
        
        if (targetItemStack == null || targetItemStack.getType().isAir()) {
            return false;
        }
        
        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemMeta targetItemMeta = targetItemStack.getItemMeta();
        Material type = itemStack.getType();
        Material targetType = targetItemStack.getType();
        
        List<Component> lore = itemMeta.lore();
        List<Component> targetLore = targetItemMeta.lore();
        
        Component displayName = itemMeta.displayName();
        Component targetDisplayName = targetItemMeta.displayName();
        
        if (type != targetType) {
            return false;
        }
        
        if (lore == null || displayName == null) {
            return false;
        }
        
        if (!lore.equals(targetLore)) {
            return false;
        }
        
        if (!displayName.equals(targetDisplayName)) {
            return false;
        }
        
        if (!itemStack.getEnchantments().equals(targetItemStack.getEnchantments())) {
            return false;
        }
        
        return itemStack.getItemFlags().equals(targetItemStack.getItemFlags());
    }
    
    @Override
    public void apply(Player player) {
        
        String playerUUID = player.getUniqueId().toString();
        BasePlayer basePlayer = EquipmentEffects.INSTANCE.basePlayerRepository.findById(playerUUID);
        
        basePlayer.getActiveEquipment().add(this);
        
        getEffectList().forEach(baseEffect -> baseEffect.apply(player));
    }
    
    @Override
    public void unApply(Player player) {
        
        String playerUUID = player.getUniqueId().toString();
        BasePlayer basePlayer = EquipmentEffects.INSTANCE.basePlayerRepository.findById(playerUUID);
        
        basePlayer.getActiveEquipment().remove(this);
        
        getEffectList().forEach(baseEffect -> baseEffect.unApply(player));
    }
}
