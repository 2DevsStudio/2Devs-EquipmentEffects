package com.ignitedev.devsequipmenteffects.util;

import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.enums.BaseCheck;
import java.util.*;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class BaseUtil {

  public String fixColor(String toColor) {
    return ChatColor.translateAlternateColorCodes('&', toColor);
  }

  public boolean compareBooleanPair(boolean boolean1, boolean boolean2) {
    return (boolean1 && boolean2) || (!boolean1 && !boolean2);
  }

  public boolean isArrayContainingCheck(BaseCheck[] baseChecks, BaseCheck baseCheck) {
    for (BaseCheck check : baseChecks) {
      if (check == baseCheck) {
        return true;
      }
    }
    return false;
  }

  public List<BaseEquipment> findPlayerApplicableBaseEquipment(
      @NonNull Player player, @NonNull List<BaseEquipment> baseEquipments) {

    if (baseEquipments.isEmpty()) {
      return List.of();
    }
    ItemStack itemInMainHand =
        MinecraftVersion.isBefore(9)
            ? player.getInventory().getItemInHand()
            : player.getInventory().getItemInMainHand();
    ItemStack itemInOffHand =
        MinecraftVersion.isBefore(9)
            ? player.getInventory().getItemInHand()
            : player.getInventory().getItemInOffHand();

    Set<BaseEquipment> appliedEquipment = new HashSet<>();
    Set<String> processedIds = new HashSet<>();

    for (BaseEquipment equipment : baseEquipments) {
      if (equipment == null || !processedIds.add(equipment.getIdentifier().toLowerCase())) {
        continue; // Skip null or duplicate equipment
      }
      if (shouldApplyEquipment(equipment, player, itemInMainHand, itemInOffHand)) {
        appliedEquipment.add(equipment);
      }
    }
    return new ArrayList<>(appliedEquipment);
  }

  private boolean shouldApplyEquipment(
      BaseEquipment equipment, Player player, ItemStack mainHand, ItemStack offHand) {
    if (equipment.isMustWear()) {
      return Stream.of(player.getInventory().getArmorContents())
          .filter(Objects::nonNull)
          .anyMatch(equipment::isSimilar);
    }
    boolean mustHoldMain = equipment.isMustHoldMainHand();
    boolean mustHoldOff = equipment.isMustHoldOffHand();

    if (mustHoldMain && mustHoldOff) {
      return equipment.isSimilar(mainHand) || equipment.isSimilar(offHand);
    } else if (mustHoldMain) {
      return equipment.isSimilar(mainHand);
    } else if (mustHoldOff) {
      return equipment.isSimilar(offHand);
    }

    return true;
  }
}
