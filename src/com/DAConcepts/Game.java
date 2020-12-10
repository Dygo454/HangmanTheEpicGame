package com.DAConcepts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 128;
    private static final int HEIGHT = 128;
    private static final int SCALE = 5;
    private final String TITLE = "Hangman The Epic Game";
    private boolean running = true;
    private static JFrame frame;
    private Thread thread;
    private BufferedImage BG;
    private BufferedImage head;
    private BufferedImage arm1;
    private BufferedImage arm2;
    private BufferedImage bod1;
    private BufferedImage bod2;
    private BufferedImage leg1;
    private BufferedImage leg2;
    public int livesLeft = 7;
    private int lastLivesLeft = 8;
    private Player player;
    private char letter = '0';

    public static void main(String[] args) {
        Game game = new Game();
        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        frame = new JFrame(game.TITLE);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.start();
    }

    private void init() {
        player = new Player(this);
        requestFocus();
        BufferedImageLoader loader = new BufferedImageLoader();
        try {
            BG = loader.LoadImage("res/BG.png");
            head = loader.LoadImage("res/head.png");
            arm1 = loader.LoadImage("res/arm1.png");
            arm2 = loader.LoadImage("res/arm2.png");
            bod1 = loader.LoadImage("res/bod1.png");
            bod2 = loader.LoadImage("res/bod2.png");
            leg1 = loader.LoadImage("res/leg1.png");
            leg2 = loader.LoadImage("res/leg2.png");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void start() {
        thread = new Thread(this);
        addKeyListener(new KeyInput(this));
        init();
        thread.start();
    }

    public synchronized void stop() {
        System.exit(1);
    }

    @Override
    public void run() {
        while (running) {
            this.tick();
            this.render();
            if (livesLeft <= 0 || player.won) {
                break;
            }
        }
        stop();
    }

    public void tick() {
        //asdfghjkl
    }

    public void keyPressed(KeyEvent e) {
        if ("abcdefghijklmnopqrstuvwxyz".contains(e.getKeyChar() + "")) {
            letter = (""+e.getKeyChar()).toUpperCase().charAt(0);
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (player.tryGuess(letter)) {
                if (!player.guess(letter)) {
                    livesLeft--;
                }
            }
            letter = '0';
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        while (lastLivesLeft != livesLeft) {
            BufferedImage[] parts = {BG, head,arm1,arm2,bod1,bod2,leg1,leg2};
            g.drawImage(parts[8-lastLivesLeft],0,0, WIDTH*SCALE,HEIGHT*SCALE,this);
            lastLivesLeft--;
        }
        player.render(g);
        g.dispose();
        bs.show();
    }
}
