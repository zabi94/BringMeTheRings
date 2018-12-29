package zabi.minecraft.bmtr;

import org.apache.logging.log4j.LogManager;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import zabi.minecraft.bmtr.proxy.CommonProxy;

@Mod(name = BringMeTheRings.NAME, modid = BringMeTheRings.ID, version = BringMeTheRings.VERSION, dependencies=BringMeTheRings.DEPS, certificateFingerprint=BringMeTheRings.FINGERPRINT)
public class BringMeTheRings {
	
	public static final String ID = "bmtr";
	public static final String NAME = "Bring me the rings";
	public static final String VERSION = "@VERSION@";
	public static final String DEPS = "required-after:baubles";
	public static final String FINGERPRINT = "@FINGERPRINT@";
	
	@Instance
	public static BringMeTheRings mod;
	
	@SidedProxy(clientSide = "zabi.minecraft.bmtr.proxy.ClientProxy", serverSide = "zabi.minecraft.bmtr.proxy.ServerProxy")
	public static CommonProxy proxy = null;
	
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		proxy.init();
	}
	
	@EventHandler
	public void fingerprintViolation(FMLFingerprintViolationEvent evt) {
		if (!"true".equals(System.getProperty("ignoreBMTRFingerprint"))) {
			throw new FingerprintViolationException();
		} else {
			LogManager.getLogger(ID).warn("WARNING: BMTR signature mismatch!");
			LogManager.getLogger(ID).warn("Ignoring as per launch option");
		}
	}
	
	@SuppressWarnings("serial")
	private static class FingerprintViolationException extends RuntimeException {
		public FingerprintViolationException() {
			super("\n\n!! WARNING:\n\nThe mod "+NAME+" has an invalid signature, this is likely due to someone messing with the jar without permission.\nThe execution will be stopped in order to prevent damages to your system.\n"
					+ "If you wish to continue executing, please add -DignoreBMTRFingerprint=true to your launch arguments\n\n");
		}
	}
}