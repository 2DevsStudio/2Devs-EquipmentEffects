package com.ignitedev.devsequipmenteffects.configuration;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import com.ignitedev.devsequipmenteffects.base.effect.BaseTrigger;
import com.ignitedev.devsequipmenteffects.base.effect.factory.BaseEffectFactory;
import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.repository.BaseEquipmentRepository;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

@Data
public class BaseConfiguration {
    
    private final BaseEquipmentRepository baseEquipmentRepository;
    private final EquipmentEffects equipmentEffects;
    public File itemsDirectory;
    private int taskScheduleTimeTicks;
    private int updatePartitionsAmount;
    private int partitionMinimumPlayersMultiplier;
    private String adminCommandUsage;
    private String reloadMessage;
    private String noPermissions;
    
    public void initialize(FileConfiguration fileConfiguration) {
        
        partitionMinimumPlayersMultiplier = fileConfiguration.getInt("partition-minimum-players-multiplier");
        taskScheduleTimeTicks = fileConfiguration.getInt("task-schedule-time");
        updatePartitionsAmount = fileConfiguration.getInt("update-partitions-amount");
        
        adminCommandUsage = fileConfiguration.getString("messages.admin-command-usage");
        reloadMessage = fileConfiguration.getString("messages.reload");
        noPermissions = fileConfiguration.getString("messages.no-permissions");
        
        itemsDirectory = new File(equipmentEffects.getDataFolder(), "/Items");
        
        loadDefaultItem();
        loadEffectItems();
    }
    
    private void loadDefaultItem() {
        
        File defaultItemFile = new File(itemsDirectory.getPath(), "default.yml");
        
        if (!defaultItemFile.exists()) {
            equipmentEffects.saveResource("Items/default.yml", true);
        }
    }
    
    private void loadEffectItems() {
        
        BaseEffectFactory baseEffectFactory = equipmentEffects.baseEffectFactories.getDefaultFactory();
        File[] files = itemsDirectory.listFiles();
        baseEquipmentRepository.getBaseEquipmentCache().clear();
        
        for (File file : files) {
            YamlConfiguration fileYaml = YamlConfiguration.loadConfiguration(file);
            
            String identifier = fileYaml.getString("id");
            
            if (baseEquipmentRepository.findById(identifier) != null) {
                baseEquipmentRepository.remove(identifier);
            }
            
            String name = fileYaml.getString("name");
            boolean mustWear = fileYaml.getBoolean("must-wear");
            boolean mustHoldMainHand = fileYaml.getBoolean("must-hold-mainhand");
            boolean mustHoldOffHand = fileYaml.getBoolean("must-hold-offhand");
            List<BaseEffect> applicableEffects = baseEffectFactory.convertToBaseEffects(
                    fileYaml.getStringList("applicable-effects"));
            ItemStack itemStack = fileYaml.getItemStack("itemstack");
            
            // triggers
            
            List<String> enableCommands = fileYaml.getStringList("trigger-interactions.trigger-enable.command");
            List<String> disableCommands = fileYaml.getStringList("trigger-interactions.trigger-disable.command");
            
            BaseTrigger baseTrigger = new BaseTrigger(enableCommands, disableCommands);
            
            baseEquipmentRepository.add(
                    new BaseEquipment(identifier, name, mustWear, mustHoldMainHand, mustHoldOffHand, applicableEffects,
                            baseTrigger, itemStack
                    ));
        }
    }
}
