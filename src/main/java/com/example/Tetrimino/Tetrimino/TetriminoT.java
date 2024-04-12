package com.example.Tetrimino.Tetrimino;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TetriminoT extends Tetrimino
{
    public TetriminoT()
    {
        this.forme = new int[2][3];
        this.couleur = 3;
        this.color = Color.MAGENTA;

        for (int x = 0;x<forme[0].length;x++)
                this.forme[0][x] = this.couleur;

        this.forme[1][1] = this.couleur;
    }

    @Override
    protected void creeSchema()
    {
        for (int i = 0; i < 3; i++)
        {
            Rectangle rect = new Rectangle(30, 30, Color.PURPLE);
            rect.setX(i * 30);
            rect.setStroke(Color.GRAY);
            this.shema.getChildren().add(rect);
        }

        Rectangle rectT = new Rectangle(30, 30, Color.PURPLE);
        rectT.setX(30);
        rectT.setY(-30);
        rectT.setStroke(Color.GRAY);
        this.shema.getChildren().add(rectT);
    }

}
