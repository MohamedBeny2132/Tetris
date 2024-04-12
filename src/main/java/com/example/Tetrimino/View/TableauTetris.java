package com.example.Tetrimino.View;

import com.example.Tetrimino.Utilitaire.Position;
import com.example.Tetrimino.Tetrimino.Tetrimino;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

public class TableauTetris extends GridPane
{
    private final int HAUTEUR = 20;
    private final int LARGEUR = 10;
    private final int tailleCellule = 30;
    private Rectangle[][] cellules;
    private int[][] plateau;
    private Tetrimino tetriminoActuelle;
    private Tetrimino tetriminoSuivant;
    private AnimationTimer animationTimer;
    private InformationJeuEnCour info;

    private boolean partiFini = false;

    public TableauTetris(InformationJeuEnCour info)
    {
        initPremiersTetrimino();
        initTableau();
        this.info = info;
        this.animationTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            private long intervalNs = 1000000000L;

            @Override
            public void handle(long now) {
                if ((now - lastUpdate) >= intervalNs) {
                    joue();
                    lastUpdate = now;
                }
            }
        };

        this.animationTimer.start();
        dessineTetrimino();


        add(info,LARGEUR+1,0);
    }

    private void initTableau()
    {
        this.cellules = new Rectangle[HAUTEUR][LARGEUR];
        this.plateau = new int[HAUTEUR][LARGEUR];

        for (int y = 0; y < HAUTEUR; y++)
        {
            for (int x = 0; x < LARGEUR; x++)
            {
                this.cellules[y][x] = new Rectangle(tailleCellule, tailleCellule);
                this.cellules[y][x].setFill(Color.WHITE);
                this.cellules[y][x].setStroke(Color.GRAY);

                this.add(this.cellules[y][x], x, y);
            }
        }
    }

    private void initPremiersTetrimino()
    {
        Random rdm = new Random();

        this.tetriminoActuelle = Tetrimino.tetriminoParIndex(rdm.nextInt(7)+1);
        this.tetriminoSuivant = Tetrimino.tetriminoParIndex(rdm.nextInt(7)+1);
    }


    public void start() { animationTimer.start(); }
    public void pause()
    {
        animationTimer.stop();
    }





    // S'OCCUPE DE DESCENDRE LE PLATEAU EN CAS DE LIGNE COMPLETE
    private void descendrePlateau(List<Integer> lignes)
    {
        int premierLigne = lignes.getFirst();
        int derniereLigne = lignes.getLast();


        if (premierLigne != 0)
        {
            for (int y = derniereLigne ; y > 0; y--)
            {
                for (int x = 0; x < LARGEUR; x++)
                {
                    this.plateau[y][x] = this.plateau[y-1][x];
                    this.cellules[y][x].setFill(this.cellules[y-1][x].getFill());
                }
            }
        }
    }

    private void supprimeLigne(List<Integer> lignes)
    {
        for (int y : lignes)
        {
            for (int x = 0; x < LARGEUR; x++)
            {
                this.plateau[y][x] = 0;
                this.cellules[y][x].setFill(Color.WHITE);
            }
        }
    }

    private List<Integer> chercheLigneComplete()
    {
        List<Integer> ligneAsupprimer = new ArrayList<>();

        boolean ligneComplete;

        for (int y = 0;y < HAUTEUR ;y++)
        {
            ligneComplete = true;

            for (int x = 0; x < LARGEUR && ligneComplete; x++)
            {
                if (this.plateau[y][x] == 0)
                    ligneComplete = false;
            }

            if (ligneComplete)
                ligneAsupprimer.add(y);


        }

        return ligneAsupprimer;
    }


    private boolean caseEstVide(int x,int y)
    {
        return this.plateau[y][x] == 0;
    }
    private void dessineTetrimino()
    {
        Position pos = this.tetriminoActuelle.getPosition();

        for (int y = 0; y < this.tetriminoActuelle.hauteur(); y++)
        {
            for (int x = 0; x < this.tetriminoActuelle.largeur(); x++)
            {
                if (!this.tetriminoActuelle.caseVide(x,y))
                {
                    this.plateau[pos.getY() + y][pos.getX()+x] = this.tetriminoActuelle.getCouleur();
                    this.cellules[pos.getY() + y][pos.getX()+x].setFill(this.tetriminoActuelle.getColor());
                }
            }
        }
    }

    private void effaceTetrimino()
    {
        Position pos = this.tetriminoActuelle.getPosition();

        for (int y = 0;y < this.tetriminoActuelle.hauteur();y++)
        {
            for (int x = 0;x < this.tetriminoActuelle.largeur();x++)
            {
                if (!this.tetriminoActuelle.caseVide(x,y))
                {
                    this.cellules[pos.getY()+y][pos.getX()+x].setFill(Color.WHITE);
                    this.plateau[pos.getY()+y][pos.getX()+x] = 0;
                }

            }
        }
    }

    public void joue()
    {
        if (!partiFini)
        {
            if (peuDescendre())
            {

                descendreTetrimino();

            }
            else
            {
                pause();

                PauseTransition pause = new PauseTransition(Duration.seconds(0.7));

                pause.setOnFinished(event ->
                {
                    if (!peuDescendre())
                    {
                        List<Integer> lignesASupprimer = chercheLigneComplete();

                        if (!lignesASupprimer.isEmpty())
                        {
                            supprimeLigne(lignesASupprimer);
                            descendrePlateau(lignesASupprimer);
                            ajoutScore(lignesASupprimer.size());
                        }

                        nouveauTetrimino();
                        actualiseShema();

                        partiFini = !peuDescendre();
                    }

                    start();
                });

                pause.play();
            }
        }
        else
        {
            System.exit(1);
        }



    }

    private boolean finDePartie()
    {
        for (int i = 0;i<LARGEUR;i++)
            if (plateau[0][i] != 0)
                return true;
        return false;
    }

    private void nouveauTetrimino()
    {
        Random rdm = new Random();
        this.tetriminoActuelle = this.tetriminoSuivant;
        this.tetriminoSuivant = Tetrimino.tetriminoParIndex(rdm.nextInt(7)+1);
    }








    // S'OCCUPE DES DEPLACEMENT

    private boolean peuDescendre()
    {



        boolean peuDescendre = true;
        Position pos = this.tetriminoActuelle.getPosition();

        int ligneEnDessousDuTetrimino = pos.getY() + this.tetriminoActuelle.hauteur();


        if (ligneEnDessousDuTetrimino < HAUTEUR)
        {
            for (int y = this.tetriminoActuelle.hauteur()-1;y >= 0 && peuDescendre;y--)
            {
                for (int x = 0;x<this.tetriminoActuelle.largeur() && peuDescendre;x++)
                {
                    if (this.tetriminoActuelle.faitPartiDeLaForme(x,y))
                    {
                        if (y == tetriminoActuelle.hauteur()-1)
                        {
                            if (!caseEstVide(pos.getX()+x,ligneEnDessousDuTetrimino))
                                peuDescendre = false;
                        }
                        else
                        {
                            if (!tetriminoActuelle.faitPartiDeLaForme(x,y+1) && !caseEstVide(pos.getX()+x,ligneEnDessousDuTetrimino))
                                peuDescendre = false;
                        }
                    }
                }
                ligneEnDessousDuTetrimino--;
            }
        }
        else
        {
            peuDescendre = false;
        }

        return  peuDescendre;
    }

    private void descendreTetrimino()
    {
        effaceTetrimino();
        this.tetriminoActuelle.descend();
        dessineTetrimino();
    }


    public boolean peuSeDeplacerAdroite()
    {
        boolean peuSeDeplacerAdroite = true;
        Position pos = this.tetriminoActuelle.getPosition();

        int coteDroite = pos.getX() + this.tetriminoActuelle.largeur();


        if (coteDroite < LARGEUR)
        {
            for (int y = 0;y < this.tetriminoActuelle.hauteur() && peuSeDeplacerAdroite;y++)
            {
                for (int x = this.tetriminoActuelle.largeur()-1;x >= 0 && peuSeDeplacerAdroite;x--)
                {
                    if (this.tetriminoActuelle.faitPartiDeLaForme(x,y))
                    {
                        if (x == this.tetriminoActuelle.largeur()-1)
                        {
                            if (!caseEstVide(coteDroite,pos.getY()+y))
                                peuSeDeplacerAdroite = false;
                        }
                        else
                        {
                            if (!this.tetriminoActuelle.faitPartiDeLaForme(x+1,y) && !caseEstVide(pos.getX()+x+1, pos.getY()+y))
                                peuSeDeplacerAdroite = false;
                        }
                    }
                }
            }
        }
        else
        {
            peuSeDeplacerAdroite = false;
        }

        return  peuSeDeplacerAdroite;
    }
    public void seDeplacerAdroite()
    {
        effaceTetrimino();
        this.tetriminoActuelle.seDeplaceAdroite();
        dessineTetrimino();
    }

    public boolean peuSeDeplacerAgauche()
    {
        boolean peuSeDeplacerAgauche = true;
        Position pos = this.tetriminoActuelle.getPosition();
        int coteGauche = pos.getX();

        if (coteGauche > 0)
        {
            coteGauche--;
            for (int y = 0;y < this.tetriminoActuelle.hauteur() && peuSeDeplacerAgauche;y++)
            {
                for (int x = 0;x < this.tetriminoActuelle.largeur() && peuSeDeplacerAgauche;x++)
                {
                    if (this.tetriminoActuelle.faitPartiDeLaForme(x,y))
                    {
                        if (x == 0)
                        {
                            if (!caseEstVide(coteGauche,pos.getY()+y))
                                peuSeDeplacerAgauche = false;
                        }
                        else
                        {
                            if (!tetriminoActuelle.faitPartiDeLaForme(x-1,y) && !caseEstVide(pos.getX()+x-1, pos.getY()+y))
                                peuSeDeplacerAgauche = false;
                        }
                    }
                }
            }
        }
        else
        {
            peuSeDeplacerAgauche = false;
        }

        return  peuSeDeplacerAgauche;
    }
    public void seDeplacerAGauche()
    {
        effaceTetrimino();
        this.tetriminoActuelle.seDeplaceAgauche();
        dessineTetrimino();
    }



    // S'OCCUPE DES ROTATION
    public boolean peuFaireUneRotation()
    {
        int[][] formeSuivante = this.tetriminoActuelle.formeSuivante();
        Position pos = this.tetriminoActuelle.getPosition();
        boolean peuFaireUneRotation = true;

        int limiteGauche = pos.getX();
        int limiteDroite = pos.getX() + formeSuivante[0].length-1;
        int limiteHaut = pos.getY();
        int limiteBas = pos.getY() + formeSuivante.length-1;

        effaceTetrimino();

        if (limiteGauche >= 0 && limiteDroite < LARGEUR && limiteBas < HAUTEUR && limiteHaut >= 0)
        {
            for (int y = 0;y < formeSuivante.length && peuFaireUneRotation;y++)
            {
                for (int x = 0; x < formeSuivante[0].length && peuFaireUneRotation; x++)
                {
                    if (formeSuivante[y][x] != 0 && !caseEstVide(pos.getX()+x,pos.getY()+y))
                        peuFaireUneRotation = false;
                }
            }
        }
        else
        {
            peuFaireUneRotation = false;
        }

        dessineTetrimino();

        return peuFaireUneRotation;
    }

    public void rotation()
    {
        effaceTetrimino();
        this.tetriminoActuelle.rotationDroite();
        dessineTetrimino();
    }


    // S'OCCUPE D'ACTUALISER LA PANNEAU D'INFORMATION
    private void actualiseShema()
    {
        this.info.nouveauTetrimino(this.tetriminoSuivant.shemaTetrimino());
    }
    private void ajoutScore(int ligne)
    {
        this.info.ajoutScore(ligne*125);
    }
}































































