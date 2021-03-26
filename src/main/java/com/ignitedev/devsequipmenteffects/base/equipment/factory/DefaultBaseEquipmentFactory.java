package com.ignitedev.devsequipmenteffects.base.equipment.factory;

import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import org.bukkit.inventory.ItemStack;

/*
 * If you thinking for what is that wrapping, this wrapping is for future
 * expand to be sure that i'm able for example to add support of nbt holding ;)
 */
public class DefaultBaseEquipmentFactory implements BaseEquipmentFactory {
    
    @Override
    public ItemStack convertToItemStack(BaseEquipment baseEquipment) {
        
        return baseEquipment.getItemStack();
    }
}
