package com.test.dto;

import com.google.common.collect.Lists;
import com.test.consts.GameConsts;
import com.test.service.Audio;
import com.test.util.PoolUtil;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

@Data
public class GamePanel extends JPanel {
    private TanKe tanKe1;
    private TanKe tanKe2;
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
        tanKe1.drawMe(g);
        tanKe2.drawMe(g);
        if (tanKe1.isDie()&&tanKe2.isDie()) {
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
        this.setTanKe1(tanKe1);
        this.setTanKe2(tanKe2);
    }

    public boolean isOver() {
        return (tanKe1.isDie()&&tanKe2.isDie()) || isWin();
    }
}
