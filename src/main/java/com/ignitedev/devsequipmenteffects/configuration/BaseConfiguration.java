package com.ignitedev.devsequipmenteffects.configuration;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import com.ignitedev.devsequipmenteffects.base.effect.BaseTrigger;
import com.ignitedev.devsequipmenteffects.base.effect.factory.BaseEffectFactory;
import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.repository.BaseEquipmentRepository;
import com.ignitedev.devsequipmenteffects.enums.BaseCheck;
import com.ignitedev.devsequipmenteffects.util.BaseUtil;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Data
public class BaseConfiguration {

  private final BaseEquipmentRepository baseEquipmentRepository;
  private final EquipmentEffects equipmentEffects;
  private File itemsDirectory;
  private int taskScheduleTimeTicks;
  private int updatePartitionsAmount;
  private int partitionMinimumPlayersMultiplier;
  private String adminCommandUsage;
  private String reloadMessage;
  private String noPermissions;
  private String itemAlreadyExists;
  private String noItemInHand;
  private String thisCommandIsPlayerOnly;
  private String createdNewItem;

  public void initialize(FileConfiguration fileConfiguration) {
    partitionMinimumPlayersMultiplier =
        fileConfiguration.getInt("partition-minimum-players-multiplier");
    taskScheduleTimeTicks = fileConfiguration.getInt("task-schedule-time");
    updatePartitionsAmount = fileConfiguration.getInt("update-partitions-amount");

    adminCommandUsage = fileConfiguration.getString("messages.admin-command-usage");
    reloadMessage = fileConfiguration.getString("messages.reload");
    noPermissions = fileConfiguration.getString("messages.no-permissions");
    itemAlreadyExists = fileConfiguration.getString("messages.item-already-exists");
    noItemInHand = fileConfiguration.getString("no-item-in-hand");
    thisCommandIsPlayerOnly = fileConfiguration.getString("this-command-is-player-only");
    createdNewItem = fileConfiguration.getString("created-new-item");

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

    if (files == null) {
      return;
    }

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
      List<BaseEffect> applicableEffects =
          baseEffectFactory.convertToBaseEffects(fileYaml.getStringList("applicable-effects"));
      ItemStack itemStack = fileYaml.getItemStack("itemstack");
      ItemMeta itemMeta = itemStack.getItemMeta();

      if (itemMeta != null) {
        if (itemMeta.hasDisplayName()) {
          itemMeta.setDisplayName(BaseUtil.fixColor(itemMeta.getDisplayName()));
          itemStack.setItemMeta(itemMeta);
        }
      }
      // triggers

      List<String> enableCommands =
          fileYaml.getStringList("trigger-interactions.trigger-enable.command");
      List<String> disableCommands =
          fileYaml.getStringList("trigger-interactions.trigger-disable.command");

      BaseTrigger baseTrigger = new BaseTrigger(enableCommands, disableCommands);

      baseEquipmentRepository.add(
          new BaseEquipment(
              identifier,
              name,
              mustWear,
              mustHoldMainHand,
              mustHoldOffHand,
              applicableEffects,
              baseTrigger,
              Arrays.asList(getBaseChecks(fileYaml)),
              itemStack));
    }
  }

  private BaseCheck[] getBaseChecks(YamlConfiguration fileYaml) {
    BaseCheck[] baseChecks = new BaseCheck[BaseCheck.values().length];
    int index = 0;

    for (BaseCheck value : BaseCheck.values()) {
      if (fileYaml.getBoolean(value.getYamlPath())) {
        baseChecks[index] = value;
        index++;
      }
    }
    return baseChecks;
  }
}
