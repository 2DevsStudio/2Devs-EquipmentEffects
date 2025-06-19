package com.ignitedev.devsequipmenteffects.base.equipment.factory;

import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.factory.Factory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BaseEquipmentFactory extends Factory {

  @Nullable
  BaseEquipment convertToBaseEquipment(ItemStack itemStack);

  List<BaseEquipment> convertToBaseEquipments(List<ItemStack> itemStacks);
}
