package com.ai_villagers.event;

import com.ai_villagers.entity.EntitySmartVillager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.List;

@Mod.EventBusSubscriber
public class ChatEventHandler {
    private static final double INTERACTION_RANGE = 10.0;

    @SubscribeEvent
    public static void onChatMessage(ServerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        
        // Get nearby smart villagers
        List<Entity> entities = player.level().getEntities(player, 
            player.getBoundingBox().inflate(INTERACTION_RANGE));
            
        for (Entity entity : entities) {
            if (entity instanceof EntitySmartVillager villager) {
                // Handle naming command
                if (message.startsWith("/name ")) {
                    String[] parts = message.split(" ");
                    if (parts.length >= 2) {
                        villager.setName(parts[1]);
                        player.sendSystemMessage(
                            net.minecraft.network.chat.Component.literal(
                                "Villager renamed to " + parts[1]));
                        return;
                    }
                }
                
                // Handle general chat
                villager.handleChat(player, message);
            }
        }
    }
}