package de.blablubbabc.sprites.nms;

import org.bukkit.craftbukkit.v1_21_R3.entity.CraftTextDisplay;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftChatMessage;
import org.bukkit.entity.TextDisplay;
import org.checkerframework.checker.nullness.qual.Nullable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.network.chat.IChatBaseComponent;

public class NMSHandler {

	public static final NMSHandler INSTANCE = new NMSHandler();

	public NMSHandler() {
	}

	public void setText(TextDisplay entity, String text, @Nullable ChatColor color, String font) {
		var spigotComponent = new TextComponent(text);
		spigotComponent.setFont(font);
		spigotComponent.setColor(color);

		IChatBaseComponent component = CraftChatMessage.fromJSONOrNull(ComponentSerializer.toString(spigotComponent));

		var craftEntity = (CraftTextDisplay) entity;
		craftEntity.getHandle().a(component);
	}
}
