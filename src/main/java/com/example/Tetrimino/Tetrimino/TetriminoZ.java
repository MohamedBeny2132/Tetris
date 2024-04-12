package com.example.Tetrimino.Tetrimino;

import com.example.Tetrimino.Tetrimino.Tetrimino;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TetriminoZ extends Tetrimino
{
    public TetriminoZ()
    {
        this.forme = new int[2][3];
        this.couleur = 6;
        this.color = Color.RED;

        for (int x = 0;x<forme[0].length-1;x++)
            this.forme[0][x] = this.couleur;

        for (int x = 1;x<forme[0].length;x++)
            this.forme[1][x] = this.couleur;
    }

    @Override
    protected void creeSchema()
    {
        for (int i = 0; i < 2; i++)
        {
            Rectangle rect = new Rectangle(30, 30, Color.RED);
            rect.setX(i * 30);
            rect.setStroke(Color.GRAY);
            this.shema.getChildren().add(rect);
        }

        for (int i = 0; i < 2; i++)
        {
            Rectangle rect = new Rectangle(30, 30, Color.RED);
            rect.setX((i + 1) * 30);
            rect.setY(30);
            rect.setStroke(Color.GRAY);
            this.shema.getChildren().add(rect);
        }
    }
}
