package com.ignitedev.devsequipmenteffects.util;

import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.enums.BaseCheck;
import java.util.ArrayList;
import java.util.List;
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
      Player player, List<BaseEquipment> baseEquipments) {

    List<BaseEquipment> appliedBaseEquipment = new ArrayList<>();

    ItemStack itemInMainHand;
    ItemStack itemInOffHand;

    if (MinecraftVersion.isBefore(9)) {
      itemInMainHand = player.getInventory().getItemInHand();
      itemInOffHand = player.getInventory().getItemInHand();
    } else {
      itemInMainHand = player.getInventory().getItemInMainHand();
      itemInOffHand = player.getInventory().getItemInOffHand();
    }

    baseEquipments.forEach(
        baseEquipment -> {
          boolean foundDuplicate = false;

          for (BaseEquipment equipment : appliedBaseEquipment) {
            if (equipment.getIdentifier().equalsIgnoreCase(baseEquipment.getIdentifier())) {
              foundDuplicate = true;
              break;
            }
          }
          if (foundDuplicate) {
            return;
          }

          boolean shouldApply = false;

          if (baseEquipment == null) {
            return;
          }

          if (!baseEquipment.isMustWear()) {
            if (baseEquipment.isMustHoldMainHand() && baseEquipment.isMustHoldOffHand()) {
              if (baseEquipment.isSimilar(itemInMainHand)
                  || baseEquipment.isSimilar(itemInOffHand)) {
                shouldApply = true;
              }
            } else if (baseEquipment.isMustHoldMainHand()) {
              if (baseEquipment.isSimilar(itemInMainHand)) {
                shouldApply = true;
              }
            } else if (baseEquipment.isMustHoldOffHand()) {
              if (baseEquipment.isSimilar(itemInOffHand)) {
                shouldApply = true;
              }
            } else if (!baseEquipment.isMustWear()) {
              shouldApply = true;
            }
          } else {
            for (ItemStack armorContent : player.getInventory().getArmorContents()) {
              if (baseEquipment.isSimilar(armorContent)) {
                shouldApply = true;
                break;
              }
            }
          }
          if (shouldApply) {
            appliedBaseEquipment.add(baseEquipment);
          }
        });

    return appliedBaseEquipment;
  }
}
