package com.avasthi.datascience.pipeline.spark;

import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinition;
import com.avasthi.datascience.pipeline.common.pojos.DatasetType;
import com.avasthi.datascience.pipeline.common.pojos.InputSource;
import com.avasthi.datascience.pipeline.common.pojos.InputSourceType;
import com.avasthi.datascience.pipeline.factories.ConnectionFactory;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;

public class SparkContextManager {

    @Data
    @Builder
    public static class Dummy {

        private int data;
    }
    private final String appName;
    private final String sparkHost;
    public SparkContextManager(String appName, String sparkHost) {
        this.appName = appName;
        this.sparkHost = sparkHost;
    }
    public SparkSession createSQLContext(String executorMemory, String driverMemory) {
        SparkSession spark = SparkSession.builder()
                .config("spark.executor.memory", executorMemory)
                .config("spark.driver.memory", driverMemory)
                .master(this.sparkHost)
                .appName(this.appName)
                .getOrCreate();
        return spark;
    }
    public Dataset<Row> checkConnection(SparkSession session, InputSource inputSource) {
        if(inputSource.getType().equals(InputSourceType.INTERNAL)) {
            return session.createDataFrame(Arrays.asList(Dummy.builder().data(1).build()), Dummy.class);
        }
        else {

            return ConnectionFactory.dbSettings(inputSource, DatasetDefinition.builder()
                                    .tableName("(select 1) validate")
                                    .build(),
                            session.read())
                    .load();
        }
    }
    public Dataset<Row> executeQuery(SparkSession session, DatasetDefinition datasetDefinition) {
        if (!CollectionUtils.isEmpty(datasetDefinition.getDependentOn())) {
            for (DatasetDefinition dd : datasetDefinition.getDependentOn()) {
                executeQuery(session, dd);
            }
        }
        System.out.println("Executing query for dataset " + datasetDefinition);
        if (datasetDefinition.getType().equals(DatasetType.PRIMARY)) {

            // Derived datasets don't have a backing sql table with them. By now all the dependent tables should have
            // a view created for them. So we can just call a sql here.
            ConnectionFactory.dbSettings(datasetDefinition.getInputSource(), datasetDefinition, session.read())
                    .load()
                    .createOrReplaceTempView(datasetDefinition.getName());
        }
        else {
            session.sql(datasetDefinition.getQuery()).createOrReplaceTempView(datasetDefinition.getName());
        }
        Dataset<Row> rowDataset =  session.sql(datasetDefinition.getQuery());
        System.out.println("Execution complete for query " + datasetDefinition.getQuery() + " " + rowDataset.schema());
        return rowDataset;
    }
}
