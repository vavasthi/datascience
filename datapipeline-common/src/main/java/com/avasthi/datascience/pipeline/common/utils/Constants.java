package com.avasthi.datascience.pipeline.common.utils;

public class Constants {

    public static class Endpoints {

        public static class V1 {

            private static final String VERSION = "/v1";
            public static final String INPUT_SOURCE_ENDPOINT = VERSION + "/inputsource";
            public static final String DATASET_ENDPOINT = VERSION + "/dataset";
            public static final String DATASET_SCHEMA_ENDPOINT = VERSION + "/dataset/{datasetId}/schema";
            public static final String JOBS_ENDPOINT = VERSION + "/job";
        }
    }
    public static class Times {
        public static final int ONE_MINUTE = 60;
        public static final int FIVE_MINUTE = 5 * 60;
        public static final int TEN_MINUTE = 10 * 60;
        public static final int HALF_HOUR = 30 * 60;
        public static final int ONE_HOUR = 60 * 60;
        public static final int ONE_DAY = 24 *60 * 60;
        public static final int NEVER_EXPIRE = -1;
        public static final String DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    }
    public static class CACHES {
        public static final String INPUT_SOURCE_CACHE_NAME = "INPUT_SOURCE_CACHE_NAME";
        public static final String INPUT_SOURCE_CACHE_PREFIX = "INPUT_SOURCE_CACHE_PREFIX";

        public static final String DATASET_CACHE_NAME = "DATASET_CACHE_NAME";
        public static final String DATASET_CACHE_PREFIX = "DATASET_CACHE_PREFIX";

        public static final String DATASET_SCHEMA_CACHE_NAME = "DATASET_SCHEMA_CACHE_NAME";
        public static final String DATASET_SCHEMA_CACHE_PREFIX = "DATASET_SCHEMA_CACHE_PREFIX";

        public static final String JOBS_CACHING_NAME = "JOBS_CACHING_NAME";
        public static final String JOBS_CACHING_PREFIX = "JOBS_CACHING_PREFIX";
    }
    public static class HEADERS {
        public static final String LOCATION_HEADER = "Location";
        public static final  String RETRY_AFTER_HEADER = "Retry-AFter";
        public static final int DEFAULT_RETRY_TIMEOUT = 1;
    }
    public static class JOBS {
        public static final String VALIDATE_DB_CONNECTION = "com.avasthi.datascience.pipeline.jobs.ValidateDbConnectionJob";
        public static final String VALIDATE_DATASET_QUERY = "com.avasthi,datascience.apipeline.jobs.ValidateDatasetQuery";
    }
}
