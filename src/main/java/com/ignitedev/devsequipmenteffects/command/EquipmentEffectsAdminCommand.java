package com.ignitedev.devsequipmenteffects.command;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.repository.BaseEquipmentRepository;
import com.ignitedev.devsequipmenteffects.configuration.BaseConfiguration;
import com.ignitedev.devsequipmenteffects.util.BaseUtil;
import com.ignitedev.devsequipmenteffects.util.MinecraftVersion;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.ClickEvent.Action;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.logging.Level;

@RequiredArgsConstructor
public class EquipmentEffectsAdminCommand implements CommandExecutor {

  private final BaseConfiguration baseConfiguration;
  private final BaseEquipmentRepository baseEquipmentRepository;
  private final EquipmentEffects equipmentEffects;

  @SneakyThrows
  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String label,
      @NotNull String[] args) {

    if (!sender.hasPermission("EquipmentEffects.admin")) {
      sender.sendMessage(BaseUtil.fixColor(baseConfiguration.getNoPermissions()));
      return false;
    }

    if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
      if (!(sender instanceof Player)) {
        sender.sendMessage(BaseUtil.fixColor(baseConfiguration.getThisCommandIsPlayerOnly()));
        return false;
      }

      Player senderPlayer = (Player) sender;

      String identifier = args[1];
      BaseEquipment baseEquipmentById = baseEquipmentRepository.findById(identifier);

      if (baseEquipmentById != null) {
        senderPlayer.getInventory().addItem(baseEquipmentById.getItemStack());
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
      Builder builder = null;

      for (String identifier : baseEquipmentRepository.getBaseEquipmentCache().keySet()) {
        Builder content =
            Component.text()
                .clickEvent(
                    ClickEvent.clickEvent(Action.SUGGEST_COMMAND, "/eea give " + identifier))
                .hoverEvent(
                    HoverEvent.showText(
                        Component.text().content("CLICK ME").color(TextColor.color(255, 0, 0))))
                .content(BaseUtil.fixColor("&e" + identifier + " &7| "));

        if (builder == null) {
          builder = Component.text().content("Available: ").append(content);
        } else {
          builder.append(content);
        }
      }
      if (builder != null) {
        if (!(sender instanceof Player)) {
          sender.sendMessage(BaseUtil.fixColor(baseConfiguration.getThisCommandIsPlayerOnly()));
          return false;
        }
        Player senderPlayer = (Player) sender;

        equipmentEffects.adventure.player(senderPlayer).sendMessage(builder.build());
        return true;
      }
      return true;
    } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
      reload(sender);
      return true;
    } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) { // todo change args length
      String itemID = args[1];

      if (baseEquipmentRepository.findById(itemID) != null) {
        sender.sendMessage(BaseUtil.fixColor(baseConfiguration.getItemAlreadyExists()));
        return false;
      }

      if (!(sender instanceof Player)) {
        sender.sendMessage(BaseUtil.fixColor(baseConfiguration.getThisCommandIsPlayerOnly()));
        return false;
      }
      Player senderPlayer = (Player) sender;
      ItemStack itemInHand;

      if (MinecraftVersion.isBefore(9)) {
        itemInHand = senderPlayer.getInventory().getItemInHand();
      } else {
        itemInHand = senderPlayer.getInventory().getItemInMainHand();
      }

      if (itemInHand.getType() == Material.AIR) {
        senderPlayer.sendMessage(BaseUtil.fixColor(baseConfiguration.getNoItemInHand()));
        return false;
      }

      InputStream resource = EquipmentEffects.INSTANCE.getResource("Items/default.yml");
      File file = new File(baseConfiguration.getItemsDirectory().getPath(), itemID + ".yml");

      if (file.createNewFile()) {
        EquipmentEffects.INSTANCE.getLogger().log(Level.INFO, "Created new Item File!");
      }
      if (resource == null) {
        throw new FileNotFoundException("Resource default.yml file not found!");
      }
      try (OutputStream outputStream = new FileOutputStream(file)) {
        IOUtils.copy(resource, outputStream);
      }
      YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

      yamlConfiguration.set("itemstack", itemInHand);
      yamlConfiguration.set("id", itemID);
      yamlConfiguration.save(file);

      sender.sendMessage(BaseUtil.fixColor(baseConfiguration.getCreatedNewItem()));
      return true;
    }
    sender.sendMessage(BaseUtil.fixColor(baseConfiguration.getAdminCommandUsage()));
    return false;
  }

  private void reload(CommandSender commandSender) {
    equipmentEffects.reloadConfig();
    baseConfiguration.initialize(equipmentEffects.getConfig());
    commandSender.sendMessage(BaseUtil.fixColor(baseConfiguration.getReloadMessage()));
  }
}
