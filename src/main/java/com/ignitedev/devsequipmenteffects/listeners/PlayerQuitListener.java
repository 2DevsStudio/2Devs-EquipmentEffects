package com.ignitedev.devsequipmenteffects.listeners;

import com.ignitedev.devsequipmenteffects.base.player.BasePlayer;
import com.ignitedev.devsequipmenteffects.base.player.repository.BasePlayerRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerQuitListener implements Listener {
    
    private final BasePlayerRepository basePlayerRepository;
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        
        Player player = event.getPlayer();
        BasePlayer basePlayer = basePlayerRepository.findById(player.getUniqueId().toString());
        
        if(basePlayer == null){
            return;
        }
        
        basePlayer.clearPlayerActiveEffects();
    }
    
}
