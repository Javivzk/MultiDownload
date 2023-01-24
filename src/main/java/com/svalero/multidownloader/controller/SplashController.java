package com.svalero.multidownloader.controller;

import com.svalero.multidownloader.util.R;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView image;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new SplashScreen().start();
    }

    class SplashScreen extends Thread{
        public void run(){
            try {
                InputStream stream = new FileInputStream("src/main/resources/images/splashScreen.jpg");
                Image splashScreenImage = new Image(stream);
                image.setImage(splashScreenImage);
                Thread.sleep(3000);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(R.getUI("multidownload-2.fxml"));
                        loader.setController(new AppController());
                        VBox vbox = null;
                        try {
                            vbox = loader.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Scene scene = new Scene(vbox);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setTitle("Multidownloader");
                        stage.show();

                        rootPane.getScene().getWindow().hide();
                    }
                });


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
