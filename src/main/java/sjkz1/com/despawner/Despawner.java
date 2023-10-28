package sjkz1.com.despawner;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import sjkz1.com.despawner.block.DespawnerBlock;
import sjkz1.com.despawner.block.DespawnerBlockEntity;
import sjkz1.com.despawner.menu.DespawnerMenu;
import sjkz1.com.despawner.menu.DespawnerScreen;

public class Despawner implements ModInitializer {

    public static final MenuType<DespawnerMenu> DESPAWNER_MENU = new MenuType<>(DespawnerMenu::new,FeatureFlags.DEFAULT_FLAGS);

    public static final Block DESPAWNER_BLOCK = new DespawnerBlock(BlockBehaviour.Properties.copy(Blocks.BEACON));
    public static final ResourceLocation INTERACT_WITH_DESPAWNER = Registry.register(BuiltInRegistries.CUSTOM_STAT,"interact_with_despawner",new ResourceLocation("despawner","interact_with_despawner"));;
    public static final BlockEntityType<DespawnerBlockEntity> DESPAWNER_BLOCK_ENTITY_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,new ResourceLocation("despawner","despawner_block_entity"), FabricBlockEntityTypeBuilder.create(DespawnerBlockEntity::new, DESPAWNER_BLOCK).build());


    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation("despawner", "despawner_block"), DESPAWNER_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("despawner", "despawner_block"), new BlockItem(DESPAWNER_BLOCK, new Item.Properties()));
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation("despawner", "despawner_menu"), DESPAWNER_MENU);
        MenuScreens.register(DESPAWNER_MENU, DespawnerScreen::new);
    }
}
