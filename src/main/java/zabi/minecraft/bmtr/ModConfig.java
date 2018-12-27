package zabi.minecraft.bmtr;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ModConfig {
	
	private static int slots = 2;
	
	public static void initConfig() {
		File config = new File(new File("config"), "bringmetherings.cfg");
		Configuration cfg = new Configuration(config);
		cfg.load();
		slots = cfg.getInt("extraSlots", "general", 2, 1, 36, "How many ring slots should there be in addition to the existing 2. [WARNING:] lowering this number will *destroy* every bauble in excess and may cause unforeseen side effects.");
		if (cfg.hasChanged()) {
			cfg.save();
		}
	}
	
	public static int getExtraSlots() {
		return slots;
	}
}
