package com.ai_villagers;

import com.ai_villagers.ModEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("ai_villagers")
public class Main {
    public static final String MOD_ID = "ai_villagers";
    
    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register entities and items
        ModEntities.ENTITIES.register(modEventBus);
        ModEntities.ITEMS.register(modEventBus);
        
        // Register events
        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Post-registration setup
        });
    }
}
