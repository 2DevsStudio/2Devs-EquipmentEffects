package com.ignitedev.devsequipmenteffects.base.equipment;

import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

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
}
