package com.example.Tetrimino.Tetrimino;

import com.example.Tetrimino.Tetrimino.Tetrimino;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TetriminoL extends Tetrimino
{
    public TetriminoL()
    {
        this.forme = new int[2][3];
        this.couleur = 4;
        this.color = Color.BLUE;

        for (int x = 0;x<forme[0].length;x++)
            this.forme[0][x] = this.couleur;

        this.forme[1][0] = this.couleur;
    }

    @Override
    protected void creeSchema()
    {
        for (int i = 0; i < 3; i++)
        {
            Rectangle rect = new Rectangle(30, 30, Color.BLUE);
            rect.setX(i * 30);
            rect.setStroke(Color.GRAY);
            this.shema.getChildren().add(rect);
        }

        Rectangle rectL = new Rectangle(30, 30, Color.BLUE);
        rectL.setX(2 * 10);
        rectL.setY(-10);
        this.shema.getChildren().add(rectL);
    }
}
