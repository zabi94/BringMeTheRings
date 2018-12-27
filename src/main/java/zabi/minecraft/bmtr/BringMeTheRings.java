package zabi.minecraft.bmtr;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import zabi.minecraft.bmtr.proxy.CommonProxy;

@Mod(name = BringMeTheRings.NAME, modid = BringMeTheRings.ID, version = BringMeTheRings.VERSION, dependencies=BringMeTheRings.DEPS)
public class BringMeTheRings {
	public static final String ID = "bmtr";
	public static final String NAME = "Bring me the rings";
	public static final String VERSION = "0.3";
	public static final String DEPS = "required-after:baubles";
	
	@Instance
	public static BringMeTheRings mod;
	
	@SidedProxy(clientSide = "zabi.minecraft.bmtr.proxy.ClientProxy", serverSide = "zabi.minecraft.bmtr.proxy.ServerProxy")
	public static CommonProxy proxy = null;
	
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		proxy.init();
	}
}