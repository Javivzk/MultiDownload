package com.svalero.multidownloader.controller;

import com.svalero.multidownloader.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AppController {

    public TextField tfUrl;
    public TextField timerInput;

    public Button btDownload;
    public TabPane tpDownloads;

    private Map<String, DownloadController> allDownloads;

    String logPath = "C:\\myFolder\\myFile.txt";


    public AppController() {
        allDownloads = new HashMap<>();
    }

    //Boton de descarga
    @FXML
    public void startDownload() {
        String url = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();
        launch(url);
    }

    // Descargar desde DLC
    @FXML
    public void launchFileDownload(ActionEvent event) {
        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();
        readDLC();
    }

    // Parar todas las descargas
    @FXML
    public void stopAllDownloads() {
        for (DownloadController downloadController : allDownloads.values())
            downloadController.stop();
    }

    // Carga de la ventana de descarga
    private void launch(String url) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUI("download.fxml"));

            DownloadController downloadController = new DownloadController(url, Integer.parseInt(timerInput.getText()));
            loader.setController(downloadController);
            VBox downloadBox = loader.load();

            String filename = url.substring(url.lastIndexOf("/") + 1);
            tpDownloads.getTabs().add(new Tab(filename, downloadBox));

            allDownloads.put(url, downloadController);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // Abrir Log
    @FXML
    public void openLog(ActionEvent actionEvent) throws IOException, IllegalArgumentException {
        if (Desktop.isDesktopSupported()) {
            try {
                File file = new File("log" + File.separator + "multidownload.log");
                Desktop.getDesktop().open(file);
            } catch (IOException ioe) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ha habido un error.");
                alert.show();
            } catch (IllegalArgumentException iae){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ha habido un error al abrir el log. Es posible que no exista.");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No soportado.");
            alert.show();
        }

    }

    // Descarga desde fichero elegible
    @FXML
    public void readDLC() {

        try {
            // Quitar comentario para descarga desde fichero "dlc.txt".
            //File dlcFile = new File("dlc.txt");

            // Quitar comentario para descarga desde el fichero que el usuario seleccione.
            FileChooser fileChooser = new FileChooser();
            File dlcFile = fileChooser.showOpenDialog(tfUrl.getScene().getWindow());
            if (dlcFile == null)
                return;

            // Leer fichero
            Scanner reader = new Scanner(dlcFile);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                System.out.println(data);
                // Lanzar controlador y descarga
                launch(data);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Se ha producido un error");
            e.printStackTrace();
        }

    }
}
