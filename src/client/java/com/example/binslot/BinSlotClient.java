package com.example.binslot;

import com.example.binslot.event.ClientEventHandler;
import com.example.binslot.network.ClientNetworkManager;
import net.fabricmc.api.ClientModInitializer;

public class BinSlotClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientEventHandler.init();

		ClientNetworkManager.init();
	}
}
