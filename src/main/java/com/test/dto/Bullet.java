package com.test.dto;

import com.google.common.collect.Lists;
import com.test.consts.Direction;
import com.test.service.Draw;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

public class Bullet implements Draw {
    private BufferedImage bulletImg;
    private Direction direction;
    private int x;
    private int y;
    private int width = 14;
    private int height = 14;
    private boolean die;
    private boolean playerShot;

    public Bullet(BufferedImage parentImg, int x, int y, Direction direction, boolean playerShot) {
        this.x = x;
        this.y = y;
        this.playerShot = playerShot;
        this.direction = direction;
        bulletImg = new BufferedImage(width, height, parentImg.getType());
        bulletImg.getGraphics().drawImage(parentImg.getSubimage(180, 214, width, width), 0, 0, null);
    }

    @Override
    public void drawMe(Graphics g) {
        g.drawImage(bulletImg, x, y, width, height, null);
    }

    public void move(GamePanel gamePanel) {
        List<TanKe> tanKes = gamePanel.getEnemyList();
        TanKe player1 = gamePanel.getTanKeOne();
        TanKe player2 = gamePanel.getTanKeTwo();
        if (direction != null && !this.isDie()) {
            if (direction.equals(Direction.UP)) {
                this.setY(getY() - height);
            }
            if (direction.equals(Direction.DOWN)) {
                this.setY(getY() + height);
            }
            if (direction.equals(Direction.LEFT)) {
                this.setX(getX() - width);
            }
            if (direction.equals(Direction.RIGHT)) {
                this.setX(getX() + width);
            }
            this.setDie(GamePanel.isOutOfPanel(getX(), getY(), width, height));
            if (this.isPlayerShot()) {
                for (TanKe tanKe : Optional.ofNullable(tanKes).orElse(Lists.newArrayList())) {
                    if (tanKe.getRect().intersects(this.getRect())) {
                        tanKe.die();
                    }
                }
            } else {
                if (player1.getRect().intersects(this.getRect())) {
                    player1.die();
                }
                if (player2.getRect().intersects(this.getRect())) {
                    player2.die();
                }

            }

        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public BufferedImage getBulletImg() {
        return bulletImg;
    }

    public void setBulletImg(BufferedImage bulletImg) {
        this.bulletImg = bulletImg;
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

    public boolean isPlayerShot() {
        return playerShot;
    }

    public void setPlayerShot(boolean playerShot) {
        this.playerShot = playerShot;
    }
}
