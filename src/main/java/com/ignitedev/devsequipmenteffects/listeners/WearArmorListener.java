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
public class WearArmorListener implements Listener {
    
    private final BaseEquipmentRepository baseEquipmentRepository;
    
    @EventHandler
    public void onArmorWear(PlayerArmorChangeEvent event) {
        
        Player player = event.getPlayer();
        ItemStack newItem = event.getNewItem();
        
        if (newItem == null) {
            return; // player haven't wear any armor
        }
        
        BaseEquipment baseEquipmentByItemStack = baseEquipmentRepository.findByItemStack(newItem);
        
        if (baseEquipmentByItemStack == null) {
            return; // worn item is not in base equipment cache, that means that it is not effect item
        }
        
        if(!baseEquipmentByItemStack.isMustWear()){
            return; // player don't have to wear item to make effects work
        }
        
        baseEquipmentByItemStack.getEffectList().forEach(baseEffect -> baseEffect.unApply(player));
    }
    
}
