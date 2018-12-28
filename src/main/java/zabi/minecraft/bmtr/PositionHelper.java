package zabi.minecraft.bmtr;

import java.util.ArrayList;

public class PositionHelper {
	
	public static ArrayList<Point> slotCoords = new ArrayList<>();
	
	public static int rows = 0, cols = 0;

	@SuppressWarnings("unused")
	public static void setupSlots() {
		slotCoords.clear();
		rows = (int) Math.ceil(ModConfig.getExtraSlots() / 9d);
		cols = Math.min(9, ModConfig.getExtraSlots());
		
		for (int i = 0; i < ModConfig.getExtraSlots(); i++) {
			slotCoords.add(new Point(6 + 18*(i % 9), -18*(1+(i / 9))));
		}
	}
	

}
