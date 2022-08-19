package com.avasthi.datascience.pipeline.jobs;

import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinition;
import com.avasthi.datascience.pipeline.common.pojos.InputSource;
import com.avasthi.datascience.pipeline.common.pojos.JobResponse;
import com.avasthi.datascience.pipeline.common.utils.DependencyUtils;
import com.avasthi.datascience.pipeline.spark.SparkContextManager;
import com.avasthi.datascience.pipeline.utils.DatapipelineClient;
import okhttp3.OkHttpClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.io.IOException;
import java.util.*;

/**
 * Hello world!
 *
 */
public class ValidateDatasetQuery
{
    public static void main( String[] args ) throws IOException {

        if (args.length < 5) {
            throw new IllegalArgumentException("Job received zero argument. Needs to have five arguments in any job.");
        }
        String sparkMaster = args[0];
        String baseUrl = args[1];
        String jobId = args[2];
        String executorMemory = args[3];
        String driverMemory = args[4];
        System.out.println(String.format("Arguments received in the job %s %s %s", sparkMaster, baseUrl, jobId));
        OkHttpClient client = new OkHttpClient();
        JobResponse response = DatapipelineClient.getJobDetails(client, baseUrl, jobId);
        DatasetDefinition dataset = DatapipelineClient.getDataset(client, baseUrl, response.getContext().getEntityId().toString());
        if (validateDatasetDefinition(dataset)) {

            InputSource inputSource = DatapipelineClient.getInputSource(client, baseUrl, dataset.getInputSourceId().toString());
            SparkContextManager scm = new SparkContextManager(ValidateDatasetQuery.class.getCanonicalName(), sparkMaster);
            SparkSession session = scm.createSQLContext(executorMemory, driverMemory);

            Dataset<Row> dataSetRow = scm.executeQuery(session, dataset);
            if (dataSetRow.isEmpty()) {
                System.out.println("OUTPUT RDD is empty.");
            }
            else {

                dataSetRow.limit(10).show();
            }
/*            StructType st = dataSetRow.schema();
            List<DatasetDefinitionSchemaCreatePojo> ddscl = Arrays.stream(st.fields()).map(f -> DatasetDefinitionSchemaCreatePojo.builder()
                    .datasetId(dataset.getId())
                    .dataType(f.dataType().toString())
                    .metadata(f.metadata().toString())
                    .name(f.name())
                    .build()).collect(Collectors.toList());
            List<DatasetDefinitionSchema> ddsl = DatapipelineClient.createDatasetDefinitionSchema(client, baseUrl, dataset.getId().toString(), ddscl);
            System.out.println(String.format("Dataset validated. Schema Updated. Schema elements = %s", ddsl.toString()));*/
        }
        else {
            DatapipelineClient.addJobLog(client, baseUrl, jobId, Arrays.asList(String.format("Dataset %s could not be validated", dataset.getId().toString())));
            throw new IllegalArgumentException(String.format("Dataset %s could not be validated", dataset.getId().toString()));
        }
    }

    private static boolean validateDatasetDefinition(DatasetDefinition dd) {
        createGraph(dd);
        return true;
    }
    public static Graph<DependencyUtils.Node, DefaultEdge> createGraph( DatasetDefinition dd) {

        Graph<DependencyUtils.Node, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        DependencyUtils.Node root = new DependencyUtils.Node(dd.getId());
        graph.addVertex(root);
        return createGraph(graph, root, dd);
    }
    public static Graph<DependencyUtils.Node, DefaultEdge> createGraph(Graph<DependencyUtils.Node, DefaultEdge> graph, DependencyUtils.Node node, DatasetDefinition dd) {
        if (!CollectionUtils.isEmpty(dd.getDependentOn())) {
            for (DatasetDefinition newdd : dd.getDependentOn()) {
                DependencyUtils.Node newNode = new DependencyUtils.Node(newdd.getId());
                graph.addVertex(newNode);
                if (graph.addEdge(node, newNode) == null) {
                    throw new IllegalArgumentException(String.format("Addition of end between %s amd %s is causing a cyclic graph", dd.getId(), newdd.getId()));
                }
                createGraph(graph, newNode, newdd);
            }
        }
        return graph;
    }
}
