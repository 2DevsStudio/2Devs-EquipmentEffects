package com.ignitedev.devsequipmenteffects.base.equipment.factory;

import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.base.equipment.repository.BaseEquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/*
 * If you thinking for what is that wrapping, this wrapping is for future
 * expand to be sure that i'm able for example to add support of nbt holding ;)
 */
@RequiredArgsConstructor
public class DefaultBaseEquipmentFactory implements BaseEquipmentFactory {

  private final BaseEquipmentRepository baseEquipmentRepository;

  @Override
  public @Nullable BaseEquipment convertToBaseEquipment(ItemStack itemStack) {

    return baseEquipmentRepository.findByItemStack(itemStack);
  }

  @Override
  public List<BaseEquipment> convertToBaseEquipments(List<ItemStack> itemStacks) {

    List<BaseEquipment> baseEquipments = new ArrayList<>();

    for (ItemStack itemStack : itemStacks) {
      BaseEquipment baseEquipment = convertToBaseEquipment(itemStack);

      if (baseEquipment == null) {
        continue;
      }

      baseEquipments.add(baseEquipment);
    }

    return baseEquipments;
  }
}
