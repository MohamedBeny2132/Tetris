package com.example.Tetrimino.View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainView extends Application
{

    @Override
    public void start(Stage stage) throws Exception
    {
        stage.setTitle("Tetris v1 - Gafarixo");
        stage.setHeight(659);
        stage.setWidth(530);

        TableauTetris jeu = new TableauTetris(new InformationJeuEnCour());
        Scene scene = new Scene(jeu);





        scene.setOnKeyPressed(e->{
            switch (e.getCode())
            {
                case LEFT:
                    if (jeu.peuSeDeplacerAgauche())
                        jeu.seDeplacerAGauche();
                    break;
                case RIGHT:
                    if (jeu.peuSeDeplacerAdroite())
                        jeu.seDeplacerAdroite();
                    break;
                case UP:
                    if (jeu.peuFaireUneRotation())
                        jeu.rotation();
                    break;
                case DOWN:
                    jeu.joue();
                    break;
            }
        });


        stage.setScene(scene);
        jeu.start();
        stage.show();
    }
}
