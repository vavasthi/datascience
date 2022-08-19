package com.avasthi.jobsystem.handlers;

import com.avasthi.jobsystem.caching.JobsCachingService;
import com.avasthi.datascience.pipeline.common.pojos.JobContext;
import com.avasthi.datascience.pipeline.common.pojos.JobStatus;
import com.avasthi.jobsystem.runnable.InputStreamReaderRunnable;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.spark.launcher.SparkLauncher;

import java.io.IOException;

@Log4j2
public class SparkJobLauncherHandler implements AbstractJobHandler{

    public SparkJobLauncherHandler(String appResource, String mainClass, String sparkMaster) {
        this.appResource = appResource;
        this.mainClass = mainClass;
        this.sparkMaster = sparkMaster;
    }

    @Override
    public JobStatus handle(JobsCachingService cachingService, JobContext context, String... appArgs) {

        try {

            cachingService.addLog(context.getId(),
                    String.format("Job handler created with mainClass=%s on Spark Master= %s with App Resource= %s",
                            mainClass,
                            sparkMaster,
                            appResource));
            Process spark = new SparkLauncher()
                    .setAppResource(appResource)
                    .setMainClass(mainClass)
                    .setMaster(sparkMaster)
                    .addAppArgs(appArgs)
                    .setVerbose(true)
                    .launch();

            InputStreamReaderRunnable inputStreamReaderRunnable = new InputStreamReaderRunnable(spark.getInputStream(), "input");
            Thread inputThread = new Thread(inputStreamReaderRunnable, "LogStreamReader input");
            inputThread.start();

            InputStreamReaderRunnable errorStreamReaderRunnable = new InputStreamReaderRunnable(spark.getErrorStream(), "error");
            Thread errorThread = new Thread(errorStreamReaderRunnable, "LogStreamReader error");
            errorThread.start();
            cachingService.addLog(context.getId(), "Waiting for job to finish.");
            System.out.println("Waiting for finish...");
            int exitCode = spark.waitFor();
            cachingService.addLog(context.getId(), String.format("Job finished with error code %d.", exitCode));
            return exitCode == 0 ? JobStatus.SUCCESS : JobStatus.FAILURE;
        } catch (IOException|InterruptedException e) {
            cachingService.addLog(context.getId(), "Exception raised in launching of spark job.\n" + e.toString());
            log.log(Level.ERROR, "Exception raised in launching of spark job.", e);
        }
        return JobStatus.FAILURE;
    }
    private final String appResource;
    private final String mainClass;
    private final String sparkMaster;
}
