package com.avasthi.datascience.pipeline.factories;

import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinition;
import com.avasthi.datascience.pipeline.common.pojos.InputSource;
import org.apache.spark.sql.DataFrameReader;

public class ConnectionFactory {
    public static DataFrameReader dbSettings(InputSource inputSource, DatasetDefinition datasetDefinition, DataFrameReader dataFrameReader) {
        switch(inputSource.getType()) {
            case POSTGRESQL:
                return PostgresqlConnectionFactory.dbSettings(inputSource, datasetDefinition, dataFrameReader);
            case MYSQL:
                return MySQLConnectionFactory.dbSettings(inputSource, datasetDefinition, dataFrameReader);
            default:
                return InternalConnectionFactory.dbSettings(inputSource, datasetDefinition, dataFrameReader);
        }
    }
}
