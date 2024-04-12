package com.example.Tetrimino.Tetrimino;

import com.example.Tetrimino.Tetrimino.Tetrimino;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TetriminoI extends Tetrimino
{
    public TetriminoI()
    {
        this.forme = new int[1][4];
        this.couleur = 1;
        this.color = Color.CYAN;


        for (int x = 0;x<forme[0].length;x++)
            this.forme[0][x] = this.couleur;
    }


    @Override
    protected void creeSchema()
    {
        for (int i = 0; i < 4; i++) {
            Rectangle rect = new Rectangle(30, 30, Color.CYAN);
            rect.setStroke(Color.GRAY);
            rect.setX(i * 30);
            this.shema.getChildren().add(rect);
        }
    }
}
