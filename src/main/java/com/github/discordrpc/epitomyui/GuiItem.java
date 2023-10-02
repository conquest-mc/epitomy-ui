package com.github.discordrpc.epitomyui;

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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GuiItem {
    private final ItemStack item;
    private Consumer<InventoryClickEvent> clickEvent;
    private Consumer<InventoryDragEvent> dragEvent;

    public GuiItem(final Material material) {
        this(new ItemStack(material));
    }

    public GuiItem(final Material material, final int amount) {
        this(new ItemStack(material, amount));
    }

    public GuiItem(ItemStack item) {
        this.item = item;
        this.clickEvent = event -> {};
        this.dragEvent = event -> {};
    }

    /**
     * Sets the item display name.
     *
     * @param name The name to set
     */
    public @Nonnull GuiItem setName(@Nonnull final String name) {
        final Component component = MiniMessage.miniMessage().deserialize(name);
        return setName(component);
    }

    /**
     * Sets the item display name.
     *
     * @param name The name to set
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem setName(@Nonnull final Component name) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;
        meta.displayName(name);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the item lore.
     *
     * @param lines The lines of lore to set
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem setLore(final Component ...lines) {
        return setLore(Arrays.asList(lines));
    }

    /**
     * Sets the item lore.
     *
     * @param lines The lines of lore to set
     * @return The {@link GuiItem} instance
     */
    public @NotNull GuiItem setLore(@NotNull final List<Component> lines) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;
        meta.lore(lines);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Appends lines to the item lore.
     *
     * @param lines The lines to append
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem addLore(final Component ...lines) {
        return addLore(Integer.MAX_VALUE, Arrays.asList(lines));
    }

    /**
     * Appends lines to the item lore.
     *
     * @param lines The lines to append
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem addLore(@Nonnull final List<Component> lines) {
        return addLore(Integer.MAX_VALUE, lines);
    }

    /**
     * Adds lines to the item lore at the given line index.
     *
     * @param line The line to add new lore at
     * @param lines The lines to add
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem addLore(@Nonnegative int line, final Component ...lines) {
        return addLore(line, Arrays.asList(lines));
    }

    /**
     * Adds lines to the item lore at the given line index.
     *
     * @param line The line to add new lore at
     * @param lines The lines to add
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem addLore(@Nonnegative int line, @Nonnull final List<Component> lines) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;
        List<Component> lore = meta.lore();
        if (lore == null) lore = new ArrayList<>();
        if (line > lore.size() - 1) line = lore.size() - 1;
        lore.addAll(line, lines);
        meta.lore(lore);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the amount of items in the {@link ItemStack}.
     *
     * @param amount The amount of items
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem setAmount(final int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Sets the durability of the items in the {@link ItemStack}.
     *
     * @param durability The durability of the items
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem setDurability(@Nonnegative final int durability) {
        return setDurability((short) durability);
    }

    /**
     * Sets the durability of the items in the {@link ItemStack}.
     *
     * @param durability The durability of the items
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem setDurability(@Nonnegative short durability) {
        final ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable damageable)) return this;
        if (durability > item.getType().getMaxDurability()) durability = item.getType().getMaxDurability();
        damageable.setDamage(durability);
        item.setItemMeta(damageable);
        return this;
    }

    /**
     * Hides the given flags on the item.
     *
     * @param flags The flags to hide
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem hideFlags(List<ItemFlag> flags) {
        return hideFlags(flags.toArray(new ItemFlag[0]));
    }

    /**
     * Hides the given flags on the item.
     *
     * @param flags The flags to hide
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem hideFlags(ItemFlag ...flags) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;
        meta.addItemFlags(flags);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Applies the given enchantments to the item.
     *
     * @param enchantments The enchantments to apply
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem enchant(Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            enchant(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Applies the given enchantment to the item.
     *
     * @param enchantment The enchantment to apply
     * @param level The level of the enchantment to apply
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem enchant(final Enchantment enchantment, final int level) {
        if (item.getType() == Material.ENCHANTED_BOOK) {
            final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            if (meta == null) return this;
            meta.addStoredEnchant(enchantment, level, true);
            item.setItemMeta(meta);
        } else {
            item.addUnsafeEnchantment(enchantment, level);
        }
        return this;
    }

    /**
     * Sets the unbreakable tag on the item.
     *
     * @param unbreakable Should the item be unbreakable or not
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem setUnbreakable(final boolean unbreakable) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Adds the given attribute modifiers to the item.
     *
     * @param modifiers The modifiers to add
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem addAttributeModifiers(Map<Attribute, AttributeModifier> modifiers) {
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entrySet()) {
            addAttributeModifier(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Adds the given attribute modifier to the item.
     *
     * @param attribute The attribute to add
     * @param modifier The modifier to add
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem addAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;
        meta.addAttributeModifier(attribute, modifier);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Called when the item is clicked on.
     *
     * @param consumer The method to execute when clicked
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem onClick(Consumer<InventoryClickEvent> consumer) {
        this.clickEvent = consumer;
        return this;
    }

    public @Nonnull Consumer<InventoryClickEvent> getClickEvent() {
        return clickEvent;
    }

    /**
     * Called when the item is dragged.
     *
     * @param consumer The method to execute on drag
     * @return The {@link GuiItem} instance
     */
    public @Nonnull GuiItem onDrag(Consumer<InventoryDragEvent> consumer) {
        this.dragEvent = consumer;
        return this;
    }

    public @Nonnull Consumer<InventoryDragEvent> getDragEvent() {
        return dragEvent;
    }

    public @Nonnull ItemStack getItem() {
        return item;
    }
}
