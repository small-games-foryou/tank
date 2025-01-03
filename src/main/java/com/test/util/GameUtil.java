package com.test.util;

import java.awt.image.BufferedImage;


public class GameUtil {
    public static BufferedImage splitImg(BufferedImage parentImg, int x, int y,int width,int height) {
        BufferedImage upImg = new BufferedImage(width, height, parentImg.getType());
        upImg.getGraphics().drawImage(parentImg.getSubimage(x, y, width, height), 0, 0, null);
        return upImg;
    }
}
