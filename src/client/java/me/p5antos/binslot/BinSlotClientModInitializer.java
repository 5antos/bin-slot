package me.p5antos.binslot;

import me.p5antos.binslot.event.ClientEventHandler;
import me.p5antos.binslot.network.ClientNetworkManager;
import net.fabricmc.api.ClientModInitializer;

public class BinSlotClientModInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientEventHandler.init();

		ClientNetworkManager.init();
	}
}
