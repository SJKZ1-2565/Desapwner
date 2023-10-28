package sjkz1.com.despawner.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DespawnerScreen extends AbstractContainerScreen<DespawnerMenu> {

    private static final ResourceLocation BEACON_LOCATION = new ResourceLocation("despawner","textures/gui/despawner.png");

    @Nullable
    int cooldown;

    @Nullable
    private Zombie zombiePreview;
    @Nullable
    private Creeper creeperPreview;
    @Nullable
    private Skeleton skeletonPreview;
    @Nullable
    private Spider spiderPreview;

    private static final Vector3f TRANSLATION = new Vector3f();
    private static final Vector3f SPIDER_TRANSLATION = new Vector3f(0.0f,0.0f,1.0f);
    private static final Quaternionf ANGLE = new Quaternionf().rotationXYZ(0.43633232f, 0.0f, (float)Math.PI);



    public DespawnerScreen(final DespawnerMenu despawnerMenu, Inventory inventory, Component component) {
        super(despawnerMenu, inventory, component);
        this.imageWidth = 230;
        this.imageHeight = 219;
        despawnerMenu.addSlotListener(new ContainerListener(){

            @Override
            public void slotChanged(AbstractContainerMenu abstractContainerMenu, int i, ItemStack itemStack) {
            }

            @Override
            public void dataChanged(AbstractContainerMenu abstractContainerMenu, int i, int j) {
                DespawnerScreen.this.cooldown = despawnerMenu.getCoolDown();
            }
        });
    }


    @Override
    protected void init() {
        super.init();
        this.zombiePreview = new Zombie(EntityType.ZOMBIE,this.minecraft.level);
        this.zombiePreview.yBodyRot = 230.0f;
        this.zombiePreview.setXRot(0.0f);
        this.zombiePreview.yHeadRot = 230;
        this.zombiePreview.yHeadRotO = 230;

        this.creeperPreview = new Creeper(EntityType.CREEPER,this.minecraft.level);
        this.creeperPreview.yBodyRot = 180.0f;
        this.creeperPreview.setXRot(0.0f);
        this.creeperPreview.yHeadRot = 180;
        this.creeperPreview.yHeadRotO = 180;

        this.skeletonPreview = new Skeleton(EntityType.SKELETON,this.minecraft.level);
        this.skeletonPreview.setItemInHand(InteractionHand.MAIN_HAND,Items.BOW.getDefaultInstance());
        this.skeletonPreview.setAggressive(true);
        this.skeletonPreview.yBodyRot = 130.0F;
        this.skeletonPreview.setXRot(-5);
        this.skeletonPreview.yHeadRot = 130.0F;
        this.skeletonPreview.yHeadRotO = 130.0F;

        this.spiderPreview = new Spider(EntityType.SPIDER,this.minecraft.level);
        this.spiderPreview.yBodyRot = 360.0F;
        this.spiderPreview.setXRot(0.0f);
        this.spiderPreview.yHeadRot = 0.0F;
        this.spiderPreview.yHeadRotO = 0.0F;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int i, int j) {
        guiGraphics.drawCenteredString(this.font, StringUtil.formatTickDuration(DespawnerScreen.this.cooldown), 25, 8, (DespawnerScreen.this.cooldown / 20) % 2 == 0 ? 0xE0E0E0 : 0XFF584D);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(BEACON_LOCATION, k, l, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0f, 0.0f, 300.0f);
        guiGraphics.renderItem(new ItemStack(Items.DIAMOND_SWORD), k + 42 + 66, l + 109);
        guiGraphics.pose().popPose();
        InventoryScreen.renderEntityInInventory(guiGraphics, this.leftPos + 151, this.topPos + 75, 25, TRANSLATION, ANGLE, null, this.zombiePreview);
        InventoryScreen.renderEntityInInventory(guiGraphics, this.leftPos + 171, this.topPos + 85, 25, TRANSLATION, ANGLE, null, this.creeperPreview);
        InventoryScreen.renderEntityInInventory(guiGraphics, this.leftPos + 171, this.topPos + 45, 25, SPIDER_TRANSLATION, ANGLE, null, this.spiderPreview);
        InventoryScreen.renderEntityInInventory(guiGraphics, this.leftPos + 191, this.topPos + 75, 25, TRANSLATION, ANGLE, null, this.skeletonPreview);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.menu.updateLevel();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        this.renderTooltip(guiGraphics, i, j);
    }

}
