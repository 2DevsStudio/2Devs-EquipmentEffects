package com.ignitedev.devsequipmenteffects.base.equipment;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.effect.BaseEffect;
import com.ignitedev.devsequipmenteffects.base.effect.BaseTrigger;
import com.ignitedev.devsequipmenteffects.base.player.BasePlayer;
import com.ignitedev.devsequipmenteffects.enums.BaseCheck;
import com.ignitedev.devsequipmenteffects.interfaces.Applicable;
import com.ignitedev.devsequipmenteffects.util.BaseUtil;
import com.ignitedev.devsequipmenteffects.util.XMaterial;
import java.util.List;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class BaseEquipment implements Applicable {

  private final String identifier;
  private final String name;

  private final boolean mustWear;
  private final boolean mustHoldMainHand;
  private final boolean mustHoldOffHand;

  private final List<BaseEffect> effectList;
  private final BaseTrigger baseTrigger;
  private final BaseCheck[] baseChecks;

  private final ItemStack itemStack;

  /*
   * this method needs checks if item have meta, lore etc
   */
  public boolean isSimilar(@Nullable ItemStack targetItemStack) {

    if (targetItemStack == null) {
      return false;
    } else {
      if (targetItemStack == this.itemStack) {
        return true;
      } else {
        XMaterial itemstack = XMaterial.matchXMaterial(this.itemStack);
        XMaterial targetItem = XMaterial.matchXMaterial(targetItemStack);

        if (itemstack == targetItem) {
          if (BaseUtil.isArrayContainingCheck(baseChecks, BaseCheck.ALL_CHECKS)) {
            return this.itemStack.hasItemMeta() == targetItemStack.hasItemMeta() && (
                !this.itemStack.hasItemMeta() || Bukkit.getItemFactory()
                    .equals(this.itemStack.getItemMeta(), targetItemStack.getItemMeta()));
          } else {
            return isSimilarWithoutNBTCheck(targetItemStack);
          }
        }
      }
    }
    return false;
  }

  private boolean isSimilarWithoutNBTCheck(@NotNull ItemStack targetItemStack) {
    XMaterial itemstack = XMaterial.matchXMaterial(this.itemStack);
    XMaterial targetItem = XMaterial.matchXMaterial(targetItemStack);

    if (itemstack != targetItem) {
      return false;
    }

    ItemMeta targetMeta = targetItemStack.getItemMeta();
    ItemMeta itemMeta = this.itemStack.getItemMeta();

    boolean isSimilar = true;

    if (targetMeta != null && itemMeta != null) {
      for (BaseCheck baseCheck : baseChecks) {
        if (baseCheck == BaseCheck.DISPLAY_NAME_CHECK) {
          if (targetMeta.hasDisplayName() && itemMeta.hasDisplayName()) {
            if (!targetMeta.getDisplayName().equalsIgnoreCase(itemMeta.getDisplayName())) {
              isSimilar = false;
            }
          }
        } else if (baseCheck == BaseCheck.LORE_CHECK) {
          if (targetMeta.hasLore() && itemMeta.hasLore()) {
            if (!targetMeta.getLore().equals(itemMeta.getLore())) {
              isSimilar = false;
            }
          }
        } else if (baseCheck == BaseCheck.ENCHANTMENT_CHECK) {
          if (targetMeta.hasEnchants() && itemMeta.hasEnchants()) {
            if (!targetMeta.getEnchants().equals(itemMeta.getEnchants())) {
              isSimilar = false;
            }
          }
        } else if (baseCheck == BaseCheck.ITEM_FLAG_CHECK) {
          return targetMeta.getItemFlags().equals(itemMeta.getItemFlags());
        }
      }
    }
    return isSimilar;
  }


  @Override
  public void apply(Player player) {

    String playerUUID = player.getUniqueId().toString();
    BasePlayer basePlayer = EquipmentEffects.INSTANCE.basePlayerRepository.findById(playerUUID);

    getBaseTrigger().apply(player);

    basePlayer.getActiveEquipment().add(this);

    getEffectList().forEach(baseEffect -> baseEffect.apply(player));
  }

  @Override
  public void unApply(Player player) {

    String playerUUID = player.getUniqueId().toString();
    BasePlayer basePlayer = EquipmentEffects.INSTANCE.basePlayerRepository.findById(playerUUID);

    basePlayer.getActiveEquipment().remove(this);

    getBaseTrigger().unApply(player);
    getEffectList().forEach(baseEffect -> baseEffect.unApply(player));
  }
}
