package com.innominds.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.innominds.TestDataGenerator;
import com.innominds.configuration.ElasticSearchClient;
import com.innominds.model.Patient;
import com.innominds.service.PatientService;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    ElasticSearchClient client;

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    @Override
    public void createIndex(String indexName) {
        CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
        indexRequest.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
        );
        try {
            if (!isIndexExists(indexName)) {
                client.getElasticClient().indices()
                        .create(indexRequest, RequestOptions.DEFAULT);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String createIndexWithMappings(String indexName) {
        try {
            String initialIndex = indexName;
            String currentTime = dateFormat.format(new Date());
            System.out.println("Time Formatted : " + currentTime);
            indexName = indexName + "_" + currentTime;
            CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
            String message = "{\"properties\":{\"id\":{\"type\":\"long\"},\"name\":{\"type\":\"text\"" +
                    ",\"fields\":{\"nameLower\":{\"type\":\"keyword\",\"normalizer\":\"lowercase\"}," +
                    "\"keyword\":{\"type\":\"keyword\"}}},\"age\":{\"type\":\"integer\"},\"address\"" +
                    ":{\"type\":\"text\"},\"prescriptions\":{\"type\":\"nested\",\"properties\":{\"id\":" +
                    "{\"type\":\"long\"},\"date\":{\"type\":\"text\"},\"bloodPressure\":{\"type\":\"text\"}," +
                    "\"weight\":{\"type\":\"float\"},\"temperature\":{\"type\":\"float\"},\"diagnosis\":" +
                    "{\"type\":\"text\"},\"medications\":{\"type\":\"nested\",\"properties\":{\"code\":" +
                    "{\"type\":\"text\"},\"name\":{\"type\":\"text\"},\"numberOfUnits\":{\"type\":\"long\"}," +
                    "\"numberOfDays\":{\"type\":\"long\"},\"daySchedule\":{\"type\":\"text\"}}}}}}}";
            XContentBuilder b;
            b = XContentFactory.jsonBuilder().prettyPrint();
            try (XContentParser p = XContentFactory.xContent(XContentType.JSON)
                    .createParser(NamedXContentRegistry.EMPTY,
                            DeprecationHandler.IGNORE_DEPRECATIONS, message)) {
                b.copyCurrentStructure(p);
            }
            indexRequest.mapping(b);

            if (!isIndexExists(indexName)) {
                System.out.println("Creating elastic index : " + indexName + " ......");
                client.getElasticClient().indices()
                        .create(indexRequest, RequestOptions.DEFAULT);
            }

            IndicesAliasesRequest request = new IndicesAliasesRequest();
            IndicesAliasesRequest.AliasActions aliasAction =
                    new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.
                            AliasActions.Type.ADD)
                            .index(indexName)
                            .alias(initialIndex);
            request.addAliasAction(aliasAction);
            client.getElasticClient().indices().updateAliases(request, RequestOptions.DEFAULT);
            GetAliasesRequest requestWithAlias = new GetAliasesRequest(initialIndex);
            GetAliasesResponse response = client.getElasticClient().indices()
                    .getAlias(requestWithAlias, RequestOptions.DEFAULT);
            Set<String> aliasIndexes = response.getAliases().keySet();
            if(aliasIndexes.size() > 0) {
                Optional<String> first = aliasIndexes.stream().findFirst();
                String indexToBeDeletedFromAlias = first.get();
                IndicesAliasesRequest deleteIndexRequest = new IndicesAliasesRequest();
                IndicesAliasesRequest.AliasActions deleteAliasAction =
                        new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.
                                AliasActions.Type.REMOVE)
                                .index(indexToBeDeletedFromAlias)
                                .alias(initialIndex);
                deleteIndexRequest.addAliasAction(deleteAliasAction);
                client.getElasticClient().indices().updateAliases(deleteIndexRequest, RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return indexName;
    }

    @Override
    public Long addPatient(Patient patient) {
        try {
            Map<String, Object> jsonMap = new LinkedHashMap<>();
            jsonMap.put("id", patient.getId());
            jsonMap.put("name", patient.getName());
            jsonMap.put("age", patient.getAge());
            jsonMap.put("address", patient.getAddress());
            IndexRequest indexRequest = new IndexRequest("my_patients")
                    .id(String.valueOf(patient.getId())).source(jsonMap);
            client.getElasticClient().index(indexRequest,
                    RequestOptions.DEFAULT);
            System.out.println("Document created with document id : " + patient.getId());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return patient.getId();
    }

    @Override
    public List<Patient> getPatients(Integer limit, String sortField, String sortOrder) {
        List<Patient> patients = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.size(limit);
        SortBuilder<FieldSortBuilder> sortBuilder = SortBuilders.fieldSort(sortField).order
                (SortOrder.valueOf(sortOrder));
        searchSourceBuilder.sort(sortBuilder);
        SearchRequest searchRequest = new SearchRequest("my_patients");
        searchRequest.source(searchSourceBuilder);
        SearchResponse search;
        try {
            search = client.getElasticClient().search(searchRequest, RequestOptions.DEFAULT);
            if (search.getHits().getHits().length > 0) {
                List<Patient> list = Arrays.stream(search.getHits().getHits()).map(searchHit -> {
                    try {
                        return mapper.readValue(searchHit.getSourceAsString(), Patient.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
                patients.addAll(list);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return patients;
    }

    @Override
    public Patient getPatientByName(String name, boolean isSensitive) {
        Patient patient = null;
        ObjectMapper mapper = new ObjectMapper();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        TermQueryBuilder nameQuery = QueryBuilders.termQuery("name.keyword", name);
        boolQueryBuilder.must(nameQuery);
        String[] excludes = {"address", "age"};
        if (isSensitive) {
            searchSourceBuilder.fetchSource(null, excludes);
        }
        searchSourceBuilder.query(boolQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("my_patients");
        searchRequest.source(searchSourceBuilder);
        SearchResponse search;
        try {
            search = client.getElasticClient().search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = search.getHits().getHits();
            if (hits.length > 0) {
                SearchHit hit = hits[0];
                patient = mapper.readValue(hit.getSourceAsString(), Patient.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return patient;
    }

    @Override
    public void updatePatientById(Integer patientId, String nameToBeUpdated) {
        UpdateRequest request = new UpdateRequest("my_patients", String.valueOf(patientId));
        Map<String, Object> parameters = singletonMap("name", nameToBeUpdated);
        Script inline = new Script(ScriptType.INLINE, "painless",
                "ctx._source.name = params.name", parameters);
        request.script(inline);
        try {
            UpdateResponse updateResponse = client.getElasticClient().update(
                    request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePatients(String searchByAddress, String addressToBeUpdated) {
        UpdateByQueryRequest request =
                new UpdateByQueryRequest("my_patients");
        Map<String, Object> parameters = singletonMap("address", addressToBeUpdated);
        request.setQuery(new MatchQueryBuilder("address", searchByAddress));
        request.setScript(
                new Script(
                        ScriptType.INLINE, "painless",
                        "ctx._source.prescriptions[0].medications[] = params.address",
                        parameters));
        try {
            client.getElasticClient().updateByQuery(
                    request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePatient(Integer patientId) {
        DeleteRequest request = new DeleteRequest("my_patients").id(patientId.toString());
        try {
            client.getElasticClient().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void createPatients(String indexName, Integer numOfPatients) {
        TestDataGenerator dataGenerator = new TestDataGenerator();
        List<Patient> patients = null;
        try {
            patients = dataGenerator.generatePatients(numOfPatients);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Size of the test content generated :: " + patients.size());
        Gson gson =new Gson();
        BulkRequest bulkRequest = new BulkRequest();
        patients.forEach(patient -> {
            IndexRequest indexRequest = new IndexRequest(indexName).id(patient.getId().toString())
                    .source(gson.toJson(patient), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        try {
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            BulkResponse bulkResponse = client.getElasticClient().bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isIndexExists(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        return client.getElasticClient().indices().exists(request, RequestOptions.DEFAULT);
    }

}
