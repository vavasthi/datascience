package com.avasthi.datascience.pipeline.factories;

import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinition;
import com.avasthi.datascience.pipeline.common.pojos.InputSource;
import com.avasthi.datascience.pipeline.common.pojos.InputSourceType;
import org.apache.spark.sql.DataFrameReader;

public class PostgresqlConnectionFactory {
    static DataFrameReader dbSettings(InputSource inputSource, DatasetDefinition datasetDefinition, DataFrameReader dataFrameReader) {
        return dataFrameReader
                .format("jdbc")
                .option("url", url(inputSource.getType(), inputSource.getHostname(), inputSource.getPortNumber(), inputSource.getDbName()))
                .option("dbtable", datasetDefinition.getTableName())
                .option("user", inputSource.getUsername())
                .option("password", inputSource.getPassword())
                .option("driver", "org.postgresql.Driver");

    }
    static String url(InputSourceType dbtype, String hostname, int portNumber, String database) {
        System.out.println(dbtype);
        System.out.println(dbtype.name());
        System.out.println(dbtype.getConnType());
        System.out.println(hostname);
        System.out.println(portNumber);
        System.out.println(database);
        return String.format("jdbc:%s://%s:%d/%s", dbtype.getConnType(), hostname, portNumber, database);
    }
}
