package com.ignitedev.devsequipmenteffects.base.equipment.repository;

import com.ignitedev.devsequipmenteffects.base.equipment.BaseEquipment;
import com.ignitedev.devsequipmenteffects.interfaces.Repository;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class BaseEquipmentRepository implements Repository<BaseEquipment> {

  private final Map<String, BaseEquipment> baseEquipmentCache = new ConcurrentHashMap<>();

  @Override
  public @Nullable BaseEquipment findById(String identifier) {

    return baseEquipmentCache.get(identifier);
  }

  public @Nullable BaseEquipment findByItemStack(ItemStack itemStack) {
    if (itemStack == null || itemStack.getType() == Material.AIR) {
      return null;
    }

    for (BaseEquipment value : baseEquipmentCache.values()) {
      if (value.isSimilar(itemStack)) {
        return value;
      }
    }
    return null;
  }

  public List<BaseEquipment> findAllByMaterial(Material material) {
    List<BaseEquipment> applicableBaseEquipments = new ArrayList<>();

    for (BaseEquipment value : baseEquipmentCache.values()) {
      if (value.getItemStack().getType() == material) {
        applicableBaseEquipments.add(value);
      }
    }
    return applicableBaseEquipments;
  }

  @Override
  public void remove(String identifier) {

    baseEquipmentCache.remove(identifier);
  }

  @Override
  public void add(BaseEquipment value) {

    baseEquipmentCache.put(value.getIdentifier(), value);
  }
}
