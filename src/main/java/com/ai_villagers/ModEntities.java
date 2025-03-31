package com.ai_villagers;

import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = 
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Main.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);

    public static final RegistryObject<EntityType<EntitySmartVillager>> SMART_VILLAGER =
        ENTITIES.register("smart_villager", 
            () -> EntityType.Builder.of(EntitySmartVillager::new, MobCategory.CREATURE)
                .sized(0.6F, 1.95F)
                .clientTrackingRange(10)
                .build("smart_villager"));
                
    public static final RegistryObject<Item> SMART_VILLAGER_EGG = ITEMS.register("smart_villager_egg",
        EntitySmartVillager.SmartVillagerEgg::new);
}