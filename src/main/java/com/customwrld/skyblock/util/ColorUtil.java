package com.customwrld.skyblock.util;

import java.awt.*;

public class ColorUtil {

    public static Color latestRGB() {
        return Color.getHSBColor((float) Math.sin(System.currentTimeMillis()/12D),1,1);
    }
    public static Color getHue(double value) {
        return Color.getHSBColor((float) Math.sin(value),1,1);
    }
}
