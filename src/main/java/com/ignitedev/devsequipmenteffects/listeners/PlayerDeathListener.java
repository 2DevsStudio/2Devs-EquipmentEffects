package com.ignitedev.devsequipmenteffects.listeners;

import com.ignitedev.devsequipmenteffects.base.player.repository.BasePlayerRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@RequiredArgsConstructor
public class PlayerDeathListener implements Listener {

  private final BasePlayerRepository basePlayerRepository;

  @EventHandler
  public void onDeath(PlayerDeathEvent event) {
    basePlayerRepository
        .findById(event.getEntity().getUniqueId().toString())
        .clearPlayerActiveEquipment();
  }
}
