package com.example.binslot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class TrashSlot extends Slot {
    public TrashSlot() {
        super(new TrashInventory(), 0, 0, 0);
    }

    public static class TrashInventory implements Inventory {
        private ItemStack currentStack = ItemStack.EMPTY;

        @Override
        public int size() {
            return 1;
        }

        @Override
        public int getMaxCountPerStack() {
            return 64;
        }

        @Override
        public boolean isEmpty() {
            return currentStack.isEmpty();
        }

        @Override
        public ItemStack getStack(int slot) {
            return currentStack;
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            return ItemStack.EMPTY;

//            ItemStack itemStack = !currentStack.isEmpty() && amount > 0 ? currentStack.split(amount) : ItemStack.EMPTY;
//
//            if (!itemStack.isEmpty())
//                this.markDirty();
//
//            return itemStack;
        }

        @Override
        public ItemStack removeStack(int slot) {
            ItemStack itemStack = currentStack;

            currentStack = ItemStack.EMPTY;

            return itemStack;
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            // Do nothing

            // currentStack = stack;
        }

        @Override
        public void markDirty() {
            // Do nothing
        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) {
            return true;
        }

        @Override
        public void clear() {
            // Already always clear

            currentStack = ItemStack.EMPTY;
        }
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return true; // Allow any item to be inserted
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return false; // Don't allow taking items out (they get deleted)
    }

    @Override
    public ItemStack takeStack(int amount) {
        return ItemStack.EMPTY; // Return empty stack when trying to take
    }

    @Override
    public ItemStack insertStack(ItemStack stack) {
        // Delete the items by returning empty stack
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        // Delete the items by returning empty stack
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(ItemStack stack) {
        // Don't actually set the stack, effectively deleting it
        super.setStack(ItemStack.EMPTY);
    }

    @Override
    public void setStackNoCallbacks(ItemStack stack) {
        // Don't actually set the stack, effectively deleting it
        this.inventory.setStack(this.getIndex(), ItemStack.EMPTY);
    }

    @Override
    public ItemStack getStack() {
        // Always return empty stack since items get deleted
        return ItemStack.EMPTY;
    }

    @Override
    public void markDirty() {
        // Override to prevent unnecessary updates
    }
}
