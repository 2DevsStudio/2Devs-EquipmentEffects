package com.ignitedev.devsequipmenteffects.task;

import com.google.common.collect.Lists;
import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.factory.BaseEquipmentFactory;
import com.ignitedev.devsequipmenteffects.base.player.BasePlayer;
import com.ignitedev.devsequipmenteffects.base.player.repository.BasePlayerRepository;
import com.ignitedev.devsequipmenteffects.configuration.BaseConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.Validate;
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
    private final BasePlayerRepository basePlayerRepository;
    
    private int cycle = -1;
    
    private List<List<Player>> partitions;
    
    @Override
    public void run() {
        
        BaseEquipmentFactory defaultFactory = equipmentEffects.baseEquipmentFactories.getDefaultFactory();
        
        if (cycle == -1) {
            // we don't want to skip index 0, so we reset on cycle -1
            // when cycles pass, we get online players again  and separate them to partitions
            
            List<Player> onlinePlayersList = new ArrayList<>(Bukkit.getOnlinePlayers());
            
            if (onlinePlayersList.isEmpty()) {
                return;
            }
            
            if (onlinePlayersList.size() <
                baseConfiguration.getUpdatePartitionsAmount() *
                baseConfiguration.getPartitionMinimumPlayersMultiplier()) {
                
                // if player amount is lower than (partition amount * multiplier), update inventories instantly for
                // everyone at the same time
                
                iterateThroughPlayers(defaultFactory, new ArrayList<>(onlinePlayersList));
                return;
            }
            
            cycle = baseConfiguration.getUpdatePartitionsAmount();
            
            partitions = Lists.partition(
                    onlinePlayersList, onlinePlayersList.size() / baseConfiguration.getUpdatePartitionsAmount());
        }
        
        iterateThroughPlayers(defaultFactory, partitions.get(cycle));
        
        cycle = cycle - 1;
    }
    
    private void iterateThroughPlayers(BaseEquipmentFactory defaultFactory, List<Player> players) {
        
        for (Player player : players) {
            
            BasePlayer basePlayer = basePlayerRepository.findById(player.getUniqueId().toString());
            
            Validate.notNull(basePlayer, "Base Player is null, Check your console for possible exception ");
            
            if (!player.isOnline()) {
                basePlayer.getActiveBaseEffects().clear();
                continue;
            }
            
            PlayerInventory inventory = player.getInventory();
            ItemStack itemInMainHand = inventory.getItemInMainHand();
            ItemStack itemInOffHand = inventory.getItemInOffHand();
            
            // convert player items to base equipment list
            List<BaseEquipment> baseEquipments = defaultFactory.convertToBaseEquipments(
                    Arrays.asList(inventory.getContents()));
            
            basePlayer.getActiveBaseEffects()
                    .forEach(potionEffect -> player.removePotionEffect(potionEffect.getPotionEffectType()));
            basePlayer.getActiveBaseEffects().clear();
            
            if (!baseEquipments.isEmpty()) {
                
                iteratePlayerItems(player, itemInMainHand, itemInOffHand, baseEquipments);
            }
        }
    }
    
    private void iteratePlayerItems(Player player,
                                    ItemStack itemInMainHand,
                                    ItemStack itemInOffHand,
                                    List<BaseEquipment> baseEquipments
    ) {
        
        baseEquipments.forEach(baseEquipment -> {
            ItemStack itemStack = baseEquipment.getItemStack();
            
            if (!baseEquipment.isMustWear()) {
                if (baseEquipment.isMustHoldMainHand() && baseEquipment.isMustHoldOffHand()) {
                    if (itemInMainHand.isSimilar(itemStack) || itemInOffHand.isSimilar(itemStack)) {
                        
                        baseEquipment.getEffectList().forEach(baseEffect -> baseEffect.apply(player));
                    }
                } else if (baseEquipment.isMustHoldMainHand()) {
                    if (itemInMainHand.isSimilar(itemStack)) {
                        baseEquipment.getEffectList().forEach(baseEffect -> baseEffect.apply(player));
                    }
                } else if (baseEquipment.isMustHoldOffHand()) {
                    if (itemInOffHand.isSimilar(itemStack)) {
                        baseEquipment.getEffectList().forEach(baseEffect -> baseEffect.apply(player));
                    }
                } else if (!baseEquipment.isMustWear()) {
                    baseEquipment.getEffectList().forEach(baseEffect -> baseEffect.apply(player));
                }
            } else {
                ItemStack[] armorContents = player.getInventory().getArmorContents();
                
                for (ItemStack armorContent : armorContents) {
                    
                    if (armorContent == null) {
                        continue;
                    }
                    
                    if (armorContent.isSimilar(itemStack)) {
                        baseEquipment.getEffectList().forEach(baseEffect -> baseEffect.apply(player));
                        break;
                    }
                }
            }
        });
    }
}
