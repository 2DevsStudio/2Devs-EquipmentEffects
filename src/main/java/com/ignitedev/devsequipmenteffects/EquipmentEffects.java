package com.ignitedev.devsequipmenteffects;

import com.ignitedev.devsequipmenteffects.base.effect.factory.BaseEffectFactories;
import com.ignitedev.devsequipmenteffects.base.effect.factory.DefaultBaseEffectFactory;
import com.ignitedev.devsequipmenteffects.base.equipment.factory.BaseEquipmentFactories;
import com.ignitedev.devsequipmenteffects.base.equipment.factory.DefaultBaseEquipmentFactory;
import com.ignitedev.devsequipmenteffects.base.equipment.repository.BaseEquipmentRepository;
import com.ignitedev.devsequipmenteffects.configuration.BaseConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class EquipmentEffects extends JavaPlugin {
    
    public BaseEquipmentFactories baseEquipmentFactories;
    public BaseEffectFactories baseEffectFactories;
    public static EquipmentEffects INSTANCE;
    
    @Override
    public void onEnable() {
        INSTANCE = this;
        
        saveDefaultConfig();
        
        registerEquipmentFactories();
        registerEffectsFactories();
        
        BaseEquipmentRepository baseEquipmentRepository = new BaseEquipmentRepository();
        FileConfiguration config = getConfig();
        BaseConfiguration baseConfiguration = new BaseConfiguration(config, baseEquipmentRepository);
        
        baseConfiguration.initialize();
    }
    
    @Override
    public void onDisable() {
    
    }
    
    private void registerEquipmentFactories() {
        
        baseEquipmentFactories = new BaseEquipmentFactories();
        baseEquipmentFactories.register("DEFAULT", new DefaultBaseEquipmentFactory());
    }
    
    private void registerEffectsFactories() {
        
        baseEffectFactories = new BaseEffectFactories();
        baseEffectFactories.register("DEFAULT", new DefaultBaseEffectFactory());
    }
}
