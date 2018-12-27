package zabi.minecraft.bmtr.core;

import baubles.api.BaublesApi;
import baubles.common.container.ContainerPlayerExpanded;
import baubles.common.container.SlotBauble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import zabi.minecraft.bmtr.ModConfig;
import zabi.minecraft.bmtr.PositionHelper;

public class Snippets {
	public static void addSlotsToContainer(ContainerPlayerExpanded c, Object p) {
		if (!(p instanceof EntityPlayer)) {
			throw new ASMException("Not an EntityPlayer: "+p);
		}
		EntityPlayer ep = (EntityPlayer) p;
//		int slots = BaublesApi.getBaublesHandler(ep).getSlots();
//		
//		System.out.println("there are "+slots+" slots");
		
		for (int i = 7; i < 7 + ModConfig.getExtraSlots(); i++) {
			SlotBauble sb = new SlotBauble(ep, BaublesApi.getBaublesHandler(ep), i, PositionHelper.slotCoords.get(i-7).getX(), PositionHelper.slotCoords.get(i-7).getY());
			sb.slotNumber = c.inventorySlots.size();
			c.inventorySlots.add(sb);
	        c.inventoryItemStacks.add(new ItemStack(Items.BLAZE_POWDER));
		}
	}
	
	public static void printBaublesSize(EntityPlayer p) {
		System.out.println(BaublesApi.getBaublesHandler(p).getSlots());
	}
}
