package com.test.dto;

import com.test.service.Draw;
import com.test.util.GameUtil;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.List;

@Data
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
}
