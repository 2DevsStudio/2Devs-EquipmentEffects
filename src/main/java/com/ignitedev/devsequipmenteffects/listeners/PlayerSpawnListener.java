package com.ignitedev.devsequipmenteffects.listeners;

import com.ignitedev.devsequipmenteffects.EquipmentEffects;
import com.ignitedev.devsequipmenteffects.base.player.BasePlayer;
import com.ignitedev.devsequipmenteffects.base.player.repository.BasePlayerRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class PlayerSpawnListener implements Listener {

  private final BasePlayerRepository basePlayerRepository;

  @EventHandler
  public void onRespawn(PlayerRespawnEvent event) {
    Bukkit.getScheduler()
        .runTaskLater(
            EquipmentEffects.INSTANCE,
            () -> {
              BasePlayer basePlayer =
                  basePlayerRepository.findById(event.getPlayer().getUniqueId().toString());

              basePlayer.clearPlayerActiveEquipment();
              basePlayer.updatePlayerActiveEffects();
            },
            20);
  }
}
