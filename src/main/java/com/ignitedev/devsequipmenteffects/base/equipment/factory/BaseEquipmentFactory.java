package com.ignitedev.devsequipmenteffects.base.equipment.factory;

import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.factory.Factory;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface BaseEquipmentFactory extends Factory {

  @Nullable BaseEquipment convertToBaseEquipment(ItemStack itemStack);

  List<BaseEquipment> convertToBaseEquipments(List<ItemStack> itemStacks);

}
