package de.yotsuba.mahouka.util;

import net.minecraft.util.Vec3;

public enum Shape
{

    CUBE, SPHERE;

    public boolean contains(Vec3 center, double size, Vec3 point)
    {
        switch (this)
        {
        case SPHERE:
            return center.distanceTo(point) < size;
        case CUBE:
            return Math.abs(center.xCoord - point.xCoord) < size && //
                    Math.abs(center.yCoord - point.yCoord) < size && //
                    Math.abs(center.zCoord - point.zCoord) < size;
        }
        return false;
    }

}