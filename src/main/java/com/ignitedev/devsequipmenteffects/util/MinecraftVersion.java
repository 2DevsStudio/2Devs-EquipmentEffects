package com.ignitedev.devsequipmenteffects.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;

@UtilityClass
public class MinecraftVersion {

  private static final String stringVersion;
  private static final int intVersion;

  static {
    String fullVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    stringVersion = StringUtils.replace(fullVersion, "_", " ");
    intVersion =
        Integer.parseInt(StringUtils.replace(fullVersion.split("_")[1].split("_R")[0], "v", ""));
  }

  public static boolean is(int version2) {
    return intVersion == version2;
  }

  public static boolean isAfter(int version2) {
    return intVersion > version2;
  }

  public static boolean isBefore(int version2) {
    return intVersion < version2;
  }

  public static boolean isOrAfter(int version2) {
    return intVersion == version2 || intVersion > version2;
  }

  public static String getStringVersion() {
    return stringVersion;
  }

  public static int getIntVersion() {
    return intVersion;
  }
}
