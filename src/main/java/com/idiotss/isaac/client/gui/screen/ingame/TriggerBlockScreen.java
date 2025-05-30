package com.idiotss.isaac.client.gui.screen.ingame;

import com.idiotss.isaac.content.blocks.OldBraceletBlocks;
import com.idiotss.isaac.content.blocks.TriggerBlock.TriggerBlockEntity;
import com.idiotss.isaac.network.packet.c2s.play.TriggerBlockUpdateC2SPacket;
import commonnetwork.api.Dispatcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.ButtonWidget;
import net.minecraft.client.gui.widget.button.CyclingButtonWidget;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Environment(EnvType.CLIENT)
public class TriggerBlockScreen extends Screen {
    // Translations
    private static final Text STRUCTURE_NAME = Text.translatable("trigger_block.trigger_name");
    private static final Text POSITION = Text.translatable("structure_block.position");
    private static final Text SIZE = Text.translatable("trigger_block.size");
    private static final Text SHOW_BOUNDING_BOX = Text.translatable("structure_block.show_boundingbox");
    private static final Text ENABLE_TRIGGER = Text.translatable("trigger_block.enable_trigger");
    private static final Text DISABLE_ON_TRIGGER = Text.translatable("trigger_block.disable_on_trigger");
    private static final Text SET_COMMAND = Text.translatable("advMode.setCommand");

    private BlockRotation rotation = BlockRotation.NONE;
    private boolean showBoundingBox;
    private boolean enableTrigger;
    private boolean shouldDisableOnTrigger;
    private TextFieldWidget inputName;
    private TextFieldWidget inputPosX;
    private TextFieldWidget inputPosY;
    private TextFieldWidget inputPosZ;
    private TextFieldWidget inputSizeX;
    private TextFieldWidget inputSizeY;
    private TextFieldWidget inputSizeZ;
    private ButtonWidget buttonRotate0;
    private ButtonWidget buttonRotate90;
    private ButtonWidget buttonRotate180;
    private ButtonWidget buttonRotate270;
    protected TextFieldWidget consoleCommandTextField;

    private CyclingButtonWidget<Boolean> buttonShowBoundingBox;
    private CyclingButtonWidget<Boolean> buttonEnableTrigger;
    private CyclingButtonWidget<Boolean> buttonDisableOnTrigger;

    private final TriggerBlockEntity triggerBlock;

    public TriggerBlockScreen(TriggerBlockEntity triggerBlock) {
        super(Text.translatable(OldBraceletBlocks.TRIGGER_BLOCK.getTranslationKey()));
        this.triggerBlock = triggerBlock;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredShadowedText(this.textRenderer, this.title, this.width / 2, 10, 16777215);
        graphics.drawShadowedText(this.textRenderer, STRUCTURE_NAME, this.width / 2 - 153, 30, 10526880);
        this.inputName.render(graphics, mouseX, mouseY, delta);

        graphics.drawShadowedText(this.textRenderer, POSITION, this.width / 2 - 153, 70, 10526880);
        this.inputPosX.render(graphics, mouseX, mouseY, delta);
        this.inputPosY.render(graphics, mouseX, mouseY, delta);
        this.inputPosZ.render(graphics, mouseX, mouseY, delta);

        graphics.drawShadowedText(this.textRenderer, SIZE, this.width / 2 - 153, 110, 10526880);
        this.inputSizeX.render(graphics, mouseX, mouseY, delta);
        this.inputSizeY.render(graphics, mouseX, mouseY, delta);
        this.inputSizeZ.render(graphics, mouseX, mouseY, delta);

        graphics.drawShadowedText(this.textRenderer, ENABLE_TRIGGER, this.width / 2 + 154 - this.textRenderer.getWidth(ENABLE_TRIGGER), 70, 10526880);
        graphics.drawShadowedText(this.textRenderer, DISABLE_ON_TRIGGER, this.width / 2 + 154 - this.textRenderer.getWidth(DISABLE_ON_TRIGGER), 110, 10526880);
        graphics.drawShadowedText(this.textRenderer, SHOW_BOUNDING_BOX, this.width / 2 + 154 - this.textRenderer.getWidth(SHOW_BOUNDING_BOX), 150, 10526880);

        graphics.drawShadowedText(this.textRenderer, SET_COMMAND, this.width / 2 - 153, 150, 10526880);
        this.consoleCommandTextField.render(graphics, mouseX, mouseY, delta);
    }

    private void cancel() {
        this.triggerBlock.setRotation(this.rotation);
        this.triggerBlock.setShowBoundingBox(this.showBoundingBox);
        this.triggerBlock.setTriggerEnabled(this.enableTrigger);
        this.triggerBlock.setShouldDisableOnTrigger(this.shouldDisableOnTrigger);
        this.client.setScreen(null);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (keyCode != 257 && keyCode != 335) {
            return false;
        } else {
            this.done();
            return true;
        }
    }

