package com.ignitedev.devsequipmenteffects;

import com.ignitedev.devsequipmenteffects.base.effect.factory.BaseEffectFactories;
import com.ignitedev.devsequipmenteffects.base.effect.factory.DefaultBaseEffectFactory;
import com.ignitedev.devsequipmenteffects.base.equipment.factory.BaseEquipmentFactories;
import com.ignitedev.devsequipmenteffects.base.equipment.factory.DefaultBaseEquipmentFactory;
import com.ignitedev.devsequipmenteffects.base.equipment.repository.BaseEquipmentRepository;
import com.ignitedev.devsequipmenteffects.base.player.repository.BasePlayerRepository;
import com.ignitedev.devsequipmenteffects.command.EquipmentEffectsAdminCommand;
import com.ignitedev.devsequipmenteffects.configuration.BaseConfiguration;
import com.ignitedev.devsequipmenteffects.listeners.PlayerQuitListener;
import com.ignitedev.devsequipmenteffects.task.UpdatePlayerEffectsTask;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class EquipmentEffects extends JavaPlugin {
    
    public static EquipmentEffects INSTANCE;
    
    public BaseEquipmentFactories baseEquipmentFactories;
    public BaseEffectFactories baseEffectFactories;
    public BasePlayerRepository basePlayerRepository;
    public BaseEquipmentRepository baseEquipmentRepository;
    
    @Override
    public void onEnable() {
        
        INSTANCE = this;
        
        saveDefaultConfig();
        
        this.baseEquipmentRepository = new BaseEquipmentRepository();
        this.basePlayerRepository = new BasePlayerRepository();
        
        FileConfiguration config = getConfig();
        BaseConfiguration baseConfiguration = new BaseConfiguration(baseEquipmentRepository, this);
        
        registerEquipmentFactories(baseEquipmentRepository);
        registerEffectsFactories();
        
        baseConfiguration.initialize(config);
        
        registerListeners(Bukkit.getPluginManager());
        scheduleTasks(baseConfiguration);
        registerCommands(baseConfiguration, baseEquipmentRepository);
        
        String configVersion = getConfig().getString("config-version");
        
        if (!this.getDescription().getVersion().equalsIgnoreCase(configVersion)) {
            getLogger().log(
                    Level.WARNING, "Config version and plugin version are different, please generate new " +
                                   "config because you can have missing values!");
        }
    }
    
    @Override
    public void onDisable() {
    
    }
    
    private void registerCommands(BaseConfiguration baseConfiguration, BaseEquipmentRepository baseEquipmentRepository
    ) {
        
        PluginCommand equipmentEffectsAdminCommand = getCommand("equipmenteffectsadmin");
        
        Validate.notNull(equipmentEffectsAdminCommand, "Command is null!");
        
        equipmentEffectsAdminCommand.setExecutor(
                new EquipmentEffectsAdminCommand(baseConfiguration, baseEquipmentRepository, this));
    }
    
    private void scheduleTasks(BaseConfiguration baseConfiguration) {
        
        new UpdatePlayerEffectsTask(baseConfiguration, this, basePlayerRepository).runTaskTimer(this, 100,
                baseConfiguration.getTaskScheduleTimeTicks()
        );
    }
    
    private void registerListeners(PluginManager pluginManager) {
        
        pluginManager.registerEvents(new PlayerQuitListener(basePlayerRepository), this);
    }
    
    private void registerEquipmentFactories(BaseEquipmentRepository baseEquipmentRepository) {
        
        baseEquipmentFactories = new BaseEquipmentFactories();
        baseEquipmentFactories.register("DEFAULT", new DefaultBaseEquipmentFactory(baseEquipmentRepository));
    }
    
    private void registerEffectsFactories() {
        
        baseEffectFactories = new BaseEffectFactories();
        baseEffectFactories.register("DEFAULT", new DefaultBaseEffectFactory());
    }
}
