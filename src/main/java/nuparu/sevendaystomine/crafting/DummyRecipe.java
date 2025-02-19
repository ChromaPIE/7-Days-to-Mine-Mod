package nuparu.sevendaystomine.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class DummyRecipe extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    private final ItemStack output;

    public DummyRecipe(ItemStack output)
    {
        this.output = output;
    }

    public static IRecipe from(IRecipe other)
    {
        return new DummyRecipe(other.getRecipeOutput()).setRegistryName(other.getRegistryName());
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return output;
    }

}