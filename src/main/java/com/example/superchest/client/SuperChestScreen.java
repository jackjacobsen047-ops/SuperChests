package com.example.superchest.client;

import com.example.superchest.SuperChestMod;
import com.example.superchest.SuperChestScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class SuperChestScreen extends HandledScreen<SuperChestScreenHandler> {

    private static final int COLS = SuperChestMod.COLUMNS;       // 9
    private static final int TOTAL_ROWS = SuperChestMod.ROWS;    // 24
    private static final int VISIBLE_ROWS = 6;                   // rows shown at once
    private static final int MAX_SCROLL = TOTAL_ROWS - VISIBLE_ROWS; // 18

    private static final int YELLOW = 0xFFFFFF55;
    private static final int PANEL = 0xFFC6C6C6;
    private static final int PANEL_DARK = 0xFF8B8B8B;
    private static final int SLOT_HOLE = 0xFF373737;
    private static final int TRACK = 0xFF555555;
    private static final int HANDLE = 0xFFAAAAAA;

    private int scrollRow = 0;

    public SuperChestScreen(SuperChestScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 184;
        this.backgroundHeight = 223;
        this.playerInventoryTitleY = SuperChestScreenHandler.PLAYER_INV_Y - 11;
    }

    @Override
    protected void init() {
        super.init();
        repositionChestSlots();
    }

    private void repositionChestSlots() {
        for (int i = 0; i < SuperChestMod.SIZE; i++) {
            Slot slot = this.handler.slots.get(i);
            int row = i / COLS;
            int col = i % COLS;
            int visibleRow = row - scrollRow;
            if (visibleRow >= 0 && visibleRow < VISIBLE_ROWS) {
                slot.x = 8 + col * 18;
                slot.y = 18 + visibleRow * 18;
            } else {
                // Park hidden slots far off-screen so they aren't drawn or clicked.
                slot.x = -10000;
                slot.y = -10000;
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (MAX_SCROLL > 0) {
            int next = (int) Math.round(scrollRow - verticalAmount);
            next = Math.max(0, Math.min(MAX_SCROLL, next));
            if (next != scrollRow) {
                scrollRow = next;
                repositionChestSlots();
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // Main panel.
        context.fill(x, y, x + backgroundWidth, y + backgroundHeight, PANEL);

        // Chest viewport background.
        int vpTop = y + 17;
        int vpBottom = y + 18 + VISIBLE_ROWS * 18;
        context.fill(x + 7, vpTop, x + 7 + COLS * 18 + 1, vpBottom, PANEL_DARK);

        // Visible chest slot holes.
        for (int vr = 0; vr < VISIBLE_ROWS; vr++) {
            for (int col = 0; col < COLS; col++) {
                int sx = x + 8 + col * 18;
                int sy = y + 18 + vr * 18;
                context.fill(sx, sy, sx + 16, sy + 16, SLOT_HOLE);
            }
        }

        // Player inventory holes.
        int piy = y + SuperChestScreenHandler.PLAYER_INV_Y;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int sx = x + 8 + col * 18;
                int sy = piy + row * 18;
                context.fill(sx, sy, sx + 16, sy + 16, SLOT_HOLE);
            }
        }
        int hy = piy + 58;
        for (int col = 0; col < 9; col++) {
            int sx = x + 8 + col * 18;
            context.fill(sx, hy, sx + 16, hy + 16, SLOT_HOLE);
        }

        // Scrollbar.
        int trackX = x + backgroundWidth - 12;
        int trackTop = y + 18;
        int trackHeight = VISIBLE_ROWS * 18;
        context.fill(trackX, trackTop, trackX + 8, trackTop + trackHeight, TRACK);
        int handleH = Math.max(14, trackHeight * VISIBLE_ROWS / TOTAL_ROWS);
        int handleY = trackTop + (MAX_SCROLL == 0 ? 0 : (trackHeight - handleH) * scrollRow / MAX_SCROLL);
        context.fill(trackX, handleY, trackX + 8, handleY + handleH, HANDLE);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // Yellow title.
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, YELLOW, false);
        context.drawText(this.textRenderer, this.playerInventoryTitle,
                this.playerInventoryTitleX, this.playerInventoryTitleY, 0x404040, false);
    }
}
