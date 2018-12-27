package zabi.minecraft.bmtr.core;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zabi.minecraft.bmtr.ModConfig;
import zabi.minecraft.bmtr.PositionHelper;

@IFMLLoadingPlugin.Name("BringMeTheRings plugin")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({"zabi.minecraft.bmtr.core"})
public class BMTRPlugin implements IFMLLoadingPlugin {
	
	private static final String[] TRANSFORMERS = {
			"zabi.minecraft.bmtr.core.BaublesTransformer"
	};

	@Override
	public String[] getASMTransformerClass() {
		return TRANSFORMERS;
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		ModConfig.initConfig();
		PositionHelper.setupSlots();
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
