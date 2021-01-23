package com.nuparu.sevendaystomine.loot.function;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.IQuality;
import com.nuparu.sevendaystomine.item.ItemFuelTool;
import com.nuparu.sevendaystomine.item.ItemGun;
import com.nuparu.sevendaystomine.item.ItemQuality;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class CityMapFunction extends LootFunction {

	protected CityMapFunction(LootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {	
		WorldServer world = context.getWorld();

		List<EntityPlayer> players = world.playerEntities;
		BlockPos pos;
		if(players.size()==0){
		pos = world.getSpawnPoint();
		}
		else {
		pos = new BlockPos(players.get(rand.nextInt(players.size())));
		}
		List<ChunkPos> cities = Utils.getClosestCities(context.getWorld(),pos.getX()>>4,pos.getZ()>>4,256);
		if(cities.size()==0) return new ItemStack(Items.MAP);
		
		ChunkPos city = cities.get(rand.nextInt(cities.size()));
		ItemStack stack2 = ItemMap.setupNewMap(context.getWorld(), city.x*16+8, city.z*16+8, (byte) 1, true, true);
		ItemMap.renderBiomePreviewMap(world, stack2);
		stack2.setTranslatableName("filled_map.city");
		MapData.addTargetDecoration(stack2, city.getBlock(8, 128, 8), "+", MapDecoration.Type.MANSION);
		Utils.renderBiomePreviewMap(context.getWorld(), stack2);

		return stack2;
	}

	public static class Serializer extends LootFunction.Serializer<CityMapFunction> {
		public Serializer() {
			super(new ResourceLocation(SevenDaysToMine.MODID, "city_map"), CityMapFunction.class);
		}

		public void serialize(JsonObject object, CityMapFunction functionClazz,
				JsonSerializationContext serializationContext) {

		}

		public CityMapFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			return new CityMapFunction(conditionsIn);
		}
	}

}
