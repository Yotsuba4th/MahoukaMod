package de.yotsuba.mahouka.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.cad.CadManager;
import de.yotsuba.mahouka.magic.cast.CastingManager;
import de.yotsuba.mahouka.magic.cast.CastingProcess;

public class CadItemRenderer implements IItemRenderer
{

    private IModelCustom model;

    private ResourceLocation texture;

    private ResourceLocation textureCast;

    public CadItemRenderer()
    {
        model = AdvancedModelLoader.loadModel(new ResourceLocation(MahoukaMod.MODID.toLowerCase(), "models/SilverHornTrident.obj"));
        texture = new ResourceLocation(MahoukaMod.MODID.toLowerCase(), "textures/models/SilverHornTrident.png");
        textureCast = new ResourceLocation(MahoukaMod.MODID.toLowerCase(), "textures/models/CastEffect.png");
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        switch (type)
        {
        case EQUIPPED:
        case EQUIPPED_FIRST_PERSON:
            return true;
        case ENTITY:
            return true;
        case INVENTORY:
            return false;
        case FIRST_PERSON_MAP:
        default:
            return false;
        }
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        switch (helper)
        {
        case ENTITY_ROTATION:
        case ENTITY_BOBBING:
            return true;
        case BLOCK_3D:
        case EQUIPPED_BLOCK:
        case INVENTORY_BLOCK:
        default:
            return false;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
    {
        GL11.glPushMatrix();

        if (type == ItemRenderType.EQUIPPED)
        {
            GL11.glRotatef(35, 0, 0, 1);
            GL11.glTranslatef(1.06f, 0.07f, 0);
            GL11.glScalef(0.8f, 0.8f, 0.8f);
        }
        else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
            GL11.glRotatef(35, 0, 0, 1);
            GL11.glTranslatef(1.2f, 0.0f, -0.2f);
            // TODO (3) For some damn reason, the model gets flipped in 1st person!
            // Because of that we need to flip it again and reverse the cull face
            GL11.glScalef(1, 1, -1);
            GL11.glCullFace(GL11.GL_FRONT);
        }
        else if (type == ItemRenderType.INVENTORY)
        {
            GL11.glTranslatef(8, 8, 0);
            GL11.glScalef(-13, -13, 13);
            GL11.glRotatef(30, 0, 0, 1);
        }
        else if (type == ItemRenderType.ENTITY)
        {
            // EntityItem item = (EntityItem) data[1];
            GL11.glRotatef(-25, 0, 0, 1);
            GL11.glScalef(1.5f, 1.5f, 1.5f);
            GL11.glTranslatef(0, 0.2f, 0);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        model.renderOnly("CAD");

        CadBase cad = CadManager.getCad(stack);
        if (cad != null)
        {
            CastingProcess cast = CastingManager.getClientCast(cad.getId());
            if (cast != null && cast.isActive())
            {
                float alpha = (float) cast.getTotalTime() / (cast.getChannelTime() + 20);
                alpha = (alpha < 0.2) ? alpha / 0.2f : ((alpha > 0.8) ? (1 - alpha) / 0.2f : 1);
                if (alpha > 0)
                {
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    GL11.glEnable(GL11.GL_BLEND);
                    Minecraft.getMinecraft().renderEngine.bindTexture(textureCast);
                    GL11.glColor4f(1, 1, 1, alpha);

                    GL11.glPushMatrix();
                    GL11.glRotatef(System.currentTimeMillis() % 3600 / 10f, 1, 0, 0);
                    model.renderOnly("fx");
                    GL11.glPopMatrix();

                    GL11.glRotatef(-System.currentTimeMillis() % 3600 / 10f, 1, 0, 0);
                    GL11.glTranslatef(-0.15f, 0, 0);
                    model.renderOnly("fx");

                    GL11.glEnable(GL11.GL_CULL_FACE);
                    GL11.glDisable(GL11.GL_BLEND);
                }
            }
        }

        GL11.glCullFace(GL11.GL_BACK);
        GL11.glPopMatrix();
    }

}
