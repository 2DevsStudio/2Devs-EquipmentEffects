package com.ignitedev.devsequipmenteffects.base.equipment.factory;

import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.factory.Factory;
import org.bukkit.inventory.ItemStack;

public interface BaseEquipmentFactory extends Factory {
    
    ItemStack convertToItemStack(BaseEquipment baseEquipment);
    
}
