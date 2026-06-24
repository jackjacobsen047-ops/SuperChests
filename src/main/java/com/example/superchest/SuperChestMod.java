package com.example.superchest;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class SuperChestMod implements ModInitializer {

    public static final String MOD_ID = "superchest";

    // 9 columns x 24 rows = 216 slots = 4x a double chest (54).
    public static final int COLUMNS = 9;
    public static final int ROWS = 24;
    public static final int SIZE = COLUMNS * ROWS;

    public static Block SUPER_CHEST_BLOCK;
    public static BlockItem SUPER_CHEST_ITEM;
    public static BlockEntityType<SuperChestBlockEntity> SUPER_CHEST_BE;
    public static ScreenHandlerType<SuperChestScreenHandler> SCREEN_HANDLER;

    @Override
    public void onInitialize() {
        Identifier id = Identifier.of(MOD_ID, "super_chest");
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);

        SUPER_CHEST_BLOCK = Registry.register(Registries.BLOCK, blockKey,
                new SuperChestBlock(AbstractBlock.Settings.create()
                        .registryKey(blockKey)
                        .strength(2.5f)
                        .sounds(BlockSoundGroup.WOOD)));

        SUPER_CHEST_ITEM = Registry.register(Registries.ITEM, itemKey,
                new SuperChestBlockItem(SUPER_CHEST_BLOCK, new Item.Settings()
                        .registryKey(itemKey)
                        .useBlockPrefixedTranslationKey()));

        SUPER_CHEST_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id,
                BlockEntityType.Builder.create(SuperChestBlockEntity::new, SUPER_CHEST_BLOCK).build());

        SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id,
                new ScreenHandlerType<>(SuperChestScreenHandler::new, FeatureSet.empty()));
    }
}
