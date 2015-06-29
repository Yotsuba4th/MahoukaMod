package de.yotsuba.mahouka.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.gui.CadProgrammerGui;

public class BlockCadProgrammer extends Block
{

    public static final BlockCadProgrammer BLOCK = new BlockCadProgrammer(Material.iron);

    public static final String ID = "cad_programmer";

    protected BlockCadProgrammer(Material material)
    {
        super(material);
        setHardness(2.0F);
        setResistance(10.0F);
        setLightLevel(0.0F);
        setBlockName(ID);
        setBlockTextureName(MahoukaMod.MODID + ":" + ID);
        setCreativeTab(MahoukaMod.creativeTab);
        setLightOpacity(0);
        setStepSound(Block.soundTypeMetal);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon(getTextureName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int front)
    {
        return blockIcon;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entity, int l, float m, float n, float o)
    {
        entity.openGui(MahoukaMod.instance, CadProgrammerGui.GUIID, world, x, y, z);
        return true;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entity, itemStack);
        world.setBlockMetadataWithNotify(x, y, z, getRotationFromEntity(entity), 2);
    }

    private int getRotationFromEntity(EntityLivingBase placer)
    {
        switch (MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3)
        {
        case 0:
            return ForgeDirection.NORTH.ordinal();
        case 1:
            return ForgeDirection.EAST.ordinal();
        case 2:
            return ForgeDirection.SOUTH.ordinal();
        case 3:
            return ForgeDirection.WEST.ordinal();
        default:
            return ForgeDirection.UNKNOWN.ordinal();
        }
    }

}
