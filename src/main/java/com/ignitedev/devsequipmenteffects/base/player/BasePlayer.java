package com.ignitedev.devsequipmenteffects.base.player;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.factory.BaseEquipmentFactory;
import com.ignitedev.devsequipmenteffects.util.BaseUtil;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
public class BasePlayer {
    
    private final UUID uuid;
    private final List<BaseEquipment> activeEquipment = new ArrayList<>();
    private transient Player player = null;
    
    public void clearPlayerActiveEquipment() {
        
        if (getPlayer() != null) {
            List<BaseEquipment> equipmentToUnapply = new ArrayList<>(getActiveEquipment());
            
            equipmentToUnapply.forEach(baseEquipment -> baseEquipment.unApply(getPlayer()));
        }
        
        activeEquipment.clear();
    }
    
    public Player getPlayer() {
        
        if (this.player == null) {
            this.player = Bukkit.getPlayer(this.uuid);
        }
        
        return this.player;
    }
    
    public void updatePlayerActiveEffects() {
        
        if (getPlayer() == null) {
            return;
        }
        
        BaseEquipmentFactory defaultFactory = EquipmentEffects.INSTANCE.baseEquipmentFactories.getDefaultFactory();
        
        // convert player items to base equipment list
        List<BaseEquipment> baseEquipments = defaultFactory.convertToBaseEquipments(
                Arrays.asList(player.getInventory().getContents()));
        
        if (!baseEquipments.isEmpty()) {
            
            List<BaseEquipment> equipmentToApply = BaseUtil.findPlayerApplicableBaseEquipment(player, baseEquipments);
            
            List<BaseEquipment> equipmentToUnapply = new ArrayList<>();
            
            // get equipment to unapply
            getActiveEquipment().forEach(baseEquipment -> {
                if (!equipmentToApply.contains(baseEquipment)) {
                    equipmentToUnapply.add(baseEquipment);
                }
            });
            
            equipmentToUnapply.forEach(baseEquipment -> baseEquipment.unApply(player));
            equipmentToApply.forEach(baseEquipment -> {
                baseEquipment.apply(player);
            });
        }
    }
    
    
}
