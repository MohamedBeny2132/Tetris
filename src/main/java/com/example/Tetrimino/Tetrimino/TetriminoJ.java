package com.example.Tetrimino.Tetrimino;

import com.example.Tetrimino.Tetrimino.Tetrimino;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TetriminoJ extends Tetrimino
{
    public TetriminoJ()
    {
        this.forme = new int[2][3];
        this.couleur = 5;
        this.color = Color.ORANGE;

        for (int x = 0;x<forme[0].length;x++)
            this.forme[0][x] = this.couleur;

        this.forme[1][2] = this.couleur;
    }

    @Override
    protected void creeSchema()
    {
        for (int i = 0; i < 3; i++)
        {
            Rectangle rect = new Rectangle(30, 30, Color.ORANGE);
            rect.setX(i * 30);
            rect.setStroke(Color.GRAY);
            this.shema.getChildren().add(rect);
        }

        Rectangle rectJ = new Rectangle(30, 30, Color.ORANGE);
        rectJ.setX(0);
        rectJ.setY(-30);
        rectJ.setStroke(Color.GRAY);
        this.shema.getChildren().add(rectJ);
    }
}
