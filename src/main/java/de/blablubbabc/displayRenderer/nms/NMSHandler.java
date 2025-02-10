package de.blablubbabc.displayRenderer.nms;

import org.bukkit.craftbukkit.v1_21_R3.entity.CraftTextDisplay;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftChatMessage;
import org.bukkit.entity.TextDisplay;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.network.chat.IChatBaseComponent;

public class NMSHandler {

	public static final NMSHandler INSTANCE = new NMSHandler();

	public NMSHandler() {
	}

	public void setText(TextDisplay entity, String text, String font) {
		var spigotComponent = new TextComponent(text);
		spigotComponent.setFont(font);
		IChatBaseComponent component = CraftChatMessage.fromJSONOrNull(ComponentSerializer.toString(spigotComponent));

		var craftEntity = (CraftTextDisplay) entity;
		craftEntity.getHandle().a(component);
	}
}
