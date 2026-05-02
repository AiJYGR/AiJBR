package com.aijygr.Entity.BlockEntity;

import javax.annotation.Nullable;

import com.aijygr.AiJGame.Game;
import com.aijygr.Reg;
import com.aijygr.Block.ContainerBlock;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class LootContainer extends RandomizableContainerBlockEntity{
    private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
    private long lastopenedtick = 0;
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            level.setBlock(pos, state.setValue(ContainerBlock.OPEN, true), 3);
            LootContainer.this.playSound(state, SoundEvents.BARREL_OPEN);
        }
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            level.setBlock(pos, state.setValue(ContainerBlock.OPEN, false), 3);
            LootContainer.this.playSound(state, SoundEvents.BARREL_CLOSE);
        }

        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int p_155466_, int p_155467_) {
        }

        protected boolean isOwnContainer(Player player) {
            if (!(player.containerMenu instanceof ChestMenu)) {
                return false;
            } else {
                Container container = ((ChestMenu) player.containerMenu).getContainer();
                return container == LootContainer.this || container instanceof CompoundContainer
                        && ((CompoundContainer) container).contains(LootContainer.this);
            }
        }

    };

    public LootContainer(BlockPos p_155630_, BlockState p_155631_) {
        super(Reg.LOOTCONTAINER_BLOCCKENTITY.get(), p_155630_, p_155631_);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.aijbr.loot_container");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory) {
        return new ChestMenu(MenuType.GENERIC_9x1, pContainerId, pPlayerInventory, this, 1);
    }

    @Override
    public void unpackLootTable(@Nullable Player player)
    {
        if(Game.refillTick == lastopenedtick)
            return ;
        if (this.lootTable != null && this.level.getServer() != null)
        {
            LootTable loottable = this.level.getServer().getLootData().getLootTable(this.lootTable);
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer) player, this.lootTable);
            }
            items = NonNullList.withSize(9, ItemStack.EMPTY);
            lastopenedtick = Game.refillTick;
            LootParams.Builder lootparams$builder = (new LootParams.Builder((ServerLevel) this.level))
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition));
            if (player != null) {
                lootparams$builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
            }
            this.lootTableSeed = 0;
            LootParams params = lootparams$builder.create(LootContextParamSets.CHEST);
            var generatedLoot = loottable.getRandomItems(params, this.lootTableSeed);
            this.clearContent();//清空物品
            int i = 0;
            for(ItemStack itemstack : mergeLootList(generatedLoot)){
                this.setItem(i,itemstack);
                i++;
            }
        }
    }
    public static List<ItemStack> mergeLootList(ObjectArrayList<ItemStack> items) {
        List<ItemStack> mergedItems = new ArrayList<>();
        for (ItemStack item : items) {
            if (item.isEmpty()) continue;
            boolean isMerged = false;
            for (ItemStack mergedItem : mergedItems) {
                if (ItemStack.isSameItemSameTags(item, mergedItem)) {
                    int t = Math.min(item.getCount(), mergedItem.getMaxStackSize() - mergedItem.getCount());
                    if (t > 0) {
                        mergedItem.grow(t);
                        item.shrink(t);
                    }
                }
                if (item.isEmpty()) {
                    isMerged = true;
                    break;
                }
            }
            if (!isMerged && !item.isEmpty()) {
                mergedItems.add(item.copy());
            }
        }
        return mergedItems;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, items);
        }
    }

    @Override
    public void load(CompoundTag tag) {

        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
    }

    public void recheckOpen(Player player) {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    void playSound(BlockState pState, SoundEvent pSound)
    {
      Vec3i vec3i = pState.getValue(BarrelBlock.FACING).getNormal();
      double d0 = (double)this.worldPosition.getX() + 0.5D + (double)vec3i.getX() / 2.0D;
      double d1 = (double)this.worldPosition.getY() + 0.5D + (double)vec3i.getY() / 2.0D;
      double d2 = (double)this.worldPosition.getZ() + 0.5D + (double)vec3i.getZ() / 2.0D;
      this.getLevel().playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
   }
}
