package com.avasthi.datascience.pipeline.jobs;

import com.avasthi.datascience.pipeline.runnable.InputStreamReaderRunnable;
import org.apache.spark.launcher.SparkLauncher;

import java.io.IOException;

public class JobLauncher {
    public static void main( String[] args ) throws IOException, InterruptedException {


        Process spark = new SparkLauncher()
                .setAppResource("/Users/vavasthi/jars/datapipeline-job-1.0-SNAPSHOT-jar-with-dependencies.jar")
                .setMainClass("com.avasthi.datascience.pipeline.jobs.JobMain")
                .setMaster("spark://192.168.1.105:7077")
                .launch();

        InputStreamReaderRunnable inputStreamReaderRunnable = new InputStreamReaderRunnable(spark.getInputStream(), "input");
        Thread inputThread = new Thread(inputStreamReaderRunnable, "LogStreamReader input");
        inputThread.start();

        InputStreamReaderRunnable errorStreamReaderRunnable = new InputStreamReaderRunnable(spark.getErrorStream(), "error");
        Thread errorThread = new Thread(errorStreamReaderRunnable, "LogStreamReader error");
        errorThread.start();

        System.out.println("Waiting for finish...");
        int exitCode = spark.waitFor();
        System.out.println("Finished! Exit code:" + exitCode);
    }
}