    private void done() {
        if (this.updateBlock(TriggerBlockEntity.Action.UPDATE_DATA)) {
            this.client.setScreen(null);
        }
    }

    public boolean updateBlock(TriggerBlockEntity.Action action) {
        BlockPos blockPos = new BlockPos(this.parseInt(this.inputPosX.getText()), this.parseInt(this.inputPosY.getText()), this.parseInt(this.inputPosZ.getText()));
        Vec3i vec3i = new Vec3i(this.parseInt(this.inputSizeX.getText()), this.parseInt(this.inputSizeY.getText()), this.parseInt(this.inputSizeZ.getText()));
        Dispatcher.sendToServer(
                new TriggerBlockUpdateC2SPacket(
                    this.triggerBlock.getPos(),
                    action,
                    this.inputName.getText(),
                    blockPos,
                    vec3i,
                    this.triggerBlock.getRotation(),
                    this.triggerBlock.shouldShowBoundingBox(),
                    this.consoleCommandTextField.getText(),
                    this.triggerBlock.isTriggerEnabled(),
                    this.triggerBlock.shouldDisableOnTrigger()
            ));
        return true;
    }

    private int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException var3) {
            return 0;
        }
    }

    @Override
    public void closeScreen() {
        this.cancel();
    }

    @Override
    protected void init() {
        this.addDrawableSelectableElement(
                ButtonWidget.builder(CommonTexts.DONE, button -> this.done()).positionAndSize(this.width / 2 - 4 - 150, 210, 150, 20).build()
        );
        this.addDrawableSelectableElement(ButtonWidget.builder(CommonTexts.CANCEL, button -> this.cancel()).positionAndSize(this.width / 2 + 4, 210, 150, 20).build());
        this.rotation = this.triggerBlock.getRotation();
        this.showBoundingBox = this.triggerBlock.shouldShowBoundingBox();
        this.enableTrigger = this.triggerBlock.isTriggerEnabled();
        this.shouldDisableOnTrigger = this.triggerBlock.shouldDisableOnTrigger();

        this.buttonEnableTrigger = this.addDrawableSelectableElement(
                CyclingButtonWidget.onOffBuilder(this.triggerBlock.isTriggerEnabled())
                        .omitKeyText()
                        .build(this.width / 2 + 4 + 100, 80, 50, 20, ENABLE_TRIGGER, (button, enableTrigger) -> this.triggerBlock.setTriggerEnabled(enableTrigger))
        );
        this.buttonDisableOnTrigger = this.addDrawableSelectableElement(
                CyclingButtonWidget.onOffBuilder(this.triggerBlock.shouldDisableOnTrigger())
                        .omitKeyText()
                        .build(this.width / 2 + 4 + 100, 120, 50, 20, DISABLE_ON_TRIGGER, (button, disableOnTrigger) -> this.triggerBlock.setShouldDisableOnTrigger(disableOnTrigger))
        );
        this.buttonShowBoundingBox = this.addDrawableSelectableElement(
                CyclingButtonWidget.onOffBuilder(this.triggerBlock.shouldShowBoundingBox())
                        .omitKeyText()
                        .build(this.width / 2 + 4 + 100, 160, 50, 20, SHOW_BOUNDING_BOX, (button, showBoundingBox) -> this.triggerBlock.setShowBoundingBox(showBoundingBox))
        );

        this.buttonRotate0 = this.addDrawableSelectableElement(ButtonWidget.builder(Text.literal("0"), button -> {
            this.triggerBlock.setRotation(BlockRotation.NONE);
            this.updateRotationButton();
        }).positionAndSize(this.width / 2 - 1 - 40 - 1 - 40 - 20, 185, 40, 20).build());
        this.buttonRotate90 = this.addDrawableSelectableElement(ButtonWidget.builder(Text.literal("90"), button -> {
            this.triggerBlock.setRotation(BlockRotation.CLOCKWISE_90);
            this.updateRotationButton();
        }).positionAndSize(this.width / 2 - 1 - 40 - 20, 185, 40, 20).build());
        this.buttonRotate180 = this.addDrawableSelectableElement(ButtonWidget.builder(Text.literal("180"), button -> {
            this.triggerBlock.setRotation(BlockRotation.CLOCKWISE_180);
            this.updateRotationButton();
        }).positionAndSize(this.width / 2 + 1 + 20, 185, 40, 20).build());
        this.buttonRotate270 = this.addDrawableSelectableElement(ButtonWidget.builder(Text.literal("270"), button -> {
            this.triggerBlock.setRotation(BlockRotation.COUNTERCLOCKWISE_90);
            this.updateRotationButton();
        }).positionAndSize(this.width / 2 + 1 + 40 + 1 + 20, 185, 40, 20).build());
        this.inputName = new TextFieldWidget(this.textRenderer, this.width / 2 - 152, 40, 300, 20, Text.translatable("trigger_block.trigger_name")) {
            @Override
            public boolean charTyped(char c, int modifiers) {
                return !TriggerBlockScreen.this.isValidCharacterForName(this.getText(), c, this.getCursor()) ? false : super.charTyped(c, modifiers);
            }
        };
        this.inputName.setMaxLength(128);
        this.inputName.setText(this.triggerBlock.getTriggerName());
        this.addSelectableElement(this.inputName);
        BlockPos blockPos = this.triggerBlock.getOffset();
        this.inputPosX = new TextFieldWidget(this.textRenderer, this.width / 2 - 152, 80, 80, 20, Text.translatable("structure_block.position.x"));
        this.inputPosX.setMaxLength(15);
        this.inputPosX.setText(Integer.toString(blockPos.getX()));
        this.addSelectableElement(this.inputPosX);
        this.inputPosY = new TextFieldWidget(this.textRenderer, this.width / 2 - 72, 80, 80, 20, Text.translatable("structure_block.position.y"));
        this.inputPosY.setMaxLength(15);
        this.inputPosY.setText(Integer.toString(blockPos.getY()));
        this.addSelectableElement(this.inputPosY);
        this.inputPosZ = new TextFieldWidget(this.textRenderer, this.width / 2 + 8, 80, 80, 20, Text.translatable("structure_block.position.z"));
        this.inputPosZ.setMaxLength(15);
        this.inputPosZ.setText(Integer.toString(blockPos.getZ()));
        this.addSelectableElement(this.inputPosZ);
        Vec3i vec3i = this.triggerBlock.getSize();
        this.inputSizeX = new TextFieldWidget(this.textRenderer, this.width / 2 - 152, 120, 80, 20, Text.translatable("trigger_block.size.x"));
        this.inputSizeX.setMaxLength(15);
        this.inputSizeX.setText(Integer.toString(vec3i.getX()));
        this.addSelectableElement(this.inputSizeX);
        this.inputSizeY = new TextFieldWidget(this.textRenderer, this.width / 2 - 72, 120, 80, 20, Text.translatable("trigger_block.size.y"));
        this.inputSizeY.setMaxLength(15);
        this.inputSizeY.setText(Integer.toString(vec3i.getY()));
        this.addSelectableElement(this.inputSizeY);
        this.inputSizeZ = new TextFieldWidget(this.textRenderer, this.width / 2 + 8, 120, 80, 20, Text.translatable("trigger_block.size.z"));
        this.inputSizeZ.setMaxLength(15);
        this.inputSizeZ.setText(Integer.toString(vec3i.getZ()));
        this.addSelectableElement(this.inputSizeZ);

        this.consoleCommandTextField = new TextFieldWidget(this.textRenderer, this.width / 2 - 152, 160, 240, 20, Text.translatable("advMode.command"));
        this.consoleCommandTextField.setMaxLength(32500);
        this.consoleCommandTextField.setText(this.triggerBlock.getCommandExecutor().getCommand());
        this.addSelectableElement(this.consoleCommandTextField);

        this.updateRotationButton();
        this.updateWidgets();
    }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.inputName);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderWorldOverlayBackground(graphics);
    }


    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.inputName.getText();
        String string2 = this.inputPosX.getText();
        String string3 = this.inputPosY.getText();
        String string4 = this.inputPosZ.getText();
        String string5 = this.inputSizeX.getText();
        String string6 = this.inputSizeY.getText();
        String string7 = this.inputSizeZ.getText();
        String string8 = this.consoleCommandTextField.getText();
        this.init(client, width, height);
        this.inputName.setText(string);
        this.inputPosX.setText(string2);
        this.inputPosY.setText(string3);
        this.inputPosZ.setText(string4);
        this.inputSizeX.setText(string5);
        this.inputSizeY.setText(string6);
        this.inputSizeZ.setText(string7);
        this.consoleCommandTextField.setText(string8);
    }

    private void updateRotationButton() {
        this.buttonRotate0.active = true;
        this.buttonRotate90.active = true;
        this.buttonRotate180.active = true;
        this.buttonRotate270.active = true;
        switch (this.triggerBlock.getRotation()) {
            case NONE:
                this.buttonRotate0.active = false;
                break;
            case CLOCKWISE_180:
                this.buttonRotate180.active = false;
                break;
            case COUNTERCLOCKWISE_90:
                this.buttonRotate270.active = false;
                break;
            case CLOCKWISE_90:
                this.buttonRotate90.active = false;
        }
    }

    private void updateWidgets() {
        this.inputName.setVisible(true);
        this.inputPosX.setVisible(true);
        this.inputPosY.setVisible(true);
        this.inputPosZ.setVisible(true);
        this.inputSizeX.setVisible(true);
        this.inputSizeY.setVisible(true);
        this.inputSizeZ.setVisible(true);
        this.buttonRotate0.visible = true;
        this.buttonRotate90.visible = true;
        this.buttonRotate180.visible = true;
        this.buttonRotate270.visible = true;
        this.buttonShowBoundingBox.visible = true;
        this.buttonEnableTrigger.visible = true;
        this.buttonDisableOnTrigger.visible = true;
    }

}
