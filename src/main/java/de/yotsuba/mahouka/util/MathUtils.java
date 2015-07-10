package de.yotsuba.mahouka.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public final class MathUtils
{

    public static Vec3 copyVector(Vec3 vector)
    {
        return Vec3.createVectorHelper(vector.xCoord, vector.yCoord, vector.zCoord);
    }

    public static void scaleVector(Vec3 vector, double factor)
    {
        vector.xCoord *= factor;
        vector.yCoord *= factor;
        vector.zCoord *= factor;
    }

    public static void addVector(Vec3 vector, Vec3 addVector)
    {
        vector.xCoord += addVector.xCoord;
        vector.yCoord += addVector.yCoord;
        vector.zCoord += addVector.zCoord;
    }

    public static boolean equals(Vec3 a, Vec3 b)
    {
        return a.xCoord == b.xCoord && a.yCoord == b.yCoord && a.zCoord == b.zCoord;
    }

    public static Vec3 getEntityPosition(Entity entity)
    {
        return Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
    }

    public static Vec3 getEntityMotion(Entity entity)
    {
        return Vec3.createVectorHelper(entity.motionX, entity.motionY, entity.motionZ);
    }

    public static void setEntityHeading(Entity entity)
    {
        setEntityHeading(entity, Vec3.createVectorHelper(entity.posX + entity.motionX, entity.posY + entity.motionY, entity.posZ + entity.motionZ));
    }

    public static void setEntityHeading(Entity entity, Vec3 lookAt)
    {
        double xd = lookAt.xCoord - entity.posX;
        double yd = lookAt.yCoord - entity.posY;
        double zd = lookAt.zCoord - entity.posZ;
        entity.rotationYaw = (float) (Math.atan2(zd, xd) * 180.0D / Math.PI) - 90.0F;
        entity.rotationPitch = (float) (Math.atan2(Math.sqrt(xd * xd + zd * zd), yd) * 180.0D / Math.PI) - 90.0F;
    }

}
