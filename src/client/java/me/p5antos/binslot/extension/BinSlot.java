package me.p5antos.binslot.extension;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BinSlot extends Slot {
    public BinSlot() {
        super(new BinInventory(), 0, 0, 0);
    }

    public static class BinInventory implements Container {
        @Override
        public int getContainerSize() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public ItemStack getItem(int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            // Do nothing
        }

        @Override
        public void setChanged() {
            // Do nothing
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }

        @Override
        public void clearContent() {
            // Already always empty
        }
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return true;
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack safeInsert(ItemStack stack) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack safeInsert(ItemStack stack, int maxCount) {
        return ItemStack.EMPTY;
    }

    @Override
    public void set(ItemStack stack) {
        super.set(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void setChanged() {
        // Override to prevent unnecessary updates
    }
}
