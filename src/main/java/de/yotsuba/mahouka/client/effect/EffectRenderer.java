package de.yotsuba.mahouka.client.effect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Callable;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class EffectRenderer
{
    private static World world;

    private static Map<UUID, List<Effect>> fxMap = new HashMap<UUID, List<Effect>>();

    public static void addEffect(Effect entityFx, UUID uuid)
    {
        List<Effect> list = fxMap.get(uuid);
        if (list == null)
        {
            list = new ArrayList<Effect>();
        }
        list.add(entityFx);
    }

    public static void removeEffects()
    {
        for (Iterator<Entry<UUID, List<Effect>>> itList = fxMap.entrySet().iterator(); itList.hasNext();)
        {
            Entry<UUID, List<Effect>> effectList = itList.next();
            for (Iterator<Effect> itEffect = effectList.getValue().iterator(); itEffect.hasNext();)
            {
                Effect effect = itEffect.next();
                if (effect.isDead())
                {
                    itEffect.remove();
                }
            }
            if (effectList.getValue().isEmpty())
                itList.remove();
        }

    }

    public static void updateEffects()
    {
        for (Iterator<Entry<UUID, List<Effect>>> itList = fxMap.entrySet().iterator(); itList.hasNext();)
        {
            Entry<UUID, List<Effect>> effectList = itList.next();
            for (Iterator<Effect> itEffect = effectList.getValue().iterator(); itEffect.hasNext();)
            {
                Effect effect = itEffect.next();
                try
                {
                    effect.update();
                }
                catch (Throwable throwable)
                {
                    crashReport("Tick", effect, throwable);
                }
            }
        }
        removeEffects();
    }

    private static void crashReport(String action, final Effect entityfx, Throwable throwable)
    {
        CrashReport crashreport = CrashReport.makeCrashReport(throwable, action + "ing Particle");
        CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being " + action.toLowerCase() + "ed");
        crashreportcategory.addCrashSectionCallable("Magic Particle", new Callable() {
            @Override
            public String call()
            {
                return entityfx.toString();
            }
        });
        throw new ReportedException(crashreport);
    }

    /**
     * Renders all current particles. Args player, partialTickTime
     */
    public static void renderParticles(Entity player, float partialTickTime)
    {
        if (player.worldObj != world)
            clearEffects(player.worldObj);

        float rotX = ActiveRenderInfo.rotationX;
        float rotZ = ActiveRenderInfo.rotationZ;
        float rotYZ = ActiveRenderInfo.rotationYZ;
        float rotXY = ActiveRenderInfo.rotationXY;
        float rotXZ = ActiveRenderInfo.rotationXZ;
        double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTickTime;
        double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTickTime;
        double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTickTime;

        GL11.glPushMatrix();
        GL11.glTranslated(interpPosX, interpPosY, interpPosZ);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glAlphaFunc(GL11.GL_GREATER, 1 / 255f);

        for (Iterator<Entry<UUID, List<Effect>>> itList = fxMap.entrySet().iterator(); itList.hasNext();)
        {
            Entry<UUID, List<Effect>> effectList = itList.next();
            for (Iterator<Effect> itEffect = effectList.getValue().iterator(); itEffect.hasNext();)
            {
                Effect effect = itEffect.next();
                try
                {
                    effect.renderParticle(partialTickTime);
                }
                catch (Throwable throwable)
                {
                    crashReport("Render", effect, throwable);
                }
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        GL11.glPopMatrix();
    }

    public static void clearEffects(World world)
    {
        EffectRenderer.world = world;
        fxMap.clear();
    }

    public static int getEffectsCount()
    {
        int effectsCount = 0;
        for (Iterator<Entry<UUID, List<Effect>>> itList = fxMap.entrySet().iterator(); itList.hasNext();)
        {
            Entry<UUID, List<Effect>> effectList = itList.next();
            effectsCount += effectList.getValue().size();
        }
        return effectsCount;
    }

}