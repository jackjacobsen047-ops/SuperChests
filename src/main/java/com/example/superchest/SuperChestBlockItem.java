package com.example.superchest;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SuperChestBlockItem extends BlockItem {

    public SuperChestBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.literal("Super Chest").formatted(Formatting.YELLOW);
    }

    @Override
    public Text getName() {
        return Text.literal("Super Chest").formatted(Formatting.YELLOW);
    }
}
