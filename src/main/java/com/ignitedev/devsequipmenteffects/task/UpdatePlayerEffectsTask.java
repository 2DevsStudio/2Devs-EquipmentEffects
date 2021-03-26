package com.ignitedev.devsequipmenteffects.task;

import com.google.common.collect.Lists;
import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.factory.BaseEquipmentFactory;
import com.ignitedev.devsequipmenteffects.configuration.BaseConfiguration;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class UpdatePlayerEffectsTask extends BukkitRunnable {
    
    private final BaseConfiguration baseConfiguration;
    private final EquipmentEffects equipmentEffects;
    private final BaseEquipmentFactory defaultFactory = equipmentEffects.baseEquipmentFactories.getDefaultFactory();
    
    private int cycle = 0;
    
    private List<List<Player>> partitions;
    
    @Override
    public void run() {
        
        if (cycle == -1) { // we don't want to skip index 0, so we reset on cycle -1
            // when cycles pass, we get online players again  and separate them to partitions
            cycle = baseConfiguration.getUpdatePartitionsAmount();
            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            partitions = Lists.partition(
                    onlinePlayers, onlinePlayers.size() / baseConfiguration.getUpdatePartitionsAmount());
        }
        
        List<Player> players = partitions.get(cycle);
        
        for (Player player : players) {
            PlayerInventory inventory = player.getInventory();
            ItemStack itemInMainHand = inventory.getItemInMainHand();
            ItemStack itemInOffHand = inventory.getItemInOffHand();
            
            // convert player items to base equipment list
            List<BaseEquipment> baseEquipments = defaultFactory.convertToBaseEquipments(
                    Arrays.asList(inventory.getContents()));
            
            
            if (baseEquipments.isEmpty()) {
                continue;
            }
            
            // remove player active potion effects, todo need to check if it is notable, if yes, store active potion
            //  effects and then check
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            
            iteratePlayerItems(player, itemInMainHand, itemInOffHand, baseEquipments);
        }
        
        cycle = cycle - 1;
    }
    
    private void iteratePlayerItems(Player player,
                                    ItemStack itemInMainHand,
                                    ItemStack itemInOffHand,
                                    List<BaseEquipment> baseEquipments
    ) {
        
        baseEquipments.forEach(baseEquipment -> {
            ItemStack itemStack = baseEquipment.getItemStack();
            
            if (baseEquipment.isMustHoldMainHand() && baseEquipment.isMustHoldOffHand()) {
                if (itemInMainHand.isSimilar(itemStack) ||
                    itemInOffHand.isSimilar(itemStack)) {
                    
                    baseEquipment.getEffectList().forEach(baseEffect -> baseEffect.apply(player));
                }
            } else if (baseEquipment.isMustHoldMainHand()) {
                if (itemInMainHand.isSimilar(itemStack)) {
                    baseEquipment.getEffectList().forEach(baseEffect -> baseEffect.apply(player));
                }
            } else if (baseEquipment.isMustHoldOffHand() && itemInOffHand.isSimilar(itemStack)) {
                if (itemInOffHand.isSimilar(itemStack)) {
                    baseEquipment.getEffectList().forEach(baseEffect -> baseEffect.apply(player));
                }
            } else if (!baseEquipment.isMustWear()) {
                baseEquipment.getEffectList().forEach(baseEffect -> baseEffect.apply(player));
            }
        });
    }
}
