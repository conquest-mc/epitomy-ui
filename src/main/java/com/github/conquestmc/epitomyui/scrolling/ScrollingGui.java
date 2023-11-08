package com.github.conquestmc.epitomyui.scrolling;

import com.github.conquestmc.epitomyui.GuiBase;
import com.github.conquestmc.epitomyui.items.GuiInteractable;
import com.github.conquestmc.epitomyui.items.GuiItem;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ScrollingGui extends GuiBase {
    private static final Logger LOGGER = Logger.getLogger("epitome-ui");
    private final ItemStack[][] items;
    private final Map<Integer, ScrollHandlerItem> handlers;
    private final Map<Integer, GuiInteractable> interactablesStore;
    private final Map<Integer, GuiInteractable> stickyItems;
    private final int maxRow;
    private final int maxColumn;
    private int row;
    private int column;

    public ScrollingGui(
            @Nullable final String title,
            @Nonnegative int rows,
            @Nonnegative int columns,
            @Nonnegative final int rowStart,
            @Nonnegative final int columnStart
    ) {
        super(title, 6);
        if (rows < 6) rows = 6;
        if (columns < 9) columns = 9;
        this.items = new ItemStack[rows][columns];
        this.handlers = new HashMap<>(54);
        this.interactablesStore = new HashMap<>(rows * columns);
        this.stickyItems = new HashMap<>(54);
        this.maxRow = rows - 6;
        this.maxColumn = columns - 9;
        this.row = rowStart > maxRow ? maxRow : rowStart;
        this.column = columnStart > maxColumn ? maxColumn : columnStart;
    }

    /**
     * Updates the inventory based on the current
     * row and column.
     */
    @Override
    public void update() {
        this.interactables.clear();

        int contextSlot = -1;
        for (int row = this.row; row < this.row + 6; row++) {
            ItemStack[] items = this.items[row];
            for (int column = this.column; column < this.column + 9; column++) {
                contextSlot++;

                GuiInteractable interactable = null;
                if (this.handlers.containsKey(contextSlot)) {
                    ScrollHandlerItem handler = this.handlers.get(contextSlot);
                    switch (handler.getScrollDirection()) {
                        case UP -> { if (this.row != 0) interactable = handler; }
                        case DOWN -> { if (this.row != this.maxRow) interactable = handler; }
                        case LEFT -> { if (this.column != 0) interactable = handler; }
                        case RIGHT -> { if (this.column != this.maxColumn) interactable = handler; }
                    }
                } else if (this.stickyItems.containsKey(contextSlot)) {
                    interactable = this.stickyItems.get(contextSlot);
                } else {
                    final int slot = getSlot(row, column);
                    interactable = this.interactablesStore.get(slot);
                }

                if (interactable == null) {
                    ItemStack item = items[column];
                    this.inventory.setItem(contextSlot, item);
                } else {
                    this.interactables.put(contextSlot, interactable);
                    this.inventory.setItem(contextSlot, interactable.getItem());
                }
            }
        }
    }

    /**
     * Checks if a slot is currently visible in the inventory.
     *
     * @param slot The slot to check
     * @return True if the slot is visible, otherwise false
     */
    private boolean isVisible(@Nonnegative final int slot) {
        final int row = this.getRow(slot);
        final int column = this.getColumn(slot);
        return this.isVisible(row, column);
    }

    /**
     * Checks if a slot is currently visible in the inventory.
     *
     * @param row The row to check
     * @param column The column to check
     * @return True if the slot is visible, otherwise false
     */
    private boolean isVisible(@Nonnegative final int row, @Nonnegative final int column) {
        return this.isRowVisible(row) && this.isColumnVisible(column);
    }

    /**
     * Checks if any part of a row is currently visible
     * in the inventory.
     *
     * @param row The row to check
     * @return True if any part of the row is visible, otherwise false
     */
    public boolean isRowVisible(@Nonnegative final int row) {
        return row >= this.row
                && row <= this.row + 5;
    }

    /**
     * Checks if any part of a column is currently visible
     * in the inventory.
     *
     * @param column The column to check
     * @return True if any part of the column is visible, otherwise false
     */
    public boolean isColumnVisible(@Nonnegative final int column) {
        return column >= this.column
                && column <= this.column + 8;
    }

    /**
     * Fills all slots in the scroll GUI with the given item.
     *
     * @param item The item to fill the scroll GUI with
     */
    public void fill(@Nonnull final GuiInteractable item) {
        for (int row = 0; row < this.items.length; row++) {
            ItemStack[] items = this.items[row];
            for (int column = 0; column < items.length; column++) {
                items[column] = item.getItem();
                final int slot = getSlot(row, column);
                this.interactablesStore.put(slot, item);
            }
        }
        this.update();
    }

    /**
     * Fills all slots in the scroll GUI with the given item.
     *
     * @param item The item to fill the scroll GUI with
     */
    public void fill(@Nullable final ItemStack item) {
        for (ItemStack[] row : this.items) {
            Arrays.fill(row, item);
        }
        this.update();
    }

    /**
     * Fills all empty slots in the scroll GUI with the given item.
     *
     * @param item The item to fill empty slots with
     */
    public void fillEmpty(@Nonnull final GuiInteractable item) {
        for (int row = 0; row < this.items.length; row++) {
            ItemStack[] items = this.items[row];
            for (int column = 0; column < items.length; column++) {
                if (items[column] != null) continue;
                final int slot = getSlot(row, column);
                this.interactablesStore.put(slot, item);
                items[column] = item.getItem();
            }
        }
        this.update();
    }

    /**
     * Fills all empty slots in the scroll GUI with the given item.
     *
     * @param item The item to fill empty slots with
     */
    public void fillEmpty(@Nonnull final ItemStack item) {
        for (ItemStack[] row : this.items) {
            for (int column = 0; column < row.length; column++) {
                if (row[column] != null) continue;
                row[column] = item;
            }
        }
    }

    /**
     * Sets an item to handle scrolling in the given direction.
     *
     * @param slot The inventory slot to set the handler at
     * @param item The handler item
     * @param direction The scroll direction of the handler
     */
    public void setScrollHandler(@Nonnegative final int slot, @Nonnull final ItemStack item, @Nonnull final ScrollDirection direction) {
        this.setScrollHandler(slot, new ScrollHandlerItem(direction, item));
    }

    /**
     * Sets an item to handle scrolling in the given direction.
     *
     * @param slot The inventory slot to set the handler at
     * @param item The handler item
     */
    public void setScrollHandler(@Nonnegative final int slot, @Nonnull final ScrollHandlerItem item) {
        if (slot > 53) throw new IndexOutOfBoundsException("Scroll handler must have a slot between 0 and 53");

        switch (item.getScrollDirection()) {
            case UP -> item.onClick(event -> {
                if (row <= 0) return;
                this.row--;
                this.update();
            });
            case DOWN -> item.onClick(event -> {
                if (row >= maxRow) return;
                this.row++;
                this.update();
            });
            case LEFT -> item.onClick(event -> {
                if (column <= 0) return;
                column--;
                this.update();
            });
            case RIGHT -> item.onClick(event -> {
                if (column >= maxColumn) return;
                column++;
                this.update();
            });
        }

        this.handlers.put(slot, item);
        this.interactables.put(slot, item);
        this.inventory.setItem(slot, item.getItem());
    }

    /**
     * Sets a sticky item in the inventory.
     *
     * @param slot The context slot (0 - 53) to set the item at
     * @param item The item to set
     */
    public void setStickyItem(@Nonnegative final int slot, @Nonnull final ItemStack item) {
        this.setStickyItem(slot, new GuiItem(item));
    }

    /**
     * Sets a sticky item in the inventory.
     *
     * @param slot The context slot (0 - 53) to set the item at
     * @param item The item to set
     */
    public void setStickyItem(@Nonnegative final int slot, @Nonnull final GuiInteractable item) {
        try {
            if (slot > 53) throw new IndexOutOfBoundsException("Sticky items must have a slot between 0 and 53");
            this.stickyItems.put(slot, item);
            this.update();
        } catch (IndexOutOfBoundsException e) {
            LOGGER.log(Level.SEVERE, "Could not a stick item to the ScrollingGui", e);
        }
    }

    /**
     * Sets an item in the scroll GUI.
     *
     * @param slot The slot to set the item at
     * @param item The item to set
     */
    @Override
    public void setItem(@Nonnegative final int slot, @Nonnull final GuiInteractable item) {
        this.setItem(getRow(slot), getColumn(slot), item);
    }

    /**
     * Sets an item in the scroll GUI.
     *
     * @param row The row to set the item at
     * @param column The column to set the item at
     * @param item The item to set
     */
    public void setItem(@Nonnegative final int row, @Nonnegative final int column, @Nonnull final GuiInteractable item) {
        this.setItem(row, column, item.getItem());
        if (this.isVisible(row, column)) {
            final int slot = getContextSlotFrom(row, column);
            this.interactables.put(slot, item);
        } else {
            final int slot = getSlot(row, column);
            this.interactablesStore.put(slot, item);
        }
    }

    /**
     * Sets  an item in the scroll GUI.
     *
     * @param slot The slot to set the item at
     * @param item The item to set
     */
    public void setItem(@Nonnegative final int slot, @Nullable final ItemStack item) {
        this.setItem(getRow(slot), getColumn(slot), item);
    }

    public void setItem(@Nonnegative final int row, @Nonnegative final int column, @Nullable final ItemStack item) {
        this.items[row][column] = item;
        if (!this.isVisible(row, column)) return;
        final int slot = this.getContextSlotFrom(row, column);
        this.inventory.setItem(slot, item);
    }

    /**
     * Gets a slot from the given row and column.
     *
     * @param row The row to get the slot from
     * @param column The column to get the slot from
     * @return The slot corresponding to the row and column
     */
    public int getSlot(@Nonnegative final int row, @Nonnegative final int column) {
        return row * (this.maxColumn + 8) + column;
    }

    /**
     * Gets the row from the given slot.
     *
     * @param slot The slot to get the row from
     * @return The row the slot belongs to
     */
    public int getRow(@Nonnegative final int slot) {
        return slot / (this.maxRow + 5);
    }

    /**
     * Gets the column from the given slot.
     *
     * @param slot The slot to get the column from
     * @return The column the slot belongs to
     */
    public int getColumn(@Nonnegative final int slot) {
        return slot % (this.maxColumn + 8);
    }

    /**
     * Gets a context slot (0 - 53) from a given slot. Context
     * slots are used to set items in the inventory object.
     *
     * @param slot The slot to get the context slot from
     * @return A context slot between 0 and 53, or -1 if there is no valid context slot
     */
    public int getContextSlotFrom(@Nonnegative final int slot) {
        final int row = this.getRow(slot);
        final int column = this.getColumn(slot);
        return this.getContextSlotFrom(row, column);
    }

    /**
     * Gets a context slot (0 - 53) from a given slot. Context
     * slots are used to set items in the inventory object.
     *
     * @param row The row get the context slot from
     * @param column The column to get the context lot from
     * @return A context slot between 0 and 53, or -1 if there is no valid context slot
     */
    public int getContextSlotFrom(@Nonnegative final int row, @Nonnegative final int column) {
        if (!this.isVisible(row, column)) return -1;
        final int contextRow = row - this.row;
        final int contextColumn = column - this.column;
        return contextRow * 9 + (contextColumn);
    }
}
