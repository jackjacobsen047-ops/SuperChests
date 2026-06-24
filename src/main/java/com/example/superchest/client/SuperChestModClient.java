package com.example.superchest.client;

import com.example.superchest.SuperChestMod;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class SuperChestModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(SuperChestMod.SCREEN_HANDLER, SuperChestScreen::new);
    }
}
