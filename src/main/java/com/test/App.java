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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;


public class App {
    private static final Set<Integer> pressedKeys = new HashSet<>();

    public static void main(String[] args) throws IOException {
        BufferedImage parentImg = ImageIO.read(ResourceUtil.getStreamSafe("classpath:images/tank_sprite.png"));

        // 确保一个漂亮的外观风格
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("坦克大战");
        GamePanel gamePanel = new GamePanel(parentImg);
        gamePanel.init();

        frame.add(gamePanel);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                pressedKeys.remove(e.getKeyCode());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                pressedKeys.add(e.getKeyCode());
                for (Integer keyCode : pressedKeys) {
                    TanKe tanKe1 = gamePanel.getTanKeOne();
                    TanKe tanKe2 = gamePanel.getTanKeTwo();
                    Direction direction1 = null;
                    Direction direction2 = null;
                    if ((gamePanel.isOver()) && keyCode != 10) {
                        return;
                    }
                    if (keyCode == 65) {
                        direction1 = Direction.LEFT;
                    }
                    if (keyCode == 87) {
                        direction1 = Direction.UP;
                    }
                    if (keyCode == 68) {
                        direction1 = Direction.RIGHT;
                    }
                    if (keyCode == 83) {
                        direction1 = Direction.DOWN;
                    }
                    if (keyCode == 37) {
                        direction2 = Direction.LEFT;
                    }
                    if (keyCode == 38) {
                        direction2 = Direction.UP;
                    }
                    if (keyCode == 39) {
                        direction2 = Direction.RIGHT;
                    }
                    if (keyCode == 40) {
                        direction2 = Direction.DOWN;
                    }

                    if (keyCode == 74) {
                        if (!gamePanel.getTanKeOne().isDie() && gamePanel.isStarted()) {
                            Bullet bullet = new Bullet(parentImg, tanKe1.getX() + 6, tanKe1.getY() + 6, tanKe1.getDirection(), true);
                            gamePanel.addBullet(bullet);
                            PoolUtil.submit(new Audio("audio/shoot.wav")::play);
                        }

                    }
                    if (keyCode == 98) {
                        if (!gamePanel.getTanKeTwo().isDie() && gamePanel.isStarted()) {
                            Bullet bullet = new Bullet(parentImg, tanKe2.getX() + 6, tanKe2.getY() + 6, tanKe2.getDirection(), true);
                            gamePanel.addBullet(bullet);
                            PoolUtil.submit(new Audio("audio/shoot.wav")::play);
                        }
                    }

                    if (direction1 != null) {
                        if (!gamePanel.getTanKeOne().isDie() && gamePanel.isStarted()) {
                            tanKe1.move(direction1);
                            PoolUtil.submit(new Audio("audio/player.move.wav")::play);
                            gamePanel.repaint();
                        }
                    }
                    if (direction2 != null) {
                        if (!gamePanel.getTanKeTwo().isDie() && gamePanel.isStarted()) {
                            tanKe2.move(direction2);
                            PoolUtil.submit(new Audio("audio/player.move.wav")::play);
                            gamePanel.repaint();
                        }
                    }
                    //按enter开始
                    if (keyCode == 10) {
                        if (!gamePanel.isStarted()) {
                            gamePanel.setStarted(true);
                            gamePanel.setWin(false);
                            gamePanel.init();
                            PoolUtil.submit(new Audio("audio/hi.wav")::play);
                            var ref = new Object() {
                                ScheduledFuture<?> scheduledFuture = null;
                                ScheduledFuture<?> scheduledFuture1 = null;
                            };
                            ref.scheduledFuture = PoolUtil.startScheduled(() -> {
                                if (gamePanel.isOver()) {
                                    gamePanel.setStarted(false);
                                    gamePanel.setBullets(Lists.newCopyOnWriteArrayList());
                                    ref.scheduledFuture.cancel(false);
                                    return;
                                }
                                for (TanKe ke : gamePanel.getEnemyList().stream().filter(f -> !f.isDie()).toList()) {
                                    Direction dir = Direction.random(ke.getDirection());
                                    ke.keepMove(dir);
                                    Bullet bullet = new Bullet(parentImg, ke.getX() + 6, ke.getY() + 6, ke.getDirection(), false);
                                    gamePanel.addBullet(bullet);
                                    gamePanel.repaint();
                                }
                            }, 400L);
                            ref.scheduledFuture1 = PoolUtil.startScheduled(() -> {
                                if (gamePanel.isOver()) {
                                    ref.scheduledFuture1.cancel(false);
                                }
                                List<Bullet> list = gamePanel.getBullets().stream().filter(f -> !f.isDie()).toList();
                                for (Bullet bullet : list) {
                                    bullet.move(gamePanel);
                                    gamePanel.repaint();
                                }
                            }, 30L);
                        }
                    }
                }


            }
        });
        frame.setBackground(Color.GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GameConsts.PANEL_WIDTH + 26, GameConsts.PANEL_HEIGHT + 30);
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
