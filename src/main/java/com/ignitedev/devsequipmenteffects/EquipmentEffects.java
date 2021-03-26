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
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
        BaseConfiguration baseConfiguration = new BaseConfiguration(config, baseEquipmentRepository, this);
        
        registerEquipmentFactories(baseEquipmentRepository);
        registerEffectsFactories();
        
        baseConfiguration.initialize();
        
        registerListeners(Bukkit.getPluginManager(), baseEquipmentRepository);
        scheduleTasks(baseConfiguration);
        registerCommands(baseConfiguration, baseEquipmentRepository);
    }
    
    @Override
    public void onDisable() {
    
    }
    
    private void registerCommands(BaseConfiguration baseConfiguration, BaseEquipmentRepository baseEquipmentRepository
    ) {
        
        getCommand("equipmenteffectsadmin").setExecutor(
                new EquipmentEffectsAdminCommand(baseConfiguration, baseEquipmentRepository));
    }
    
    private void scheduleTasks(BaseConfiguration baseConfiguration) {
        
        new UpdatePlayerEffectsTask(baseConfiguration, this, basePlayerRepository).runTaskTimer(this, 100,
                baseConfiguration.getTaskScheduleTimeTicks()
        );
    }
    
    private void registerListeners(PluginManager pluginManager, BaseEquipmentRepository baseEquipmentRepository) {
        
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
