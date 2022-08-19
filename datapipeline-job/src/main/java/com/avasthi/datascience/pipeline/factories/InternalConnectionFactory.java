package com.avasthi.datascience.pipeline.factories;

import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinition;
import com.avasthi.datascience.pipeline.common.pojos.InputSource;
import org.apache.spark.sql.DataFrameReader;

public class InternalConnectionFactory {
    static DataFrameReader dbSettings(InputSource inputSource, DatasetDefinition datasetDefinition, DataFrameReader dataFrameReader) {
        return dataFrameReader;
    }
}
