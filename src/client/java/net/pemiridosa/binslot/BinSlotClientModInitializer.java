package net.pemiridosa.binslot;

import net.pemiridosa.binslot.event.ClientEventHandler;
import net.pemiridosa.binslot.network.ClientNetworkManager;
import net.fabricmc.api.ClientModInitializer;

public class BinSlotClientModInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientEventHandler.init();

		ClientNetworkManager.init();
	}
}
