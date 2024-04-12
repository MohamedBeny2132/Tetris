package com.example.Tetrimino.Tetrimino;

import com.example.Tetrimino.Tetrimino.Tetrimino;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TetriminoO extends Tetrimino
{
    public TetriminoO()
    {
        this.forme = new int[2][2];
        this.couleur = 2;
        this.color = Color.YELLOW;

        for (int y = 0;y<forme.length;y++)
            for (int x = 0;x<forme[0].length;x++)
                this.forme[y][x] = this.couleur;

    }

    @Override
    protected void creeSchema()
    {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Rectangle rect = new Rectangle(30, 30, Color.YELLOW);
                rect.setX(i * 30);
                rect.setY(j * 30);
                rect.setStroke(Color.GRAY);
                this.shema.getChildren().add(rect);
            }
        }
    }
}
