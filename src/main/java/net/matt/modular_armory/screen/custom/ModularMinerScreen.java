package net.matt.modular_armory.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.entity.custom.helper.ModularMachineBlockEntity;
import net.matt.modular_armory.network.RangeDisplayStatePayload;
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

import java.text.DecimalFormat;

public class ModularMinerScreen extends AbstractContainerScreen<ModularMinerMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "textures/gui/modular_miner/modular_miner_gui.png");
    private static final ResourceLocation ENERGY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "textures/gui/energy_progress.png");

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

            updateRedstoneModeButtonTooltip(button, nextMode);
        })).setTooltip(getRedstoneModeTooltip(this.menu.blockEntity.getRedstoneMode()));

        this.addRenderableWidget(new RangeDisplayButton(x + 155, y + 71, 10, 10, (button) -> {
            boolean nextState = (!this.menu.blockEntity.getRangeDisplayState());

            this.menu.blockEntity.setRangeDisplayState(nextState);

            PacketDistributor.sendToServer(new RangeDisplayStatePayload(this.menu.blockEntity.getBlockPos(), nextState));

            updateRangeDisplayButtonTooltip(button, nextState);
        })).setTooltip(getRangeDisplayTooltip(this.menu.blockEntity.getRangeDisplayState()));
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

    private class RangeDisplayButton extends Button {
        public RangeDisplayButton(int x, int y, int width, int height, OnPress onPress) {
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

            boolean state = ModularMinerScreen.this.menu.blockEntity.getRangeDisplayState();

            int xCord = state ? 188 : 178;
            int yCord = 11;

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

        renderEnergyDisplay(pGuiGraphics, x, y);
    }

    private void renderEnergyDisplay(GuiGraphics guiGraphics, int x, int y) {
        if (menu.hasPower()) {
            int energyProgress = menu.getEnergyDisplayProgress();

            if (energyProgress > 0) {
                int yOffset = 54 - energyProgress;

                guiGraphics.blit(ENERGY_TEXTURE, x + 9, y + 16 + yOffset, 0, yOffset, 14, energyProgress, 14, 54);
            }
        }
    }

    private void updateRedstoneModeButtonTooltip(Button button, int mode) {
        button.setTooltip(getRedstoneModeTooltip(mode));
    }

    private void updateRangeDisplayButtonTooltip(Button button, boolean state) {
        button.setTooltip(getRangeDisplayTooltip(state));
    }

    private Tooltip getRedstoneModeTooltip(int mode) {
        String key = switch (mode) {
            case 1 -> "tooltip.mattsmodulararmory.redstone.low";
            case 2 -> "tooltip.mattsmodulararmory.redstone.ignore";
            default -> "tooltip.mattsmodulararmory.redstone.high";
        };
        return Tooltip.create(Component.translatable(key));
    }

    private Tooltip getRangeDisplayTooltip(boolean state) {
        String key = state ? "tooltip.mattsmodulararmory.range_display.true" : "tooltip.mattsmodulararmory.range_display.false";

        return Tooltip.create(Component.translatable(key));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    private Component getEnergyTooltipComponent() {
        int energyStored = this.menu.getEnergyStored();
        int maxEnergy = this.menu.getMaxEnergyStored();

        String formattedStored = formatEnergy(energyStored);
        String formattedMax = formatEnergy(maxEnergy);

        return Component.translatable("tooltip.mattsmodulararmory.energy", formattedStored, formattedMax);
    }

    private static final DecimalFormat COMPACT_FORMAT = new DecimalFormat("#.#");

    private String formatEnergy(int energy) {
        if (energy < 1_000) {
            return String.valueOf(energy);
        }
        else if (energy < 1_000_000) {
            return COMPACT_FORMAT.format(energy / 1_000.0f) + "k";
        }
        else if (energy < 1_000_000_000) {
            return COMPACT_FORMAT.format(energy / 1_000_000.0f) + "M";
        }
        else {
            return COMPACT_FORMAT.format(energy / 1_000_000_000.0f) + "G";
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // Energy Bar
        if (pMouseX >= x + 9 && pMouseX < x + 23 && pMouseY >= y + 16 && pMouseY < y + 70) {
            pGuiGraphics.renderTooltip(this.font, getEnergyTooltipComponent(), pMouseX, pMouseY);
            return;
        }

        // Slots
        if (this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
            int slotIndex = this.hoveredSlot.getContainerSlot();

            if (!(this.hoveredSlot.container instanceof Inventory)) {
                String key = switch (slotIndex) {
                    case ModularMachineBlockEntity.RANGE_LENGTH_SLOT -> "tooltip.mattsmodulararmory.range_slot.length";
                    case ModularMachineBlockEntity.RANGE_WIDTH_SLOT -> "tooltip.mattsmodulararmory.range_slot.width";
                    case ModularMachineBlockEntity.RANGE_HEIGHT_SLOT -> "tooltip.mattsmodulararmory.range_slot.height";
                    case ModularMachineBlockEntity.FILTER_SLOT -> "tooltip.mattsmodulararmory.filter_slot";
                    case ModularMachineBlockEntity.AUGMENT_SLOT -> "tooltip.mattsmodulararmory.augment_addon_slot";
                    default -> null;
                };

                if (key != null) {
                    pGuiGraphics.renderTooltip(this.font, Component.translatable(key), pMouseX, pMouseY);
                }
            }
        }
    }
}
