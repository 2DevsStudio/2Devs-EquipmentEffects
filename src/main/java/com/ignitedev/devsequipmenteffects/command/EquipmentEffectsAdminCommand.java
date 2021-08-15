package com.ignitedev.devsequipmenteffects.command;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.repository.BaseEquipmentRepository;
import com.ignitedev.devsequipmenteffects.configuration.BaseConfiguration;
import com.ignitedev.devsequipmenteffects.util.BaseUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class EquipmentEffectsAdminCommand implements CommandExecutor {

  private final BaseConfiguration baseConfiguration;
  private final BaseEquipmentRepository baseEquipmentRepository;
  private final EquipmentEffects equipmentEffects;

  @Override
  public boolean onCommand(@NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String label,
      @NotNull String[] args
  ) {

    if (!sender.hasPermission("EquipmentEffects.admin")) {
      sender.sendMessage(BaseUtil.fixColor(baseConfiguration.getNoPermissions()));
      return false;
    }

    if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
      String identifier = args[1];

      BaseEquipment baseEquipmentById = baseEquipmentRepository.findById(identifier);

      if (baseEquipmentById != null) {
        ((Player) sender).getInventory().addItem(baseEquipmentById.getItemStack());
        return true;
      }
    } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
      Player target = Bukkit.getPlayer(args[1]);

      if (target == null) {
        return false;
      }

      String identifier = args[2];

      BaseEquipment baseEquipmentById = baseEquipmentRepository.findById(identifier);

      if (baseEquipmentById != null) {
        target.getInventory().addItem(baseEquipmentById.getItemStack());
        return true;
      }
    } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
      StringBuilder stringBuilder = new StringBuilder("Available: ");

      for (String identifier : baseEquipmentRepository.getBaseEquipmentCache().keySet()) {
        stringBuilder.append(BaseUtil.fixColor("&e" + identifier))
            .append(BaseUtil.fixColor(" &7| "));
      }

      sender.sendMessage(stringBuilder.toString());
      return true;
    } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
      equipmentEffects.reloadConfig();
      baseConfiguration.initialize(equipmentEffects.getConfig());
      sender.sendMessage(BaseUtil.fixColor(baseConfiguration.getReloadMessage()));
      return true;
    }

    sender.sendMessage(BaseUtil.fixColor(baseConfiguration.getAdminCommandUsage()));
    return false;
  }
}
