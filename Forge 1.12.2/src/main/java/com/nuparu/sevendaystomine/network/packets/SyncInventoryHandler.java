package com.nuparu.sevendaystomine.network.packets;

import java.util.HashMap;

import com.nuparu.sevendaystomine.util.PlayerInventorySyncHelper;
import com.nuparu.sevendaystomine.util.item.ItemCache;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SyncInventoryHandler implements IMessageHandler<SyncInventoryMessage, IMessage> {

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(SyncInventoryMessage message, MessageContext ctx) {
		if (PlayerInventorySyncHelper.itemsCache == null) {
			PlayerInventorySyncHelper.itemsCache = new HashMap<String, ItemCache>();
		}
		PlayerInventorySyncHelper.itemsCache.put(message.getName(), message.getItems().deserialize());
		return null;
	}

}
