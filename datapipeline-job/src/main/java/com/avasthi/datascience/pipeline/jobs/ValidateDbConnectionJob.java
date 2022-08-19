package com.avasthi.datascience.pipeline.jobs;

import com.avasthi.datascience.pipeline.common.pojos.InputSource;
import com.avasthi.datascience.pipeline.common.pojos.JobResponse;
import com.avasthi.datascience.pipeline.spark.SparkContextManager;
import com.avasthi.datascience.pipeline.utils.DatapipelineClient;
import okhttp3.OkHttpClient;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class ValidateDbConnectionJob
{
    public static void main( String[] args ) throws IOException {

        if (args.length < 2) {
            throw new IllegalArgumentException("Job received zero argument. Needs to have five arguments in any job.");
        }
        String sparkMaster = args[0];
        String baseUrl = args[1];
        String jobId = args[2];
        String executorMemory = args[3];
        String driverMemory = args[4];
        OkHttpClient client = new OkHttpClient();
        JobResponse response = DatapipelineClient.getJobDetails(client, baseUrl, jobId);
        InputSource inputSource = DatapipelineClient.getInputSource(client, baseUrl, response.getContext().getEntityId().toString());
        SparkContextManager scm = new SparkContextManager(ValidateDbConnectionJob.class.getCanonicalName(), sparkMaster);

        SparkSession session = scm.createSQLContext(executorMemory, driverMemory);

        System.out.println(inputSource.toString());
        Dataset<Row> dataSetRow = scm.checkConnection(session, inputSource);
        StructType st = dataSetRow.schema();
        System.out.println("SQL is " +st.sql());
        dataSetRow.show();
        System.out.println("Number of arguments are " + args.length);
        System.out.println( "Hello World!" );
    }
}
