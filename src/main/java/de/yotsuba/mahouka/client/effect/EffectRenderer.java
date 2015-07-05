package de.yotsuba.mahouka.client.effect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Callable;

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
            fxMap.put(uuid, list);
        }
        list.add(entityFx);
    }

    public static List<Effect> getEffectList(UUID id)
    {
        return fxMap.get(id);
    }

    public static void cancelEffects(UUID id)
    {

        List<Effect> effects = fxMap.get(id);
        if (effects != null)
        {
            for (Effect effect : effects)
                effect.cancel();
        }
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
        crashreportcategory.addCrashSectionCallable("Magic Particle", new Callable<String>() {
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

        // System.out.println("yaw: " + player.rotationYaw + " pitch: " + player.rotationPitch);

        double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTickTime;
        double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTickTime;
        double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTickTime;

        GL11.glPushMatrix();
        GL11.glTranslated(-interpPosX, -interpPosY, -interpPosZ);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glAlphaFunc(GL11.GL_GREATER, 1 / 255f);

        for (Entry<UUID, List<Effect>> effectList : fxMap.entrySet())
        {
            for (Effect effect : effectList.getValue())
            {
                try
                {
                    effect.render(partialTickTime);
                }
                catch (Throwable throwable)
                {
                    crashReport("Render", effect, throwable);
                }
            }
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
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
        for (Entry<UUID, List<Effect>> effectList : fxMap.entrySet())
            effectsCount += effectList.getValue().size();
        return effectsCount;
    }

    public static boolean hasSimilarEffect(Effect fx)
    {
        for (Entry<UUID, List<Effect>> effectList : fxMap.entrySet())
        {
            for (Effect effect : effectList.getValue())
            {
                if (effect.getClass().equals(fx.getClass()) && Math.abs(effect.age - fx.age) < 2 && effect.icon == fx.icon && effect.x == fx.x
                        && effect.y == fx.y && effect.z == fx.z)
                    return true;
            }
        }
        return false;
    }
}