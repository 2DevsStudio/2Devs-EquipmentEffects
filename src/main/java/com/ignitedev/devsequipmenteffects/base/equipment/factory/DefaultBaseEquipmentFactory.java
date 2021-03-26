package com.ignitedev.devsequipmenteffects.base.equipment.factory;

import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.repository.BaseEquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/*
 * If you thinking for what is that wrapping, this wrapping is for future
 * expand to be sure that i'm able for example to add support of nbt holding ;)
 */
@RequiredArgsConstructor
public class DefaultBaseEquipmentFactory implements BaseEquipmentFactory {
    
    private final BaseEquipmentRepository baseEquipmentRepository;
    
    @Override
    public ItemStack convertToItemStack(BaseEquipment baseEquipment) {
        
        return baseEquipment.getItemStack();
    }
    
    @Override
    public @Nullable BaseEquipment convertToBaseEquipment(ItemStack itemStack) {
        
        return baseEquipmentRepository.findByItemStack(itemStack);
    }
    
    @Override
    public List<BaseEquipment> convertToBaseEquipments(List<ItemStack> itemStacks) {
        
        List<BaseEquipment> baseEquipments = new ArrayList<>();
        
        for (ItemStack itemStack : itemStacks) {
            baseEquipments.add(convertToBaseEquipment(itemStack));
        }
        
        return baseEquipments;
    }
}
