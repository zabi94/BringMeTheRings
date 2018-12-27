package zabi.minecraft.bmtr.proxy;

import org.lwjgl.util.Point;

import baubles.client.gui.GuiPlayerExpanded;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.bmtr.BringMeTheRings;
import zabi.minecraft.bmtr.PositionHelper;

public class ClientProxy extends CommonProxy {
	
	private static final ResourceLocation texture = new ResourceLocation(BringMeTheRings.ID, "textures/gui/bar.png");
	
	@Override
	public void init() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onGui(GuiScreenEvent.BackgroundDrawnEvent evt) {
		if (evt.getGui() instanceof GuiPlayerExpanded) {
			evt.getGui().mc.getTextureManager().bindTexture(texture);
			ScaledResolution sr = new ScaledResolution(evt.getGui().mc);
			int startX = (sr.getScaledWidth() - 176) / 2;
			int startY = (sr.getScaledHeight() - 166) / 2;
			evt.getGui().drawTexturedModalRect(startX, startY - 6 - PositionHelper.rows*18, 0, 0, 6, 6);
			evt.getGui().drawTexturedModalRect(startX + 18 * PositionHelper.cols + 6, startY - 6 - PositionHelper.rows*18, 58, 0, 6, 6);
			for (int i = 0; i < PositionHelper.cols; i++) {
				evt.getGui().drawTexturedModalRect(6 + startX + i*18, startY - 6 - PositionHelper.rows*18, 4, 0, 18, 6);
				for (int j = 0; j < PositionHelper.rows; j++) {
					evt.getGui().drawTexturedModalRect(6 + startX + i*18, startY - (1+j)*18, 6, 6, 18, 18);
				}
			}
			for (int j = 0; j < PositionHelper.rows; j++) {
				evt.getGui().drawTexturedModalRect(startX, startY - (1+j)*18, 0, 6, 6, 18);
				evt.getGui().drawTexturedModalRect(startX + 18 * PositionHelper.cols + 6, startY - (1+j)*18, 58, 6, 6, 30);
			}
			for (Point p:PositionHelper.slotCoords) {
				evt.getGui().drawTexturedModalRect(startX + p.getX() -1 , startY + p.getY() - 1, 0, 46, 18, 18);
			}
		}
	}
}
