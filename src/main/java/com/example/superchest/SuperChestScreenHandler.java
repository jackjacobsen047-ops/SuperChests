package com.example.superchest;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class SuperChestScreenHandler extends ScreenHandler {

    // Y position (within the GUI) where the player's own inventory starts.
    public static final int PLAYER_INV_Y = 140;

    private final Inventory inventory;

    // Called on the CLIENT (no real inventory yet — it gets synced).
    public SuperChestScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(SuperChestMod.SIZE));
    }

    // Called on the SERVER with the real chest inventory.
    public SuperChestScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(SuperChestMod.SCREEN_HANDLER, syncId);
        checkSize(inventory, SuperChestMod.SIZE);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        // Chest slots: full 9 x 24 grid. The Screen repositions/hides these as
        // you scroll; their starting positions here don't matter much.
        for (int row = 0; row < SuperChestMod.ROWS; row++) {
            for (int col = 0; col < SuperChestMod.COLUMNS; col++) {
                this.addSlot(new Slot(inventory, col + row * SuperChestMod.COLUMNS, 8 + col * 18, 18 + row * 18));
            }
        }

        // Player inventory (3 rows) at a fixed spot.
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, PLAYER_INV_Y + row * 18));
            }
        }
        // Hotbar.
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, PLAYER_INV_Y + 58));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack original = slot.getStack();
            newStack = original.copy();
            int chestEnd = SuperChestMod.SIZE;
            int totalSlots = this.slots.size();
            if (index < chestEnd) {
                // From chest -> player inventory.
                if (!this.insertItem(original, chestEnd, totalSlots, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From player inventory -> chest.
                if (!this.insertItem(original, 0, chestEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (original.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }
}
