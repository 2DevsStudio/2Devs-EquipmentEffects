package com.ignitedev.devsequipmenteffects.command;

import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.repository.BaseEquipmentRepository;
import com.ignitedev.devsequipmenteffects.configuration.BaseConfiguration;
import com.ignitedev.devsequipmenteffects.util.BaseUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class EquipmentEffectsAdminCommand implements CommandExecutor {
    
    private final BaseConfiguration baseConfiguration;
    private final BaseEquipmentRepository baseEquipmentRepository;
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args
    ) {
        
        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            String identifier = args[1];
            
            BaseEquipment baseEquipmentById = baseEquipmentRepository.findById(identifier);
            
            if (baseEquipmentById != null) {
                ((Player) sender).getInventory().addItem(baseEquipmentById.getItemStack());
                return true;
            }
        }
        
        
        sender.sendMessage(BaseUtil.colorComponent(baseConfiguration.getAdminCommandUsage()));
        return false;
    }
}
