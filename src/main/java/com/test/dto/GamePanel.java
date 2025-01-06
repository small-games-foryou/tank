package com.test.dto;

import com.google.common.collect.Lists;
import com.test.consts.GameConsts;
import com.test.service.Audio;
import com.test.util.PoolUtil;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;


public class GamePanel extends JPanel {
    private TanKe tanKeOne;
    private TanKe tanKeTwo;
    private List<TanKe> enemyList = Lists.newCopyOnWriteArrayList();
    private List<Bullet> bullets = Lists.newCopyOnWriteArrayList();
    private BufferedImage parentImg;
    private List<BigExplode> bigExplodes = Lists.newCopyOnWriteArrayList();
    private boolean win;
    private boolean started;

    public GamePanel(BufferedImage parentImg) {
        setSize(GameConsts.PANEL_WIDTH, GameConsts.PANEL_HEIGHT);
        setBackground(Color.BLACK);
        this.parentImg = parentImg;
    }

    public static boolean isOutOfPanel(int x, int y, int width, int height) {
        return isOutOfPanelX(x, width) || isOutOfPanelY(y, height);
    }

    public static boolean isOutOfPanelX(int x, int width) {
        return x < 0 || x + width > GameConsts.PANEL_WIDTH;

    }

    public static boolean isOutOfPanelY(int y, int height) {
        return y < 0 || y + height > GameConsts.PANEL_HEIGHT;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Bullet b : bullets.stream().filter(e -> !e.isDie()).toList()) {
            b.drawMe(g);
        }
        for (TanKe ke : enemyList) {
            ke.drawMe(g);
        }
        tanKeOne.drawMe(g);
        tanKeTwo.drawMe(g);
        if (tanKeOne.isDie()&& tanKeTwo.isDie()) {
            g.setColor(Color.RED);
            g.getFont().deriveFont(14F);
            g.drawString("GAME OVER", GameConsts.PANEL_WIDTH / 2, GameConsts.PANEL_HEIGHT / 2);
            g.drawString("按ENTER重新开始", GameConsts.PANEL_WIDTH / 2, GameConsts.PANEL_HEIGHT / 2+100);
            PoolUtil.submit(new Audio("audio/gameOver.wav")::play);
        }else {
            if(!started){
                g.drawString("按ENTER开始", GameConsts.PANEL_WIDTH / 2, GameConsts.PANEL_HEIGHT / 2+100);
            }
        }
        if (enemyList.stream().allMatch(TanKe::isDie)) {
            win = true;
            g.setColor(Color.GREEN);
            g.getFont().deriveFont(14F);
            g.drawString("YOU WIN", GameConsts.PANEL_WIDTH / 2, GameConsts.PANEL_HEIGHT / 2);
            g.drawString("按ENTER重新开始", GameConsts.PANEL_WIDTH / 2, GameConsts.PANEL_HEIGHT / 2+100);
            PoolUtil.submit(new Audio("audio/intro.wav")::play);
        }
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void init() {
        List<TanKe> objects = Lists.newCopyOnWriteArrayList();
        TanKe tanKe1 = new TanKe(parentImg, GameConsts.PANEL_WIDTH / 2-42, GameConsts.PANEL_HEIGHT - 26, 4, 4);
        TanKe tanKe2 = new TanKe(parentImg, GameConsts.PANEL_WIDTH / 2+42, GameConsts.PANEL_HEIGHT - 26, 4, 38);

        for (int i = 0; i < GameConsts.ENEMY_NUM; i++) {
            TanKe enemy = new TanKe(parentImg, i * (GameConsts.PANEL_WIDTH/GameConsts.ENEMY_NUM), 0, 4, 72);
            objects.add(enemy);
        }
        this.setEnemyList(objects);
        this.setTanKeOne(tanKe1);
        this.setTanKeTwo(tanKe2);
    }

    public boolean isOver() {
        return (tanKeOne.isDie()&& tanKeTwo.isDie()) || isWin();
    }

    public TanKe getTanKeOne() {
        return tanKeOne;
    }

    public void setTanKeOne(TanKe tanKeOne) {
        this.tanKeOne = tanKeOne;
    }

    public TanKe getTanKeTwo() {
        return tanKeTwo;
    }

    public void setTanKeTwo(TanKe tanKeTwo) {
        this.tanKeTwo = tanKeTwo;
    }

    public List<TanKe> getEnemyList() {
        return enemyList;
    }

    public void setEnemyList(List<TanKe> enemyList) {
        this.enemyList = enemyList;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    public BufferedImage getParentImg() {
        return parentImg;
    }

    public void setParentImg(BufferedImage parentImg) {
        this.parentImg = parentImg;
    }

    public List<BigExplode> getBigExplodes() {
        return bigExplodes;
    }

    public void setBigExplodes(List<BigExplode> bigExplodes) {
        this.bigExplodes = bigExplodes;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

}
