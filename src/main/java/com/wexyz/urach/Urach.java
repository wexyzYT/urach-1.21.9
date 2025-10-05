package com.wexyz.urach;

import com.wexyz.urach.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Urach implements ModInitializer {
	public static final String MOD_ID = "urach";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

        ModItems.registerModItems();

		LOGGER.info("Hello Fabric world!");
	}
}