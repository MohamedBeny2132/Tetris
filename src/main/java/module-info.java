module com.example.tetris {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tetris to javafx.fxml;

    exports com.example.Tetrimino.View;
    exports com.example.Tetrimino.Tetrimino;
    exports com.example.Tetrimino.Utilitaire;
}