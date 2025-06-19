package com.ignitedev.devsequipmenteffects.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BaseCheck {
  DISPLAY_NAME_CHECK("item-checks.display-name-check"),
  LORE_CHECK("item-checks.lore-check"),
  ENCHANTMENT_CHECK("item-checks.enchantment-check"),
  ITEM_FLAG_CHECK("item-checks.item-flag-check"),
  ALL_CHECKS("item-checks.all-checks");

  @Getter
  private final String yamlPath;
}
