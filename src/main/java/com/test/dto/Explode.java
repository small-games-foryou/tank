package com.test.dto;

import com.test.service.Draw;
import com.test.util.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.List;

public class Explode implements Draw {
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage img;

    public Explode(int x, int y, int width, int height, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }

    @Override
    public void drawMe(Graphics g) {
        g.drawImage(img,x,y,width,height,null);
    }
    public static List<Explode> explode(int x,int y,BufferedImage parentImg){
        Explode e1 = new Explode(x,y,22,22, GameUtil.splitImg(parentImg,686,142,22,22));
        Explode e2 = new Explode(x,y,30,28, GameUtil.splitImg(parentImg,726,139,30,28));
        Explode e3 = new Explode(x,y,32,32, GameUtil.splitImg(parentImg,749,137,32,32));
        Explode e4 = new Explode(x,y,62,58, GameUtil.splitImg(parentImg,785,141,62,58));
        Explode e5 = new Explode(x,y,64,64, GameUtil.splitImg(parentImg,852,138,64,64));
        return List.of(e1,e2,e3,e4,e5);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }
}
