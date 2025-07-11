package com.ignitedev.devsequipmenteffects.base.effect;

import com.ignitedev.devsequipmenteffects.interfaces.Applicable;
import java.util.List;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Data
public class BaseTrigger implements Applicable {

  private final List<String> enableCommands;
  private final List<String> disableCommands;

  @Override
  public void apply(Player player) {
    enableCommands.forEach(
        command ->
            Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));
  }

  @Override
  public void unApply(Player player) {
    disableCommands.forEach(
        command ->
            Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));
  }
}
