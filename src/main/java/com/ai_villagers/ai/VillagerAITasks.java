package com.ai_villagers.ai;

import com.ai_villagers.entity.EntitySmartVillager;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import java.util.EnumSet;

public class VillagerAITasks {
    
    public static class StealItemGoal extends Goal {
        private final EntitySmartVillager villager;
        private ItemEntity targetItem;
        private int stealCooldown;

        public StealItemGoal(EntitySmartVillager villager) {
            this.villager = villager;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (stealCooldown > 0) {
                stealCooldown--;
                return false;
            }
            
            // Find nearby emeralds
            this.targetItem = villager.level()
                .getEntitiesOfClass(ItemEntity.class, villager.getBoundingBox().inflate(8.0),
                    item -> item.getItem().is(Items.EMERALD))
                .stream()
                .findFirst()
                .orElse(null);
                
            return targetItem != null && villager.getRandom().nextFloat() < 0.1f;
        }

        @Override
        public void start() {
            villager.getNavigation().moveTo(targetItem, 1.0);
        }

        @Override
        public void tick() {
            if (targetItem != null && villager.distanceToSqr(targetItem) < 2.0) {
                villager.swing(InteractionHand.MAIN_HAND);
                villager.getInventory().addItem(targetItem.getItem());
                targetItem.discard();
                stealCooldown = 200; // 10 second cooldown
            }
        }
    }

    public static class VillagerChatGoal extends Goal {
        private final EntitySmartVillager villager;
        private EntitySmartVillager chatTarget;
        private int chatCooldown;

        public VillagerChatGoal(EntitySmartVillager villager) {
            this.villager = villager;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (chatCooldown > 0) {
                chatCooldown--;
                return false;
            }

            // Find another smart villager nearby
            this.chatTarget = (EntitySmartVillager)villager.level()
                .getEntitiesOfClass(EntitySmartVillager.class, 
                    villager.getBoundingBox().inflate(5.0),
                    v -> v != villager)
                .stream()
                .findFirst()
                .orElse(null);

            return chatTarget != null && villager.getRandom().nextFloat() < 0.2f;
        }

        @Override
        public void start() {
            // Villagers look at each other when chatting
            villager.getLookControl().setLookAt(chatTarget, 10.0f, 5.0f);
            chatTarget.getLookControl().setLookAt(villager, 10.0f, 5.0f);
            
            // 50% chance to initiate conversation
            if (villager.getRandom().nextBoolean()) {
                villager.handleChat(null, "Hello " + chatTarget.getCustomName().getString());
                chatCooldown = 100; // 5 second cooldown
            }
        }
    }
}