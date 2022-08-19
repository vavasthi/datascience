package com.avasthi.datascience.pipeline.utils;

import com.avasthi.datascience.pipeline.common.pojos.*;
import com.avasthi.datascience.pipeline.common.utils.Constants;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatapipelineClient {

    private static MediaType jsonType = MediaType.parse("application/json; charset=utf-8");

    public static JobResponse getJobDetails(OkHttpClient client, String baseUrl, String jobId) throws IOException {

        Request request = new Request.Builder()
                .url(url(baseUrl, Constants.Endpoints.V1.JOBS_ENDPOINT, jobId))
                .get()
                .build();
        Gson gson = gson();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        System.out.println("JSSSSON The response is "+ json);
        return gson.fromJson(json, JobResponse.class);
    }
    public static InputSource getInputSource(OkHttpClient client, String baseUrl, String entityId) throws IOException {

        Request request = new Request.Builder()
                .url(url(baseUrl, Constants.Endpoints.V1.INPUT_SOURCE_ENDPOINT, entityId))
                .get()
                .build();
        Gson gson = gson();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        System.out.println("JSSSSON The response is "+ json);
        return gson.fromJson(json, InputSource.class);
    }
    public static DatasetDefinition getDataset(OkHttpClient client, String baseUrl, String entityId) throws IOException {

        Request request = new Request.Builder()
                .url(url(baseUrl, Constants.Endpoints.V1.DATASET_ENDPOINT, entityId))
                .get()
                .build();
        Gson gson = gson();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        return gson.fromJson(json, DatasetDefinition.class);
    }
    public static List<DatasetDefinitionSchema> createDatasetDefinitionSchema(OkHttpClient client, String baseUrl, String entityId, List<DatasetDefinitionSchemaCreatePojo> ddsl) throws IOException {

        Request request = new Request.Builder()
                .url(url(baseUrl, Constants.Endpoints.V1.DATASET_SCHEMA_ENDPOINT.replace("{datasetId}", entityId)))
                .post(RequestBody.create(jsonType, gson().toJson(ddsl)))
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        System.out.println("JSSSSON The response is "+ json);
        Type listOfDataDefinitionSchema = new TypeToken<ArrayList<DatasetDefinitionSchema>>() {}.getType();
        List<DatasetDefinitionSchema> responseList = gson().fromJson(json, listOfDataDefinitionSchema);
        return responseList;
    }
    public static JobResponse addJobLog(OkHttpClient client, String baseUrl, String jobId, List<String> logs) throws IOException {

        Request request = new Request.Builder()
                .url(url(baseUrl, Constants.Endpoints.V1.JOBS_ENDPOINT+"/%s/log", jobId))
                .post(RequestBody.create(jsonType, gson().toJson(logs)))
                .build();
        Gson gson = gson();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        return gson.fromJson(json, JobResponse.class);
    }
    private static Gson gson() {
        return new GsonBuilder()
                .setDateFormat(Constants.Times.DATE_FORMAT)
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        SimpleDateFormat sdf = new SimpleDateFormat(Constants.Times.DATE_FORMAT);
                        try {
                            return sdf.parse(jsonElement.toString());
                        } catch (ParseException e) {
                        }
                        return new Date();
                    }
                })
                .create();
    }
    public static String url(String baseUrl, String endpoint, String id) {
        return String.format("%s%s/%s", baseUrl, endpoint, id);
    }
    public static String url(String baseUrl, String endpoint) {
        return String.format("%s%s", baseUrl, endpoint);
    }
}
