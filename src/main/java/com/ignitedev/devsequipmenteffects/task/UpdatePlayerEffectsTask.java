package com.ignitedev.devsequipmenteffects.task;

import com.google.common.collect.Lists;
import com.ignitedev.devsequipmenteffects.base.player.BasePlayer;
import com.ignitedev.devsequipmenteffects.base.player.repository.BasePlayerRepository;
import com.ignitedev.devsequipmenteffects.configuration.BaseConfiguration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class UpdatePlayerEffectsTask extends BukkitRunnable {

  private final BaseConfiguration baseConfiguration;
  private final BasePlayerRepository basePlayerRepository;

  private int cycle = -1;

  private List<List<Player>> partitions;

  @Override
  public void run() {

    if (cycle == -1) {
      // we don't want to skip index 0, so we reset on cycle -1
      // when cycles pass, we get online players again  and separate them to partitions
      List<Player> onlinePlayersList = new ArrayList<>(Bukkit.getOnlinePlayers());

      if (onlinePlayersList.isEmpty()) {
        return;
      }
      if (onlinePlayersList.size()
          < baseConfiguration.getUpdatePartitionsAmount()
              * baseConfiguration.getPartitionMinimumPlayersMultiplier()) {

        // if player amount is lower than (partition amount * multiplier), update inventories
        // instantly for
        // everyone at the same time

        iterateThroughPlayers(new ArrayList<>(onlinePlayersList));
        return;
      }

      cycle = baseConfiguration.getUpdatePartitionsAmount();
      partitions =
          Lists.partition(
              onlinePlayersList,
              onlinePlayersList.size() / baseConfiguration.getUpdatePartitionsAmount());
    }
    iterateThroughPlayers(partitions.get(cycle == 0 ? 0 : cycle - 1));
    cycle = cycle - 1;
  }

  private void iterateThroughPlayers(List<Player> players) {

    for (Player player : players) {

      BasePlayer basePlayer = basePlayerRepository.findById(player.getUniqueId().toString());

      basePlayer.updatePlayerActiveEffects();
    }
  }
}
