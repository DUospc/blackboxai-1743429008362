package com.ai_villagers.entity;

import com.ai_villagers.ai.VillagerAITasks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;

public class EntitySmartVillager extends Villager {
    private String customName = "Villager";
    private int aiState = 0; // 0=idle, 1=following, 2=working
    private int emeraldCount = 0;

    public EntitySmartVillager(EntityType<? extends Villager> type, Level level) {
        super(type, level);
        this.goalSelector.addGoal(3, new VillagerAITasks.StealItemGoal(this));
        this.goalSelector.addGoal(4, new VillagerAITasks.VillagerChatGoal(this));
    }

    public static class SmartVillagerEgg extends SpawnEggItem {
        public SmartVillagerEgg() {
            super(ModEntities.SMART_VILLAGER.get(), 
                0x565656, 0x00FF00, 
                new Properties().stacksTo(64));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("CustomName", this.customName);
        tag.putInt("AIState", this.aiState);
        tag.putInt("EmeraldCount", this.emeraldCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("CustomName")) {
            this.customName = tag.getString("CustomName");
        }
        if (tag.contains("AIState")) {
            this.aiState = tag.getInt("AIState");
        }
        if (tag.contains("EmeraldCount")) {
            this.emeraldCount = tag.getInt("EmeraldCount");
        }
    }

    public void setName(String name) {
        this.customName = name;
        this.setCustomName(Component.literal(name));
    }

    public void handleChat(Player player, String message) {
        if (message == null) {
            // Villager-to-villager chat
            this.playAmbientSound();
            return;
        }

        String lowerMsg = message.toLowerCase();
        if (lowerMsg.contains("hello")) {
            String response = "Hello " + (player != null ? player.getName().getString() : "friend") + 
                "! I'm " + this.customName;
            if (player != null) {
                player.sendSystemMessage(Component.literal(response));
            }
        } else if (lowerMsg.contains("follow")) {
            this.aiState = 1;
            if (player != null) {
                player.sendSystemMessage(Component.literal(this.customName + " will now follow you"));
            }
        } else if (lowerMsg.contains("stop")) {
            this.aiState = 0;
            if (player != null) {
                player.sendSystemMessage(Component.literal(this.customName + " stopped following"));
            }
        } else if (lowerMsg.contains("emeralds")) {
            String response = "I have " + this.emeraldCount + " emeralds";
            if (player != null) {
                player.sendSystemMessage(Component.literal(response));
            }
        }
    }

    public void addEmerald(ItemStack stack) {
        this.emeraldCount += stack.getCount();
    }

    public int getEmeraldCount() {
        return this.emeraldCount;
    }
}