package com.test.dto;

import com.google.common.collect.Lists;
import com.test.consts.Direction;
import com.test.consts.GameConsts;
import com.test.service.Audio;
import com.test.service.Draw;
import com.test.util.GameUtil;
import com.test.util.PoolUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static com.test.consts.GameConsts.BLOCK_SIZE;
import static com.test.consts.GameConsts.TANK_STEP;

public class TanKe implements Draw {
    private BufferedImage parentImg;
    private List<BufferedImage> upImg;
    private List<BufferedImage> downImg;
    private List<BufferedImage> leftImg;
    private List<BufferedImage> rightImg;
    private int currentIndex = 0;
    private Direction direction;
    private int x;
    private int y;
    private int width = 26;
    private int height = 26;
    private boolean die;
    private BigExplode bigExplode;
    public TanKe(BufferedImage parentImg, int x, int y, int imageX, int imageY) {
        direction = Direction.UP;
        this.x = x;
        this.y = y;
        this.parentImg = parentImg;
        upImg = Lists.newArrayList();
        upImg.add(splitImg(parentImg, imageX, imageY));
        upImg.add(splitImg(parentImg, imageX + width + 8, imageY));
        rightImg = Lists.newArrayList();
        rightImg.add(splitImg(parentImg, imageX + width * 2 + 8 * 2, imageY));
        rightImg.add(splitImg(parentImg, imageX + width * 3 + 8 * 3, imageY));
        downImg = Lists.newArrayList();
        downImg.add(splitImg(parentImg, imageX + width * 4 + 8 * 4, imageY));
        downImg.add(splitImg(parentImg, imageX + width * 5 + 8 * 5, imageY));
        leftImg = Lists.newArrayList();
        leftImg.add(splitImg(parentImg, imageX + width * 6 + 8 * 6, imageY));
        leftImg.add(splitImg(parentImg, imageX + width * 7 + 8 * 7, imageY));
    }

    public synchronized void move(Direction direction) {
        if(this.isDie()){
            return;
        }
        this.direction = direction;
        if (direction.equals(Direction.UP)) {
            y = y - TANK_STEP;
            if (y <= 0) {
                y = 0;
            }
        } else if (direction.equals(Direction.DOWN)) {
            y = y + TANK_STEP;
            if (y >= GameConsts.PANEL_HEIGHT) {
                y = GameConsts.PANEL_HEIGHT - BLOCK_SIZE;
            }
        } else if (direction.equals(Direction.LEFT)) {
            x = x - TANK_STEP;
            if (x <= 0) {
                x = 0;
            }
        } else if (direction.equals(Direction.RIGHT)) {
            x = x + TANK_STEP;
            if (x >= GameConsts.PANEL_WIDTH) {
                x = GameConsts.PANEL_WIDTH - BLOCK_SIZE;
            }
        }
        ++currentIndex;
        int maxIndex = getImagesByDir(direction).size() - 1;
        if (currentIndex < 0) {
            currentIndex = maxIndex;
        }
        if (currentIndex > maxIndex) {
            currentIndex = 0;
        }
    }

    public List<BufferedImage> getImagesByDir(Direction direction) {
        List<BufferedImage> images = Lists.newArrayList();
        if (direction.equals(Direction.UP)) {
            images = upImg;
        } else if (direction.equals(Direction.DOWN)) {
            images = downImg;
        } else if (direction.equals(Direction.LEFT)) {
            images = leftImg;

        } else if (direction.equals(Direction.RIGHT)) {
            images = rightImg;
        }
        return images;
    }

    public BufferedImage currentImg() {
        List<BufferedImage> imagesByDir = getImagesByDir(direction);
        int maxIndex = imagesByDir.size() - 1;
        if (currentIndex < 0) {
            currentIndex = maxIndex;
        }
        if (currentIndex > maxIndex) {
            currentIndex = 0;
        }
        return imagesByDir.get(currentIndex);
    }

    private BufferedImage splitImg(BufferedImage parentImg, int x, int y) {
        return GameUtil.splitImg(parentImg, x, y, BLOCK_SIZE, BLOCK_SIZE);
    }

    @Override
    public void drawMe(Graphics g) {
        if (!this.isDie()) {
            g.drawImage(currentImg(), x, y, null);
        } else {
            BigExplode bigExplode = this.getBigExplode();
            if (bigExplode != null) {
                bigExplode.drawMe(g);
            }
        }
    }


    /**
     * 自己移动
     *
     * @param direction
     */
    public synchronized void keepMove(Direction direction) {
        if(this.isDie()){
            return;
        }
        this.direction = direction;
        ++currentIndex;
        int maxIndex = getImagesByDir(direction).size() - 1;
        if (currentIndex < 0) {
            currentIndex = maxIndex;
        }
        if (currentIndex > maxIndex) {
            currentIndex = 0;
        }
        if (direction.equals(Direction.UP)) {
            y = y - TANK_STEP;
            if (y <= 0) {
                y = 0;
                keepMove(Direction.random());
            }
        } else if (direction.equals(Direction.DOWN)) {
            y = y + TANK_STEP;
            if (y >= GameConsts.PANEL_HEIGHT) {
                y = GameConsts.PANEL_HEIGHT - BLOCK_SIZE;
                keepMove(Direction.random());
            }
        } else if (direction.equals(Direction.LEFT)) {
            x = x - TANK_STEP;
            if (x <= 0) {
                x = 0;
                keepMove(Direction.random());
            }
        } else if (direction.equals(Direction.RIGHT)) {
            x = x + TANK_STEP;
            if (x >= GameConsts.PANEL_WIDTH) {
                x = GameConsts.PANEL_WIDTH - BLOCK_SIZE;
                keepMove(Direction.random());
            }
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public void die() {
        if(this.isDie()){
            return;
        }
        this.die = true;
        List<Explode> explode = Explode.explode(this.getX(), this.getY(), parentImg);
        bigExplode = new BigExplode(explode);
        PoolUtil.submit(new Audio("audio/kill.wav")::play);
        PoolUtil.delay(()->{
            bigExplode = null;
        },1000);
    }

    public BufferedImage getParentImg() {
        return parentImg;
    }

    public void setParentImg(BufferedImage parentImg) {
        this.parentImg = parentImg;
    }

    public List<BufferedImage> getUpImg() {
        return upImg;
    }

    public void setUpImg(List<BufferedImage> upImg) {
        this.upImg = upImg;
    }

    public List<BufferedImage> getDownImg() {
        return downImg;
    }

    public void setDownImg(List<BufferedImage> downImg) {
        this.downImg = downImg;
    }

    public List<BufferedImage> getLeftImg() {
        return leftImg;
    }

    public void setLeftImg(List<BufferedImage> leftImg) {
        this.leftImg = leftImg;
    }

    public List<BufferedImage> getRightImg() {
        return rightImg;
    }

    public void setRightImg(List<BufferedImage> rightImg) {
        this.rightImg = rightImg;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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

    public boolean isDie() {
        return die;
    }

    public void setDie(boolean die) {
        this.die = die;
    }

    public BigExplode getBigExplode() {
        return bigExplode;
    }

    public void setBigExplode(BigExplode bigExplode) {
        this.bigExplode = bigExplode;
    }


}
