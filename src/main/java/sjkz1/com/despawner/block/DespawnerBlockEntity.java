package sjkz1.com.despawner.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sjkz1.com.despawner.Despawner;
import sjkz1.com.despawner.menu.DespawnerMenu;

import java.util.List;

public class DespawnerBlockEntity extends BlockEntity
        implements MenuProvider {

    int levels;
    int cooldown;

    private static final Component DISPLAY_NAME = Component.translatable("container.despawner");

    private final ContainerData dataAccess = new ContainerData() {


        @Override
        public int get(int i) {
            return switch (i) {
                case 0 -> DespawnerBlockEntity.this.levels;
                case 1 -> DespawnerBlockEntity.this.cooldown;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
        }

        @Override
        public void set(int i, int j) {
            switch (i) {
                case 0 -> {
                    DespawnerBlockEntity.this.levels = j;
                }
                case 1 -> {
                    DespawnerBlockEntity.this.cooldown = j;
                }
            }

        }
        @Override
        public int getCount () {
            return 2;
        }
    };

    private LockCode lockKey = LockCode.NO_LOCK;


    public DespawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Despawner.DESPAWNER_BLOCK_ENTITY_ENTITY, blockPos, blockState);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return DISPLAY_NAME;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, DespawnerBlockEntity despawnerBlockEntity) {
        if(despawnerBlockEntity.dataAccess.get(0) == 0) {
            despawnNoDrop(level,blockPos,despawnerBlockEntity);
        }
        if(despawnerBlockEntity.dataAccess.get(1) > 0) {
            despawnerBlockEntity.dataAccess.set(1,despawnerBlockEntity.dataAccess.get(1)-1);
        }
        if(despawnerBlockEntity.dataAccess.get(1) == 0)
        {
            despawnerBlockEntity.dataAccess.set(1,319);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("Levels", this.levels);
        compoundTag.putInt("Cooldown", this.cooldown);
        this.lockKey.addToTag(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.lockKey = LockCode.fromTag(compoundTag);
    }

    private static void despawnNoDrop(Level level, BlockPos blockPos, DespawnerBlockEntity despawnerBlockEntity) {
        if (level.isClientSide) {
            return;
        }
        AABB aABB = new AABB(blockPos).inflate(16).expandTowards(0.0, level.getHeight(), 0.0);
        List<Monster> list = level.getEntitiesOfClass(Monster.class, aABB);
        for (Monster monster : list) {
            if(despawnerBlockEntity.dataAccess.get(1) <= 1) {
                if (monster.getMaxHealth() < 50 && !monster.isPersistenceRequired()) {
                    monster.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }

    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        if (BaseContainerBlockEntity.canUnlock(player, this.lockKey, this.getDisplayName())) {
            return new DespawnerMenu(i, inventory, this.dataAccess, ContainerLevelAccess.create(this.level, this.getBlockPos()));
        }
        return null;
    }
}
