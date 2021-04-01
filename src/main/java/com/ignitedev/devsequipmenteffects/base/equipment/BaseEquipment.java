package com.ignitedev.devsequipmenteffects.base.equipment;

import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Data
public class BaseEquipment {
    
    private final String identifier;
    private final String name;
    
    private final boolean mustWear;
    private final boolean mustHoldMainHand;
    private final boolean mustHoldOffHand;
    
    private final List<BaseEffect> effectList;
    
    private final ItemStack itemStack;
    
    public boolean isSimilar(ItemStack targetItemStack) {
        
        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemMeta targetItemMeta = targetItemStack.getItemMeta();
        Material type = itemStack.getType();
        Material targetType = targetItemStack.getType();
        
        if (type != targetType) {
            return false;
        }
        
        if (!itemMeta.equals(targetItemMeta)) {
            return false;
        }
        
        if (!itemStack.getEnchantments().equals(targetItemStack.getEnchantments())) {
            return false;
        }
        
        return itemStack.getItemFlags().equals(targetItemStack.getItemFlags());
    }
}
