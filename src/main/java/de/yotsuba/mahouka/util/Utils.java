package de.yotsuba.mahouka.util;

public class Utils
{

    public static final int TICKS_PER_SECOND = 20;

    public static int millisecondsToTicks(int ms)
    {
        return TICKS_PER_SECOND * ms / 1000;
    }

}
