package com.ignitedev.devsequipmenteffects.util;

import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class BaseUtil {
    
    public static Component colorComponent(String toColor) {
        
        return LegacyComponentSerializer.legacyAmpersand().deserialize(toColor).asComponent();
    }
    
    public static List<BaseEquipment> findPlayerApplicableBaseEquipment(Player player,
                                                                        List<BaseEquipment> baseEquipments
    ) {
        
        List<BaseEquipment> appliedBaseEquipment = new ArrayList<>();
        
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
        
        baseEquipments.forEach(baseEquipment -> {
            
            boolean shouldApply = false;
            
            if (baseEquipment == null) {
                return;
            }
            
            if (!baseEquipment.isMustWear()) {
                if (baseEquipment.isMustHoldMainHand() && baseEquipment.isMustHoldOffHand()) {
                    if (baseEquipment.isSimilar(itemInMainHand) || baseEquipment.isSimilar(itemInOffHand)) {
                        shouldApply = true;
                    }
                } else if (baseEquipment.isMustHoldMainHand()) {
                    if (baseEquipment.isSimilar(itemInMainHand)) {
                        shouldApply = true;
                    }
                } else if (baseEquipment.isMustHoldOffHand()) {
                    if (baseEquipment.isSimilar(itemInOffHand)) {
                        shouldApply = true;
                    }
                } else if (!baseEquipment.isMustWear()) {
                    shouldApply = true;
                }
            } else {
                for (ItemStack armorContent : player.getInventory().getArmorContents()) {
                    if (baseEquipment.isSimilar(armorContent)) {
                        shouldApply = true;
                        break;
                    }
                }
            }
            if (shouldApply) {
                appliedBaseEquipment.add(baseEquipment);
            }
        });
        
        return appliedBaseEquipment;
    }
}
