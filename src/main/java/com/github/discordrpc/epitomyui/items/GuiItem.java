package com.github.discordrpc.epitomyui.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GuiItem {
    private ItemStack item;
    private Consumer<InventoryClickEvent> clickEvent;
    private Consumer<InventoryDragEvent> dragEvent;

    public GuiItem(@Nonnull final Material material) {
        this(new ItemStack(material));
    }

    public GuiItem(@Nonnull final Material material, final int amount) {
        this(new ItemStack(material, amount));
    }

    public GuiItem(@Nullable final ItemStack item) {
        this.item = item;
        this.clickEvent = event -> {};
        this.dragEvent = event -> {};
    }

    /**
     * Sets the item stack.
     *
     * @param item The item to set
     */
    public void setItem(@Nullable final ItemStack item) {
        this.item = item;
    }

    /**
     * Sets the item material type.
     *
     * @param material The material to set
     */
    public void setMaterial(@Nonnull final Material material) {
        this.item.setType(material);
    }

    /**
     * Sets the item display name.
     *
     * @param name The name to set
     */
    public void setName(@Nonnull final String name) {
        final Component component = MiniMessage.miniMessage().deserialize(name);
        setName(component);
    }

    /**
     * Sets the item display name.
     *
     * @param name The name to set
     */
    public void setName(@Nonnull final Component name) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.displayName(name);
        item.setItemMeta(meta);
    }

    /**
     * Sets the item lore.
     *
     * @param lines The lines of lore to set
     */
    public void setLore(@Nonnull final Component... lines) {
        setLore(Arrays.asList(lines));
    }

    /**
     * Sets the item lore.
     *
     * @param lines The lines of lore to set
     */
    public void setLore(@Nullable final List<Component> lines) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.lore(lines);
        item.setItemMeta(meta);
    }

    /**
     * Appends lines to the item lore.
     *
     * @param lines The lines to append
     */
    public void addLore(@Nonnull final Component... lines) {
        addLore(Integer.MAX_VALUE, Arrays.asList(lines));
    }

    /**
     * Appends lines to the item lore.
     *
     * @param lines The lines to append
     */
    public void addLore(@Nonnull final List<Component> lines) {
        addLore(Integer.MAX_VALUE, lines);
    }

    /**
     * Adds lines to the item lore at the given line index.
     *
     * @param line The line to add new lore at
     * @param lines The lines to add
     */
    public void addLore(@Nonnegative int line, @Nonnull final Component... lines) {
        addLore(line, Arrays.asList(lines));
    }

    /**
     * Adds lines to the item lore at the given line index.
     *
     * @param line The line to add new lore at
     * @param lines The lines to add
     */
    public void addLore(@Nonnegative int line, @Nonnull final List<Component> lines) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        List<Component> lore = meta.lore();
        if (lore == null) lore = new ArrayList<>();
        if (line > lore.size() - 1) line = lore.size() - 1;
        lore.addAll(line, lines);
        meta.lore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Sets the amount of items in the {@link ItemStack}.
     *
     * @param amount The amount of items
     */
    public void setAmount(@Nonnegative final int amount) {
        item.setAmount(amount);
    }

    /**
     * Sets the durability of the items in the {@link ItemStack}.
     *
     * @param durability The durability of the items
     */
    public void setDurability(@Nonnegative final int durability) {
        setDurability((short) durability);
    }

    /**
     * Sets the durability of the items in the {@link ItemStack}.
     *
     * @param durability The durability of the items
     */
    public void setDurability(@Nonnegative short durability) {
        final ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable damageable)) return;
        if (durability > item.getType().getMaxDurability()) durability = item.getType().getMaxDurability();
        damageable.setDamage(durability);
        item.setItemMeta(damageable);
    }

    /**
     * Hides the given flags on the item.
     *
     * @param flags The flags to hide
     */
    public void hideFlags(@Nonnull final List<ItemFlag> flags) {
        hideFlags(flags.toArray(new ItemFlag[0]));
    }

    /**
     * Hides the given flags on the item.
     *
     * @param flags The flags to hide
     */
    public void hideFlags(@Nonnull final ItemFlag... flags) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.addItemFlags(flags);
        item.setItemMeta(meta);
    }

    /**
     * Applies the given enchantments to the item.
     *
     * @param enchantments The enchantments to apply
     */
    public void enchant(@Nonnull final Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            enchant(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Applies the given enchantment to the item.
     *
     * @param enchantment The enchantment to apply
     * @param level The level of the enchantment to apply
     */
    public void enchant(@Nonnull final Enchantment enchantment, @Nonnegative final int level) {
        if (item.getType() == Material.ENCHANTED_BOOK) {
            final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            if (meta == null) return;
            meta.addStoredEnchant(enchantment, level, true);
            item.setItemMeta(meta);
        } else {
            item.addUnsafeEnchantment(enchantment, level);
        }
    }

    /**
     * Sets the unbreakable tag on the item.
     *
     * @param unbreakable Should the item be unbreakable or not
     */
    public void setUnbreakable(final boolean unbreakable) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
    }

    /**
     * Adds the given attribute modifiers to the item.
     *
     * @param modifiers The modifiers to add
     */
    public void addAttributeModifiers(@Nonnull final Map<Attribute, AttributeModifier> modifiers) {
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entrySet()) {
            addAttributeModifier(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds the given attribute modifier to the item.
     *
     * @param attribute The attribute to add
     * @param modifier The modifier to add
     */
    public void addAttributeModifier(@Nonnull final Attribute attribute, @Nonnull final AttributeModifier modifier) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.addAttributeModifier(attribute, modifier);
        item.setItemMeta(meta);
    }

    /**
     * Called when the item is clicked on.
     *
     * @param consumer The method to execute when clicked
     */
    public void onClick(@Nonnull final Consumer<InventoryClickEvent> consumer) {
        this.clickEvent = consumer;
    }

    /**
     * @return The {@link Consumer} to run on an {@link InventoryClickEvent}
     */
    public @Nonnull Consumer<InventoryClickEvent> getClickEvent() {
        return clickEvent;
    }

    /**
     * Called when the item is dragged.
     *
     * @param consumer The method to execute on drag
     */
    public void onDrag(@Nonnull final Consumer<InventoryDragEvent> consumer) {
        this.dragEvent = consumer;
    }

    /**
     * @return The {@link Consumer} to run on an {@link InventoryDragEvent}
     */
    public @Nonnull Consumer<InventoryDragEvent> getDragEvent() {
        return dragEvent;
    }

    /**
     * @return The {@link ItemStack} belonging to the {@link GuiItem}
     */
    public @Nonnull ItemStack getItem() {
        return item;
    }
}
