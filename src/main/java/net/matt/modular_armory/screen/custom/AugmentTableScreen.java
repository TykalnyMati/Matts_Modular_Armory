package net.matt.modular_armory.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class AugmentTableScreen extends AbstractContainerScreen<AugmentTableMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "textures/gui/augment_table/augment_table_gui.png");

    public AugmentTableScreen(AugmentTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Blacking out inactive slots
        for (Slot slot : this.menu.slots) {
            if (!slot.isActive()) {
                int slot_x = leftPos + slot.x;
                int slot_y = topPos + slot.y;

                pGuiGraphics.fill(
                        slot_x,
                        slot_y,
                        slot_x + 16,
                        slot_y + 16,
                        0x88000000 // semi-transparent black
                );
            }
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
