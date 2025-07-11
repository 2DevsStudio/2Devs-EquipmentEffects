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
  public boolean isSimilar(@Nullable ItemStack target) {
    if (target == null) {
      return false;
    }
    if (target == this.itemStack) {
      return true;
    }

    XMaterial mat = XMaterial.matchXMaterial(this.itemStack);
    XMaterial targetMat = XMaterial.matchXMaterial(target);

    if (mat != targetMat) {
      return false;
    }
    return baseChecks.contains(BaseCheck.ALL_CHECKS)
        ? compareWithMeta(target)
        : isSimilarWithoutNBTCheck(target);
  }

  private boolean compareWithMeta(ItemStack target) {
    boolean hasMeta = itemStack.hasItemMeta();
    if (hasMeta != target.hasItemMeta()) {
      return false;
    }
    return !hasMeta
        || Bukkit.getItemFactory().equals(itemStack.getItemMeta(), target.getItemMeta());
  }

  private boolean processCheck(
      boolean hasItem, boolean hasTarget, Object item, Object target, BaseCheck check) {

    if (!baseChecks.contains(check)) {
      return true;
    }
    if (!BaseUtil.compareBooleanPair(hasItem, hasTarget)) {
      return false;
    }
    return !hasItem || item.equals(target);
  }

  private boolean isSimilarWithoutNBTCheck(@NotNull ItemStack target) {
    ItemMeta meta = itemStack.getItemMeta();
    ItemMeta targetMeta = target.getItemMeta();

    if (!BaseUtil.compareBooleanPair(meta == null, targetMeta == null)) {
      return false;
    }
    if (meta == null) {
      return true; // Both items have no meta
    }

    return checkMeta(
            meta,
            targetMeta,
            BaseCheck.DISPLAY_NAME_CHECK,
            ItemMeta::hasDisplayName,
            ItemMeta::getDisplayName)
        && checkMeta(meta, targetMeta, BaseCheck.LORE_CHECK, ItemMeta::hasLore, ItemMeta::getLore)
        && checkMeta(
            meta,
            targetMeta,
            BaseCheck.ENCHANTMENT_CHECK,
            ItemMeta::hasEnchants,
            ItemMeta::getEnchants)
        && (!baseChecks.contains(BaseCheck.ITEM_FLAG_CHECK)
            || meta.getItemFlags().equals(targetMeta.getItemFlags()));
  }

  private <T> boolean checkMeta(
      ItemMeta meta1,
      ItemMeta meta2,
      BaseCheck check,
      java.util.function.Function<ItemMeta, Boolean> hasCheck,
      java.util.function.Function<ItemMeta, T> getter) {
    if (!baseChecks.contains(check)) {
      return true;
    }
    boolean has1 = hasCheck.apply(meta1);
    boolean has2 = hasCheck.apply(meta2);
    return has1 == has2 && (!has1 || getter.apply(meta1).equals(getter.apply(meta2)));
  }

  @Override
  public void apply(Player player) {
    BasePlayer basePlayer = getPlayer(player);
    getBaseTrigger().apply(player);
    basePlayer.getActiveEquipment().add(this);
    getEffectList().forEach(effect -> effect.apply(player));
  }

  @Override
  public void unApply(Player player) {
    BasePlayer basePlayer = getPlayer(player);
    basePlayer.getActiveEquipment().remove(this);
    getBaseTrigger().unApply(player);
    getEffectList().forEach(effect -> effect.unApply(player));
  }

  private BasePlayer getPlayer(Player player) {
    return EquipmentEffects.INSTANCE.basePlayerRepository.findById(player.getUniqueId().toString());
  }
}
