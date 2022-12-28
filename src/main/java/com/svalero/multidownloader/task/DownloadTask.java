package com.svalero.multidownloader.task;

import com.svalero.multidownloader.controller.DownloadController;
import javafx.concurrent.Task;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.Instant;

public class DownloadTask extends Task<Integer> {

    private URL url;
    private File file;

    private JSONArray message = new JSONArray();


    private static final Logger logger = LogManager.getLogger(DownloadController.class);

    public DownloadTask(String urlText, File file) throws MalformedURLException {
        this.url = new URL(urlText);
        this.file = file;
    }

    @Override
    protected Integer call() throws Exception {
        logger.trace("Descarga " + url.toString() + " iniciada");
        updateMessage("Conectando con el servidor . . .");

        URLConnection urlConnection = url.openConnection();
        double fileSize = urlConnection.getContentLength();
        double fileSizeMb = fileSize / 1048576;
        message.put(0,"/" + Math.round(fileSizeMb * 100) / 100d + "Mb");

        // Descomentar para limitar el tamaño de la descarga.
        /*double megaSize = fileSize / 1048576;
        if (megaSize > 10){
            logger.trace("Máximo tamaño de fichero alcanzado");
            throw new Exception("Max. size");
        }*/

        //Guardamos la descarga en el buffer
        BufferedInputStream in = new BufferedInputStream(url.openStream());

        //Archivo donde guardamos la descarga

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte dataBuffer[] = new byte[4096];
        int bytesRead;
        int totalRead = 0;
        double downloadProgress = 0;


        while ((bytesRead = in.read(dataBuffer)) != -1){

            // Comentar para acelerar la descarga.
            //Thread.sleep(1);

            //Escribe la secuencia de bytes en el archivo local

            fileOutputStream.write(dataBuffer, 0, bytesRead);

            //Actualizamos el total leido

            totalRead += bytesRead;

            //Progreso de la descarga
            downloadProgress = Math.round(((double) totalRead / fileSize) * 100) / 100d;
            double totalReadMb = totalRead / 1048576;
            message.put(1,totalReadMb);
            message.put(2,downloadProgress * 100 + " %");
            updateProgress(downloadProgress, 1);
            updateMessage(String.valueOf(message));

            if(isCancelled()) {
                logger.info("Descarga " + url.toString() + " cancelada.");
                updateProgress(0, 0);
                message.put(2, "Cancelada");
                updateMessage(String.valueOf(message));
                return null;
            }
        }

        updateProgress(1, 1);
        updateMessage("100 %");

        logger.trace("Descarga " + url.toString() + " finalizada");

        return null;
    }
}
