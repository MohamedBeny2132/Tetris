package com.example.Tetrimino.View;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class InformationJeuEnCour extends Pane
{
    private Label labelScore;
    private int score;
    private Label tetriminoSuivant;

    private Pane shemaTetrimino;

    public InformationJeuEnCour()
    {
        this.score = 0;
        this.labelScore = new Label("Score : "+this.score);
        mettreAjourScore();
        this.tetriminoSuivant = new Label("Tetrimino suivant :");
        this.shemaTetrimino = new Pane();



        ajoutNode();
    }


    private void ajoutNode()
    {
        labelScore.setLayoutX(50);
        labelScore.setLayoutY(50);

        tetriminoSuivant.setLayoutX(50);
        tetriminoSuivant.setLayoutY(100);

        shemaTetrimino.setLayoutX(50);
        shemaTetrimino.setLayoutY(200);


        this.getChildren().addAll(labelScore,tetriminoSuivant,shemaTetrimino);
    }


    public void nouveauTetrimino(Pane shema) {
        this.getChildren().remove(this.shemaTetrimino); // Retirez l'ancien shema
        this.shemaTetrimino = shema; // Mettez à jour la référence
        shema.setLayoutX(50);
        shema.setLayoutY(200); // Assurez-vous que le nouveau shema est bien positionné
        this.getChildren().add(shema); // Ajoutez le nouveau shema au Pane
    }



    public void ajoutScore(int point)
    {
        this.score+=point;
        mettreAjourScore();
    }

    private void mettreAjourScore()
    {
        this.labelScore.setText("Score : "+this.score);
    }






}