/*
*
*
*
*
package com.example.Tetrimino.View;

import com.example.Tetrimino.Utilitaire.Position;
import com.example.Tetrimino.Tetrimino.Tetrimino;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

public class TableauTetris extends GridPane
{
    private final int HAUTEUR = 20;
    private final int LARGEUR = 10;
    private final int tailleCellule = 30;
    private Rectangle[][] cellules;
    private int[][] plateau;
    private Tetrimino tetriminoActuelle;
    private Tetrimino tetriminoSuivant;
    private AnimationTimer animationTimer;
    private InformationJeuEnCour info;

    private boolean partiFini = false;

    public TableauTetris(InformationJeuEnCour info)
    {
        initPremiersTetrimino();
        initTableau();
        this.info = info;
        this.animationTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            private long intervalNs = 1000000000L;

            @Override
            public void handle(long now) {
                if ((now - lastUpdate) >= intervalNs) {
                    joue();
                    lastUpdate = now;
                }
            }
        };

        this.animationTimer.start();
        dessineTetrimino();


        add(info,LARGEUR+1,0);
    }

    private void initTableau()
    {
        this.cellules = new Rectangle[HAUTEUR][LARGEUR];
        this.plateau = new int[HAUTEUR ][LARGEUR];

        for (int y = 0; y < HAUTEUR; y++)
        {
            for (int x = 0; x < LARGEUR; x++)
            {
                this.cellules[y][x] = new Rectangle(tailleCellule, tailleCellule);
                this.cellules[y][x].setFill(Color.WHITE);
                this.cellules[y][x].setStroke(Color.GRAY);

                this.add(this.cellules[y][x], x, y);
            }
        }
    }

    private void initPremiersTetrimino()
    {
        Random rdm = new Random();

        this.tetriminoActuelle = Tetrimino.tetriminoParIndex(rdm.nextInt(7)+1);
        this.tetriminoSuivant = Tetrimino.tetriminoParIndex(rdm.nextInt(7)+1);
    }


    public void start() {animationTimer.start();}
    public void pause()
    {
        animationTimer.stop();
    }





    // S'OCCUPE DE DESCENDRE LE PLATEAU EN CAS DE LIGNE COMPLETE
    private void descendrePlateau(List<Integer> lignes)
    {
        int premierLigne = lignes.getFirst();
        int derniereLigne = lignes.getLast();


        if (premierLigne != 0)
        {
            for (int y = derniereLigne ; y > 0; y--)
            {
                for (int x = 0; x < LARGEUR; x++)
                {
                    this.plateau[y][x] = this.plateau[y-1][x];
                    this.cellules[y][x].setFill(this.cellules[y-1][x].getFill());
                }
            }
        }
    }

    private void supprimeLigne(List<Integer> lignes)
    {
        for (int y : lignes)
        {
            for (int x = 0; x < LARGEUR; x++)
            {
                this.plateau[y][x] = 0;
                this.cellules[y][x].setFill(Color.WHITE);
            }
        }
    }

    private List<Integer> chercheLigneComplete()
    {
        List<Integer> ligneAsupprimer = new ArrayList<>();

        boolean ligneComplete;

        for (int y = 0;y < HAUTEUR ;y++)
        {
            ligneComplete = true;

            for (int x = 0; x < LARGEUR && ligneComplete; x++)
            {
                if (this.plateau[y][x] == 0)
                    ligneComplete = false;
            }

            if (ligneComplete)
                ligneAsupprimer.add(y);


        }

        return ligneAsupprimer;
    }


    private boolean caseEstVide(int x,int y)
    {
        return this.plateau[y][x] == 0;
    }
    private void dessineTetrimino()
    {
        Position pos = this.tetriminoActuelle.getPosition();

        for (int y = 0; y < this.tetriminoActuelle.hauteur(); y++)
        {
            for (int x = 0; x < this.tetriminoActuelle.largeur(); x++)
            {
                if (!this.tetriminoActuelle.caseVide(x,y))
                {
                    this.plateau[pos.getY() + y][pos.getX()+x] = this.tetriminoActuelle.getCouleur();
                    this.cellules[pos.getY() + y][pos.getX()+x].setFill(this.tetriminoActuelle.getColor());
                }
            }
        }
    }

    private void effaceTetrimino()
    {
        Position pos = this.tetriminoActuelle.getPosition();

        for (int y = 0;y < this.tetriminoActuelle.hauteur();y++)
        {
            for (int x = 0;x < this.tetriminoActuelle.largeur();x++)
            {
                if (!this.tetriminoActuelle.caseVide(x,y))
                {
                    this.cellules[pos.getY()+y][pos.getX()+x].setFill(Color.WHITE);
                    this.plateau[pos.getY()+y][pos.getX()+x] = 0;
                }

            }
        }
    }

    public void joue()
    {
        if (!partiFini)
        {
            if (peuDescendre())
            {

                descendreTetrimino();

            }
            else
            {
                pause();

                PauseTransition pause = new PauseTransition(Duration.seconds(0.7));

                pause.setOnFinished(event ->
                {
                    if (!peuDescendre())
                    {
                        List<Integer> lignesASupprimer = chercheLigneComplete();

                        if (!lignesASupprimer.isEmpty())
                        {
                            supprimeLigne(lignesASupprimer);
                            descendrePlateau(lignesASupprimer);
                            ajoutScore(lignesASupprimer.size());
                        }

                        nouveauTetrimino();
                        actualiseShema();
                    }

                    start();
                });

                pause.play();
            }
        }
        else
        {
            System.out.println("fegzgzegzefg");
            pause();
        }

        partiFini = finDePartie();

    }

    private boolean finDePartie()
    {
        for (int i = 0;i<LARGEUR;i++)
            if (plateau[0][i] != 0)
                return true;
        return false;
    }

    private void nouveauTetrimino()
    {
        Random rdm = new Random();
        this.tetriminoActuelle = this.tetriminoSuivant;
        this.tetriminoSuivant = Tetrimino.tetriminoParIndex(rdm.nextInt(7)+1);
    }








    // S'OCCUPE DES DEPLACEMENT

    private boolean peuDescendre()
    {
        boolean peuDescendre = true;
        Position pos = this.tetriminoActuelle.getPosition();

        int ligneEnDessousDuTetrimino = pos.getY() + this.tetriminoActuelle.hauteur();


        if (ligneEnDessousDuTetrimino < HAUTEUR)
        {
            for (int y = this.tetriminoActuelle.hauteur()-1;y >= 0 && peuDescendre;y--)
            {
                for (int x = 0;x<this.tetriminoActuelle.largeur() && peuDescendre;x++)
                {
                    if (this.tetriminoActuelle.faitPartiDeLaForme(x,y))
                    {
                        if (y == tetriminoActuelle.hauteur()-1)
                        {
                            if (!caseEstVide(pos.getX()+x,ligneEnDessousDuTetrimino))
                                peuDescendre = false;
                        }
                        else
                        {
                            if (!tetriminoActuelle.faitPartiDeLaForme(x,y+1) && !caseEstVide(pos.getX()+x,ligneEnDessousDuTetrimino))
                                peuDescendre = false;
                        }
                    }
                }
                ligneEnDessousDuTetrimino--;
            }
        }
        else
        {
            peuDescendre = false;
        }

        return  peuDescendre;
    }

    private void descendreTetrimino()
    {
        effaceTetrimino();
        this.tetriminoActuelle.descend();
        dessineTetrimino();
    }


    public boolean peuSeDeplacerAdroite()
    {
        boolean peuSeDeplacerAdroite = true;
        Position pos = this.tetriminoActuelle.getPosition();

        int coteDroite = pos.getX() + this.tetriminoActuelle.largeur();


        if (coteDroite < LARGEUR)
        {
            for (int y = 0;y < this.tetriminoActuelle.hauteur() && peuSeDeplacerAdroite;y++)
            {
                for (int x = this.tetriminoActuelle.largeur()-1;x >= 0 && peuSeDeplacerAdroite;x--)
                {
                    if (this.tetriminoActuelle.faitPartiDeLaForme(x,y))
                    {
                        if (x == this.tetriminoActuelle.largeur()-1)
                        {
                            if (!caseEstVide(coteDroite,pos.getY()+y))
                                peuSeDeplacerAdroite = false;
                        }
                        else
                        {
                            if (!this.tetriminoActuelle.faitPartiDeLaForme(x+1,y) && !caseEstVide(pos.getX()+x+1, pos.getY()+y))
                                peuSeDeplacerAdroite = false;
                        }
                    }
                }

            }
        }
        else
        {
            peuSeDeplacerAdroite = false;
        }

        return  peuSeDeplacerAdroite;
    }
    public void seDeplacerAdroite()
    {
        effaceTetrimino();
        this.tetriminoActuelle.seDeplaceAdroite();
        dessineTetrimino();
    }

    public boolean peuSeDeplacerAgauche()
    {
        boolean peuSeDeplacerAgauche = true;
        Position pos = this.tetriminoActuelle.getPosition();
        int coteGauche = pos.getX();

        if (coteGauche > 0)
        {
            coteGauche--;
            for (int y = 0;y < this.tetriminoActuelle.hauteur() && peuSeDeplacerAgauche;y++)
            {
                for (int x = 0;x < this.tetriminoActuelle.largeur() && peuSeDeplacerAgauche;x++)
                {
                    if (this.tetriminoActuelle.faitPartiDeLaForme(x,y))
                    {
                        if (x == 0)
                        {
                            if (!caseEstVide(coteGauche,pos.getY()+y))
                                peuSeDeplacerAgauche = false;
                        }
                        else
                        {
                            if (!tetriminoActuelle.faitPartiDeLaForme(x-1,y) && !caseEstVide(pos.getX()+x-1, pos.getY()+y))
                                peuSeDeplacerAgauche = false;
                        }
                    }
                }
            }
        }
        else
        {
            peuSeDeplacerAgauche = false;
        }

        return  peuSeDeplacerAgauche;
    }
    public void seDeplacerAGauche()
    {
        effaceTetrimino();
        this.tetriminoActuelle.seDeplaceAgauche();
        dessineTetrimino();
    }



    // S'OCCUPE DES ROTATION
    public boolean peuFaireUneRotation()
    {
        int[][] formeSuivante = this.tetriminoActuelle.formeSuivante();
        Position pos = this.tetriminoActuelle.getPosition();
        boolean peuFaireUneRotation = true;

        int limiteGauche = pos.getX();
        int limiteDroite = pos.getX() + formeSuivante[0].length-1;
        int limiteHaut = pos.getY();
        int limiteBas = pos.getY() + formeSuivante.length-1;

        effaceTetrimino();

        if (limiteGauche >= 0 && limiteDroite < LARGEUR && limiteBas < HAUTEUR && limiteHaut >= 0)
        {
            for (int y = 0;y < formeSuivante.length && peuFaireUneRotation;y++)
            {
                for (int x = 0; x < formeSuivante[0].length && peuFaireUneRotation; x++)
                {
                    if (formeSuivante[y][x] != 0 && !caseEstVide(pos.getX()+x,pos.getY()+y))
                        peuFaireUneRotation = false;
                }
            }
        }
        else
        {
            peuFaireUneRotation = false;
        }

        dessineTetrimino();

        return peuFaireUneRotation;
    }

    public void rotation()
    {
        effaceTetrimino();
        this.tetriminoActuelle.rotationDroite();
        dessineTetrimino();
    }


    // S'OCCUPE D'ACTUALISER LA PANNEAU D'INFORMATION
    private void actualiseShema()
    {
        this.info.nouveauTetrimino(this.tetriminoSuivant.shemaTetrimino());
    }
    private void ajoutScore(int ligne)
    {
        this.info.ajoutScore(ligne*125);
    }
}
*/