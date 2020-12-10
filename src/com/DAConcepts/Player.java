package com.DAConcepts;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Player {
    private Game game;
    public boolean won = false;
    private String secret;
    private String blanks;
    private String guessed = "";

    public Player(Game g) {
        game = g;
        String[] words = {"interest", "program", "obscure", "mutator", "constructor", "hangman", "variable", "constant", "savings"};
        try {
            Scanner input = new Scanner(new File("res/words.txt"));
            words = new String[3000];
            int index = 0;
            while(input.hasNextLine()){
                words[index] = input.nextLine().toLowerCase();
                index++;
            }
            input.close();
        }
        catch (Exception e) {
            System.out.println("File \"words.txt\" was not found. Using shorter list of words instead.");
        }
        secret = "12345678901";
        while (secret.length() > 10) {
            secret = words[(int) (Math.random() * words.length)].toUpperCase();
        }
        blanks = "";
        for (char c : secret.toCharArray()) {
            blanks += "_ ";
        }
    }

    public boolean guess(char c) {
        guessed += c+" ";
        if (secret.contains(""+c)) {
            int ind = 0;
            while (secret.indexOf(""+c,ind) != -1) {
                ind = secret.indexOf(""+c,ind);
                blanks = blanks.substring(0,ind*2)+c+blanks.substring(ind*2+1);
                ind++;
            }
            if (!blanks.contains("_")) {
                won = true;
            }
            return true;
        }
        return false;
    }

    public boolean tryGuess(char c) {
        if (!guessed.contains("" + c) && c != '0') {
            return true;
        }
        return false;
    }

    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Verdana", 0, 20));
        // letters used:
        g.clearRect((int) (128*2.75),(int) (128*2.5),(int) (128*2.25),128);
        String lettersUsed = guessed;
        for (int i = 0; i < lettersUsed.length(); i += 20) {
            g.drawString(lettersUsed.substring(i,Math.min(i+20,lettersUsed.length())),(int) (128*2.75),(int) (128*2.5)+30+(int) (i*1.5));
        }
        // blanks/instructions:
        g.clearRect(31,(int) (128*2.5),(int) (128*2.125),100);
        String blanksOrInstructions = "Word: "+blanks+"\n(Press the letter you wish\nto guess then press enter)";
        int count = 0;
        for (String subStr : blanksOrInstructions.split("\n")) {
            g.drawString(subStr, 32, (int) (128 * 2.5) + 30+count*30);
            count++;
        }
    }
}
