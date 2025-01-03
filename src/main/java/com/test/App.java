package com.test;

import cn.hutool.core.io.resource.ResourceUtil;
import com.google.common.collect.Lists;
import com.test.consts.Direction;
import com.test.consts.GameConsts;
import com.test.service.Audio;
import com.test.dto.Bullet;
import com.test.dto.GamePanel;
import com.test.dto.TanKe;
import com.test.util.PoolUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;


public class App {
    private static boolean started = false;

    public static void main(String[] args) throws IOException {
        BufferedImage parentImg = ImageIO.read(ResourceUtil.getStreamSafe("classpath:images/tank_sprite.png"));

        // 确保一个漂亮的外观风格
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("坦克大战");
        GamePanel gamePanel = new GamePanel(parentImg);
        List<TanKe> objects = Lists.newCopyOnWriteArrayList();
        TanKe tanKe = new TanKe(parentImg, GameConsts.PANEL_WIDTH / 2, GameConsts.PANEL_HEIGHT - 26, 4, 4);
        for (int i = 0; i < GameConsts.ENEMY_NUM; i++) {
            TanKe enemy = new TanKe(parentImg, i * 104, 0, 4, 72);
            objects.add(enemy);
        }
        gamePanel.setEnemyList(objects);
        gamePanel.setTanKe(tanKe);

        frame.add(gamePanel);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(tanKe.isDie()||gamePanel.isWin()){
                    return;
                }
                super.keyPressed(e);
                Direction direction = null;
                int keyCode = e.getKeyCode();
                if (keyCode == 37) {
                    direction = Direction.LEFT;
                }
                if (keyCode == 38) {
                    direction = Direction.UP;
                }
                if (keyCode == 39) {
                    direction = Direction.RIGHT;
                }
                if (keyCode == 40) {
                    direction = Direction.DOWN;
                }
                if (keyCode == 32) {
                    Bullet bullet = new Bullet(parentImg, tanKe.getX() + 6, tanKe.getY() + 6, tanKe.getDirection(),true);
                    gamePanel.addBullet(bullet);
                    PoolUtil.submit(new Audio("audio/shoot.wav")::play);
                }
                if (direction != null) {
                    tanKe.move(direction);
                    PoolUtil.submit(new Audio("audio/player.move.wav")::play);
                    gamePanel.repaint();
                }
                if (!started) {
                    started = true;
                    PoolUtil.submit(new Audio("audio/hi.wav")::play);
                    PoolUtil.startScheduled(() -> {
                        if (tanKe.isDie()||gamePanel.isWin()) {
                            started = false;
                            return;
                        }
                        for (TanKe ke : gamePanel.getEnemyList().stream().filter(f->!f.isDie()).toList()) {
                            Direction dir = Direction.random(ke.getDirection());
                            ke.keepMove(dir);
                            Bullet bullet = new Bullet(parentImg, ke.getX() + 6, ke.getY() + 6, ke.getDirection(),false);
                            gamePanel.addBullet(bullet);
                            gamePanel.repaint();
                        }
                    }, 400L);
                    PoolUtil.startScheduled(() -> {
                        if (tanKe.isDie()||gamePanel.isWin()) {
                            return;
                        }
                        List<Bullet> list = gamePanel.getBullets().stream().filter(f -> !f.isDie()).toList();
                        for (Bullet bullet : list) {
                            bullet.move(gamePanel.getEnemyList(),tanKe);
                            gamePanel.repaint();
                        }
                    }, 30L);
                }

            }
        });
        frame.setBackground(Color.GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GameConsts.PANEL_WIDTH+26, GameConsts.PANEL_HEIGHT+30);
        frame.setFocusable(true);
        int windowWidth = frame.getWidth(); // 获得窗口宽
        int windowHeight = frame.getHeight(); // 获得窗口高
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
        Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
        int screenWidth = screenSize.width; // 获取屏幕的宽
        int screenHeight = screenSize.height; // 获取屏幕的高
        frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

        frame.setVisible(true);
        frame.setResizable(false);


    }

}
