package de.yotsuba.mahouka.util;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

public final class RenderUtils
{

    public static void renderLineBox()
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
