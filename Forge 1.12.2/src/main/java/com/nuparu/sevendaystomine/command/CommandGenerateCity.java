package com.nuparu.sevendaystomine.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.block.repair.BreakData;
import com.nuparu.sevendaystomine.block.repair.BreakSavedData;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.City;
import com.nuparu.sevendaystomine.world.gen.city.EnumCityType;
import com.nuparu.sevendaystomine.world.gen.city.building.Building;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingLargeBanditCamp;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class CommandGenerateCity extends CommandBase {
	@SuppressWarnings("rawtypes")
	private final List aliases;

	protected String fullEntityName;
	protected Entity conjuredEntity;

	@SuppressWarnings("rawtypes")
	public CommandGenerateCity() {
		aliases = new ArrayList();
	}

	@Override
	public String getName() {
		return "generatecity";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/generatecity <x> <y> <z> [metropolis : urban : rural : village]";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getAliases() {
		return this.aliases;
	}

	public int getRequiredPermissionLevel() {
		return 4;
	}
	
	@Override
	public void execute(MinecraftServer server, final ICommandSender sender, String[] args) throws CommandException {
		final World world = sender.getEntityWorld();

		if (world.isRemote) {
		} else {
			EnumCityType type = EnumCityType.CITY;
			if (args.length == 4) {
				type = EnumCityType.getByName(args[3]);
			} else if (args.length != 4)
				return;

			if (type == null)
				return;
			BlockPos blockPos = parseBlockPos(sender, args, 0, true);
			City city = new City(world, blockPos, type, new Random());
			city.startCityGen();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		return args.length > 0 && args.length <= 3 ? getTabCompletionCoordinate(args, 0, pos)
				: args.length == 4
						? CommandBase.getListOfStringsMatchingLastWord(args, "village", "town", "city", "metropolis")
						: null;
	}
}