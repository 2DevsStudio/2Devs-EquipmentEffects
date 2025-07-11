package com.ignitedev.devsequipmenteffects.task;

import com.ignitedev.devsequipmenteffects.base.player.repository.BasePlayerRepository;
import com.ignitedev.devsequipmenteffects.configuration.BaseConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class UpdatePlayerEffectsTask extends BukkitRunnable {

  private final BaseConfiguration baseConfiguration;
  private final BasePlayerRepository basePlayerRepository;

  private final AtomicInteger cycle = new AtomicInteger(-1);
  private volatile List<List<Player>> partitions = new CopyOnWriteArrayList<>();

  @Override
  public void run() {
    if (cycle.get() == -1) {
      resetAndPartitionPlayers();
      return;
    }
    updateCurrentPartition();
  }

  private void resetAndPartitionPlayers() {
    List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());

    if (onlinePlayers.isEmpty()) {
      return;
    }
    int minPlayersForPartitioning =
        baseConfiguration.getUpdatePartitionsAmount()
            * baseConfiguration.getPartitionMinimumPlayersMultiplier();

    if (onlinePlayers.size() < minPlayersForPartitioning) {
      updatePlayers(onlinePlayers);
      return;
    }

    int partitionSize =
        Math.max(1, onlinePlayers.size() / baseConfiguration.getUpdatePartitionsAmount());
    List<List<Player>> newPartitions = new ArrayList<>();

    for (int i = 0; i < onlinePlayers.size(); i += partitionSize) {
      int end = Math.min(onlinePlayers.size(), i + partitionSize);
      newPartitions.add(new ArrayList<>(onlinePlayers.subList(i, end)));
    }

    this.partitions = new CopyOnWriteArrayList<>(newPartitions);
    this.cycle.set(0);

    // Process first partition immediately
    if (!partitions.isEmpty()) {
      updateCurrentPartition();
    }
  }

  private void updateCurrentPartition() {
    int currentCycle = cycle.getAndUpdate(c -> (c + 1) % Math.max(1, partitions.size()));

    if (currentCycle >= 0 && currentCycle < partitions.size()) {
      List<Player> currentPartition = partitions.get(currentCycle);

      if (currentPartition != null) {
        updatePlayers(currentPartition);
      }
    }
    // Reset if we've completed a full cycle
    if (currentCycle == partitions.size() - 1) {
      resetCycle();
    }
  }

  private void updatePlayers(List<Player> players) {
    for (Player player : players) {
      if (player == null || !player.isOnline()) {
        continue;
      }
      basePlayerRepository.findById(player.getUniqueId().toString()).updatePlayerActiveEffects();
    }
  }

  private void resetCycle() {
    this.cycle.set(-1);
    this.partitions.clear();
  }
}
