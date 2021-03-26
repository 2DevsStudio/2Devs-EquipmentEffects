package com.ignitedev.devsequipmenteffects.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.repository.BaseEquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ArmorTakeOffListener implements Listener {
    
    private final BaseEquipmentRepository baseEquipmentRepository;
    
    @EventHandler
    public void onArmorWear(PlayerArmorChangeEvent event) {
        
        Player player = event.getPlayer();
        ItemStack oldItem = event.getOldItem();
        
        if (oldItem == null) {
            return; // player haven't any armor before
        }
        
        BaseEquipment baseEquipmentByItemStack = baseEquipmentRepository.findByItemStack(oldItem);
        
        if (baseEquipmentByItemStack == null) {
            return; // worn item is not in base equipment cache, that means that it is not effect item
        }
        
        baseEquipmentByItemStack.getEffectList().forEach(baseEffect -> baseEffect.apply(player));
    }
    
}
