package com.example.Tetrimino.Tetrimino;

import com.example.Tetrimino.Utilitaire.Position;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public abstract class Tetrimino
{

    protected int[][] forme;
    protected int couleur;
    protected Color color;

    protected int posX;
    protected int posY;

    protected Pane shema;



    public Tetrimino()
    {
        this.posX = 5;
        this.posY = 0;

        this.shema = new Pane();
        this.creeSchema();
    }

    public void setPosY(int y)
    {
        this.posY = y;
    }



    public void rotationDroite() {
        forme = formeSuivante();
    }

    public int[][] formeSuivante()
    {
        int[][] newForme = new int[forme[0].length][forme.length];

        for (int y = 0; y < forme.length; y++)
            for (int x = 0; x < forme[0].length; x++)
                newForme[x][y] = forme[y][x];


        for (int y = 0; y < newForme.length / 2; y++)
        {
            int[] temp = newForme[y];
            newForme[y] = newForme[newForme.length - 1 - y];
            newForme[newForme.length - 1 - y] = temp;
        }

        return newForme;
    }

    public void descend()
    {
        this.posY++;
    }

    public void seDeplaceAgauche()
    {
        this.posX--;
    }

    public void seDeplaceAdroite()
    {
        this.posX++;
    }

    public int hauteur()
    {
        return forme.length;
    }

    public int largeur()
    {
        return forme[0].length;
    }

    public Position getPosition()
    {
        return new Position(posX,posY);
    }

    public Color getColor()
    {
        return this.color;
    }
    public int getCouleur()
    {
        return this.couleur;
    }

    public boolean caseVide(int x,int y)
    {
        return this.forme[y][x] == 0;
    }



    public static Tetrimino tetriminoParIndex(int i)
    {
        return switch (i)
        {
            case 1 -> new TetriminoI();
            case 2 -> new TetriminoO();
            case 3 -> new TetriminoT();
            case 4 -> new TetriminoL();
            case 5 -> new TetriminoJ();
            case 6 -> new TetriminoZ();
            case 7 -> new TetriminoS();
            default -> null;
        };

    }

    public boolean faitPartiDeLaForme(int x,int y)
    {
        return this.forme[y][x] != 0;
    }


    protected abstract void creeSchema();
    public Pane shemaTetrimino()
    {
        return this.shema;
    }




}
