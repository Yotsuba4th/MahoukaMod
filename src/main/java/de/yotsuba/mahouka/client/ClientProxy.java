package de.yotsuba.mahouka.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import de.yotsuba.mahouka.CommonProxy;
import de.yotsuba.mahouka.client.effect.EffectRenderer;
import de.yotsuba.mahouka.client.render.RenderCrystal;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileEarth;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileIce;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.cad.CadManager;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetArea;
import de.yotsuba.mahouka.util.target.TargetBlock;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetOffset;

public class ClientProxy extends CommonProxy
{

    @Override
    public void castStartClient(MagicProcess process, CastingProcess cp, Target target)
    {
        process.castStartClient(cp, target);
    }

    @Override
    public void castTickClient(MagicProcess process, CastingProcess cp, Target target)
    {
        process.castTickClient(cp, target);
    }

    @Override
    public void castEndClient(MagicProcess process, CastingProcess cp, Target target)
    {
        process.castEndClient(cp, target);
    }

    /* ------------------------------------------------------------ */

    @Override
    public void init(FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);

        // RenderingRegistry.registerEntityRenderingHandler(EntityMagicProjectileFire.class, new RenderFireball(0.5f));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicProjectileIce.class, new RenderCrystal(0.2f, 0.3f, 1f, 0.4f, 0.4f, 2));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicProjectileEarth.class, new RenderCrystal(0.5f, 0.36f, 0.05f, 1, 0.5f, 0.5f));
    }

    /* ------------------------------------------------------------ */

    @SubscribeEvent
    public void clientTickEvent(ClientTickEvent event)
    {
        if (event.phase == Phase.END)
        {
            EffectRenderer.updateEffects();
        }
    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event)
    {
        renderTargetCUI();

        EffectRenderer.renderParticles(Minecraft.getMinecraft().thePlayer, event.partialTicks);
    }

    private void renderTargetCUI()
    {
        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
        if (player == null)
            return;

        ItemStack stack = player.getCurrentEquippedItem();
        if (stack == null || !(stack.getItem() instanceof ItemCad))
            return;

        CadBase cad = CadManager.getCad(stack);
        if (cad == null || cad.getSelectedSequence() == null)
            return;

        Target target = cad.selectTarget(player);
        if (target == null)
            return;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 0, 0, 0.4f);
        GL11.glLineWidth(2);

        GL11.glPushMatrix();
        GL11.glTranslated(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ);
        if (target instanceof TargetEntity)
        {
            TargetEntity targetEntity = (TargetEntity) target;
            Entity entity = targetEntity.getEntity();
            AxisAlignedBB aabb = entity.boundingBox;
            Vec3 point = target.getCurrentPoint();

            GL11.glTranslated(point.xCoord, point.yCoord, point.zCoord);
            if (aabb != null)
                GL11.glScaled((aabb.maxX - aabb.minX) * 1.25 + 0.25, (aabb.maxY - aabb.minY) * 1.25, (aabb.maxZ - aabb.minZ) * 1.25 + 0.25);
            GL11.glTranslated(0, 0.5, 0);
        }
        else if (target instanceof TargetOffset)
        {
            // TODO
        }
        else if (target instanceof TargetArea)
        {
            TargetArea targetArea = (TargetArea) target;
            Vec3 center = targetArea.getCenter();
            Vec3 size = targetArea.getSize();
            GL11.glTranslated(center.xCoord, center.yCoord, center.zCoord);
            GL11.glScaled(size.xCoord, size.yCoord, size.zCoord);
        }
        else if (target instanceof TargetBlock)
        {
            TargetBlock targetBlock = (TargetBlock) target;
            GL11.glTranslated(targetBlock.getX() + 0.5, targetBlock.getY() + 0.5, targetBlock.getZ() + 0.5);
        }
        else
        {
            GL11.glScaled(0, 0, 0);
        }
        renderBox();

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private static void renderBox()
    {
        Tessellator.instance.startDrawing(GL11.GL_LINES);

        // FRONT
        Tessellator.instance.addVertex(-0.5, -0.5, -0.5);
        Tessellator.instance.addVertex(-0.5, 0.5, -0.5);

        Tessellator.instance.addVertex(-0.5, 0.5, -0.5);
        Tessellator.instance.addVertex(0.5, 0.5, -0.5);

        Tessellator.instance.addVertex(0.5, 0.5, -0.5);
        Tessellator.instance.addVertex(0.5, -0.5, -0.5);

        Tessellator.instance.addVertex(0.5, -0.5, -0.5);
        Tessellator.instance.addVertex(-0.5, -0.5, -0.5);

        // BACK
        Tessellator.instance.addVertex(-0.5, -0.5, 0.5);
        Tessellator.instance.addVertex(-0.5, 0.5, 0.5);

        Tessellator.instance.addVertex(-0.5, 0.5, 0.5);
        Tessellator.instance.addVertex(0.5, 0.5, 0.5);

        Tessellator.instance.addVertex(0.5, 0.5, 0.5);
        Tessellator.instance.addVertex(0.5, -0.5, 0.5);

        Tessellator.instance.addVertex(0.5, -0.5, 0.5);
        Tessellator.instance.addVertex(-0.5, -0.5, 0.5);

        // betweens.
        Tessellator.instance.addVertex(0.5, 0.5, -0.5);
        Tessellator.instance.addVertex(0.5, 0.5, 0.5);

        Tessellator.instance.addVertex(0.5, -0.5, -0.5);
        Tessellator.instance.addVertex(0.5, -0.5, 0.5);

        Tessellator.instance.addVertex(-0.5, -0.5, -0.5);
        Tessellator.instance.addVertex(-0.5, -0.5, 0.5);

        Tessellator.instance.addVertex(-0.5, 0.5, -0.5);
        Tessellator.instance.addVertex(-0.5, 0.5, 0.5);

        Tessellator.instance.draw();
    }

}
