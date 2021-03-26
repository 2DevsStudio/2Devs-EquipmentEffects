package com.ignitedev.devsequipmenteffects.configuration;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import com.ignitedev.devsequipmenteffects.base.effect.factory.BaseEffectFactory;
import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.repository.BaseEquipmentRepository;
import lombok.Data;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public class BaseConfiguration {
    
    private final FileConfiguration fileConfiguration;
    private final BaseEquipmentRepository baseEquipmentRepository;
    private final EquipmentEffects equipmentEffects;
    
    private int taskScheduleTimeTicks;
    private int updatePartitionsAmount;
    private int partitionMinimumPlayersMultiplier;
    
    private String adminCommandUsage;
    
    public void initialize() {
        
        partitionMinimumPlayersMultiplier = fileConfiguration.getInt("partition-minimum-players-multiplier");
        taskScheduleTimeTicks = fileConfiguration.getInt("task-schedule-time");
        updatePartitionsAmount = fileConfiguration.getInt("update-partitions-amount");
        
        adminCommandUsage = fileConfiguration.getString("messages.admin-command-usage");
        
        loadEffectItems();
    }
    
    private void loadEffectItems() {
        
        BaseEffectFactory baseEffectFactory = equipmentEffects.baseEffectFactories.getDefaultFactory();
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("effect-items");
        
        Validate.notNull(configurationSection, "effect-items section is incorrect!");
        
        configurationSection.getKeys(false).forEach(key -> {
            String identifier = configurationSection.getString(key + ".id");
            
            if (baseEquipmentRepository.findById(identifier) != null) {
                baseEquipmentRepository.remove(identifier);
            }
            
            String name = configurationSection.getString(key + ".name");
            boolean mustWear = configurationSection.getBoolean(key + ".must-wear");
            boolean mustHoldMainHand = configurationSection.getBoolean(key + ".must-hold-mainhand");
            boolean mustHoldOffHand = configurationSection.getBoolean(key + ".must-hold-offhand");
            List<BaseEffect> applicableEffects = baseEffectFactory.convertToBaseEffects(
                    configurationSection.getStringList(key + ".applicable-effects"));
            ItemStack itemStack = configurationSection.getItemStack(key + ".itemstack");
            
            
            baseEquipmentRepository.add(
                    new BaseEquipment(identifier, name, mustWear, mustHoldMainHand, mustHoldOffHand, applicableEffects,
                            itemStack
                    ));
        });
        
        
    }
    
}
