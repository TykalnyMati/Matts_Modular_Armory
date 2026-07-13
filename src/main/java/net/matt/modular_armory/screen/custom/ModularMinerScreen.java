package net.matt.modular_armory.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.entity.custom.ModularMinerBlockEntity;
import net.matt.modular_armory.network.RedstoneModePayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class ModularMinerScreen extends AbstractContainerScreen<ModularMinerMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "textures/gui/modular_miner/modular_miner_gui.png");

    public ModularMinerScreen(ModularMinerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageHeight = 179;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Redstone Mode Button
        this.addRenderableWidget(new RedstoneControlButton(x + 11, y + 71, 10, 10, (button) -> {
            int nextMode = (this.menu.blockEntity.getRedstoneMode() + 1) % 3;

            this.menu.blockEntity.setRedstoneMode(nextMode);

            PacketDistributor.sendToServer(new RedstoneModePayload(this.menu.blockEntity.getBlockPos(), nextMode));

            updateButtonTooltip(button, nextMode);
        })).setTooltip(getModeTooltip(this.menu.blockEntity.getRedstoneMode()));
    }

    private class RedstoneControlButton extends Button {
        public RedstoneControlButton(int x, int y, int width, int height, OnPress onPress) {
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

            int mode = ModularMinerScreen.this.menu.blockEntity.getRedstoneMode();

            int xCord = switch (mode) {
                case 1 -> 188; // Low
                case 2 -> 198; // Ignore
                default -> 178; // High
            };
            int yCord = 1;

            guiGraphics.blit(GUI_TEXTURE, this.getX() + 1, this.getY() + 1, xCord, yCord, 8, 8);
        }
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

    private void updateButtonTooltip(Button button, int mode) {
        button.setTooltip(getModeTooltip(mode));
    }

    private Tooltip getModeTooltip(int mode) {
        String key = switch (mode) {
            case 1 -> "tooltip.mattsmodulararmory.redstone.low";
            case 2 -> "tooltip.mattsmodulararmory.redstone.ignore";
            default -> "tooltip.mattsmodulararmory.redstone.high";
        };
        return Tooltip.create(Component.translatable(key));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        if (this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
            int slotIndex = this.hoveredSlot.getContainerSlot();

            if (!(this.hoveredSlot.container instanceof Inventory)) {
                String key = switch (slotIndex) {
                    case ModularMinerBlockEntity.RANGE_LENGTH_SLOT -> "tooltip.mattsmodulararmory.range_slot.length";
                    case ModularMinerBlockEntity.RANGE_WIDTH_SLOT -> "tooltip.mattsmodulararmory.range_slot.width";
                    case ModularMinerBlockEntity.RANGE_HEIGHT_SLOT -> "tooltip.mattsmodulararmory.range_slot.height";
                    default -> null;
                };

                if (key != null) {
                    pGuiGraphics.renderTooltip(this.font, Component.translatable(key), pMouseX, pMouseY);
                }
            }
        }
    }
}
