package net.matt.modular_armory.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.screen.custom.helpers.GhostSlot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class FilterScreen extends AbstractContainerScreen<FilterMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "textures/gui/filter_item/filter_item_gui.png");

    public FilterScreen(FilterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // List Mode Button
        this.addRenderableWidget(new ListModeButton(x + 133, y + 34, 18, 18, (button) -> {
            if (this.minecraft != null && this.minecraft.gameMode != null && this.minecraft.player != null) {
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);

                this.menu.clickMenuButton(this.minecraft.player, 0);

                updateListButtonTooltip(button);
            }
        })).setTooltip(getModeTooltip());
    }

    private class ListModeButton extends Button {
        public ListModeButton(int x, int y, int width, int height, OnPress onPress) {
            super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        }

        // Prevents the button from staying focused
        @Override
        public boolean isFocused() {
            return false;
        }

        // Prevents taking keyboard inputs
        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return false;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

            boolean mode = FilterScreen.this.menu.isWhitelist();

            int xCord = mode ? 178 : 196;
            int yCord = 1;

            guiGraphics.blit(GUI_TEXTURE, this.getX() + 1, this.getY() + 1, xCord, yCord, 16, 16);
        }
    }

    private Tooltip getModeTooltip() {
        String key = this.menu.isWhitelist() ? "tooltip.mattsmodulararmory.filter.whitelist" : "tooltip.mattsmodulararmory.filter.blacklist";
        return Tooltip.create(Component.translatable(key));
    }

    private void updateListButtonTooltip(Button button) {
        button.setTooltip(getModeTooltip());
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderSlot(GuiGraphics pGuiGraphics, Slot pSlot) {
        super.renderSlot(pGuiGraphics, pSlot);

        if (pSlot instanceof GhostSlot && pSlot.hasItem()) {
            int x = pSlot.x;
            int y = pSlot.y;

            pGuiGraphics.fill(x, y, x + 16, y + 16, 0x99404040);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
