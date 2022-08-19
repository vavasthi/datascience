package com.avasthi.jobsystem.runnable;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Log4j2
public class InputStreamReaderRunnable implements Runnable {

    private BufferedReader reader;

    private String name;

    public InputStreamReaderRunnable(InputStream is, String name) {
        this.reader = new BufferedReader(new InputStreamReader(is));
        this.name = name;
    }

    public void run() {
        try {
            String line = reader.readLine();
            while (line != null) {
                log.log(Level.INFO, line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}