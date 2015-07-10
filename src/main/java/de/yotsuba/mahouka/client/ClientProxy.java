package de.yotsuba.mahouka.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import de.yotsuba.mahouka.CommonProxy;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.client.effect.EffectRenderer;
import de.yotsuba.mahouka.client.render.CadItemRenderer;
import de.yotsuba.mahouka.client.render.RenderCrystal;
import de.yotsuba.mahouka.client.render.RenderFireball;
import de.yotsuba.mahouka.core.PlayerData;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileEarth;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileFire;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileIce;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.cad.CadManager;
import de.yotsuba.mahouka.util.RenderUtils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetArea;
import de.yotsuba.mahouka.util.target.TargetBlock;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetOffset;

public class ClientProxy extends CommonProxy
{

    @Override
    public void init(FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);

        RenderingRegistry.registerEntityRenderingHandler(EntityMagicProjectileFire.class, new RenderFireball(0.5f));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicProjectileIce.class, new RenderCrystal(0.2f, 0.3f, 1f, 0.4f, 0.4f, 2));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicProjectileEarth.class, new RenderCrystal(0.5f, 0.36f, 0.05f, 1, 0.5f, 0.5f));
        MinecraftForgeClient.registerItemRenderer(MahoukaMod.cad, new CadItemRenderer());
    }

    /* ------------------------------------------------------------ */

    @SubscribeEvent
    public void clientTickEvent(ClientTickEvent event)
    {
        if (event.phase != Phase.END || Minecraft.getMinecraft().isGamePaused())
            return;
        EffectRenderer.updateEffects();
    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event)
    {
        EffectRenderer.renderParticles(Minecraft.getMinecraft().thePlayer, event.partialTicks);
        renderTargetInfo();
    }

    @SubscribeEvent
    public void renderGameOverlayEvent(RenderGameOverlayEvent event)
    {
        PlayerData playerData = new PlayerData(Minecraft.getMinecraft().thePlayer);
        renderPsionBar(event, playerData);
    }

    public static void renderPsionBar(RenderGameOverlayEvent event, PlayerData playerData)
    {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        ItemStack equipped = player.getCurrentEquippedItem();
        if (equipped == null || !(equipped.getItem() instanceof ItemCad))
            return;
        CadBase cad = CadManager.getCad(equipped);
        int castCost = (cad != null && cad.getSelectedSequence() != null) ? cad.getSelectedSequence().getPsionCost() : 0;
        int maxPsion = playerData.getMaxPsion();
        int psion = playerData.getPsion();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(3);

        float top = event.resolution.getScaledHeight() - (player.capabilities.isCreativeMode ? 24 : 42);
        float left = event.resolution.getScaledWidth() / 2 - 91;
        float barWidth = 182;
        float ptsPerPsion = barWidth / maxPsion;

        GL11.glBegin(GL11.GL_LINES);

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glVertex2f(left + ptsPerPsion * 0, top);
        GL11.glVertex2f(left + ptsPerPsion * maxPsion, top);

        GL11.glColor4f(0.0f, 0.1f, 0.9f, 1);
        GL11.glVertex2f(left + ptsPerPsion * 0, top);
        GL11.glVertex2f(left + ptsPerPsion * psion, top);

        GL11.glColor4f(1, 0, 0, 1);
        GL11.glVertex2f(left + Math.max(0, ptsPerPsion * (psion - castCost)), top);
        GL11.glVertex2f(left + ptsPerPsion * psion, top);

        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void renderTargetInfo()
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null || player.isSneaking())
            return;

        ItemStack stack = player.getCurrentEquippedItem();
        if (stack == null || !(stack.getItem() instanceof ItemCad))
            return;

        CadBase cad = CadManager.getCad(stack);
        if (cad == null || cad.getSelectedSequence() == null)
            return;

        MagicProcess sequence = cad.getSelectedSequence();
        if (sequence == null)
            return;

        Target target = cad.selectTarget(player);
        if (target == null || !sequence.isTargetValid(target))
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

            GL11.glTranslated(entity.posX, entity.posY, entity.posZ);
            if (aabb != null)
                GL11.glScaled((aabb.maxX - aabb.minX) * 1.25 + 0.25, (aabb.maxY - aabb.minY) * 1.25, (aabb.maxZ - aabb.minZ) * 1.25 + 0.25);
            GL11.glTranslated(0, 0.502, 0);
        }
        else if (target instanceof TargetOffset)
        {
            // TODO (4) Target CUI for offset targets
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
        RenderUtils.renderLineBox();

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

}
