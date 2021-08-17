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
  private final List<BaseCheck> baseChecks;

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
          if (baseChecks.contains(BaseCheck.ALL_CHECKS)) {
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

  private boolean processCheck(boolean itemBoolean, boolean targetBoolean, Object itemObject,
      Object targetObject, BaseCheck baseCheck) {

    if (!baseChecks.contains(baseCheck)) {
      return true;
    }

    boolean isSimilar = BaseUtil.compareBooleanPair(itemBoolean, targetBoolean);

    if (itemBoolean && targetBoolean) {
      if (!itemObject.equals(targetObject)) {
        isSimilar = false;
      }
    }
    return isSimilar;
  }


  private boolean isSimilarWithoutNBTCheck(@NotNull ItemStack targetItemStack) {
    XMaterial itemstack = XMaterial.matchXMaterial(this.itemStack);
    XMaterial targetItem = XMaterial.matchXMaterial(targetItemStack);

    if (itemstack != targetItem) {
      return false;
    }

    ItemMeta targetMeta = targetItemStack.getItemMeta();
    ItemMeta itemMeta = this.itemStack.getItemMeta();

    if (!BaseUtil.compareBooleanPair(targetMeta == null, itemMeta == null)) {
      return false;
    }

    // it might be nulled pair
    if (targetMeta == null || itemMeta == null) {
      return false;
    }

    boolean displayNameCheck = processCheck(itemMeta.hasDisplayName(), targetMeta.hasDisplayName(),
        itemMeta.getDisplayName(), targetMeta.getDisplayName(), BaseCheck.DISPLAY_NAME_CHECK);
    boolean loreCheck = processCheck(itemMeta.hasLore(), targetMeta.hasLore(),
        itemMeta.getLore(), targetMeta.getLore(), BaseCheck.LORE_CHECK);
    boolean enchantmentCheck = processCheck(itemMeta.hasEnchants(), targetMeta.hasEnchants(),
        itemMeta.getEnchants(), targetMeta.getEnchants(), BaseCheck.ENCHANTMENT_CHECK);
    boolean flagCheck = processCheck(true, true, itemMeta.getItemFlags(),
        targetMeta.getItemFlags(), BaseCheck.ITEM_FLAG_CHECK);

    return displayNameCheck && loreCheck && enchantmentCheck && flagCheck;
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
