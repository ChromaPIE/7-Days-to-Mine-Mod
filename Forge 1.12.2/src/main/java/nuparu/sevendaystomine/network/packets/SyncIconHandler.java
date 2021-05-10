package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.computer.process.WindowsDesktopProcess.IconPosUpdate;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class SyncIconHandler implements IMessageHandler<SyncIconMessage, SyncIconMessage> {

	@Override
	public SyncIconMessage onMessage(SyncIconMessage message, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().player;
		World world = player.world;

		BlockPos pos = message.getPos();
		NBTTagCompound nbt = message.getNBT();
		
		IconPosUpdate update = new IconPosUpdate();
		update.readFromNBT(nbt);

		TileEntity TE = world.getTileEntity(pos);

		if (TE != null && TE instanceof TileEntityComputer) {
			TileEntityComputer computerTE = (TileEntityComputer) TE;
			if (computerTE.getMonitorTE() != null && computerTE.getMonitorTE().getLookingPlayers().contains(player)) {
				computerTE.getHardDrive().setIconPostion(update.x, update.y, update.app);
			}
		}

		return null;
	}

}
