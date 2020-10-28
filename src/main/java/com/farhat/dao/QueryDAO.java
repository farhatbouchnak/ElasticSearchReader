package com.farhat.dao;
/*
 * Created by farhat
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farhat.entity.PayloadDocument;
import com.farhat.prop.ConfigProps;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QueryDAO {

	private final RestHighLevelClient client;
	private final SearchSourceBuilder sourceBuilder;
	private final ConfigProps props;
	private final Gson gson;
	private final ObjectMapper mapper = new ObjectMapper()
			.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
			.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

	@Autowired
	public QueryDAO(RestHighLevelClient client, SearchSourceBuilder sourceBuilder, ConfigProps props, Gson gson) {
		this.client = client;
		this.sourceBuilder = sourceBuilder;
		this.props = props;
		this.gson = gson;
	}

	/**
	 * @author farhat
	 * @param document
	 * @return String
	 */
	public String indexRequest(final PayloadDocument document) {

		try {

			final IndexRequest indexRequest = new IndexRequest(props.getIndex().getName())
					// .source(XContentType.JSON,docToString);
					.source(XContentType.JSON, 
							"after", document.getAfter(), 
							"before", document.getBefore(), 
							"source",document.getSource(), 
							"op", document.getOp(), 
							"ts_ms", document.getTs_ms(), 
							"transaction", document.getTransaction());
			final IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
			return response.getId();

		} catch (Exception ex) {
			log.error("The exception was thrown while inserting document into Index.", ex);
		}

		return null;
	}

	/** 
	 * @author farhat
	 * @return List<Document>
	 */
	public List<PayloadDocument> matchAllQuery() {

		List<PayloadDocument> result = new ArrayList<>();

		try {
			refreshRequest();
			result = getDocuments(QueryBuilders.matchAllQuery());
			return result;
		} catch (Exception ex) {
			log.error("The exception was thrown in searching for documents", ex);
		}

		return Collections.emptyList();
	}

	/**
	 * @author farhat
	 * @param query
	 * @return List<Document>
	 */
	public List<PayloadDocument> wildcardQuery(String query) {

		List<PayloadDocument> result = new ArrayList<>();

		try {
			result = getDocuments(QueryBuilders.queryStringQuery("*" + query.toLowerCase() + "*"));
			return result;
		} catch (Exception ex) {
			log.error("The exception was thrown in searching for documents by criteria ", ex);
		}

		return Collections.emptyList();
	}

	/**
	 *
	 * @return
	 */
	private SearchRequest getSearchRequest() {
		SearchRequest searchRequest = new SearchRequest(props.getIndex().getName());
		searchRequest.source(sourceBuilder);
		return searchRequest;
	}

	/**
	 * Generic method to retreive a list of documents from search request
	 * 
	 * @param builder
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	private List<PayloadDocument> getDocuments(AbstractQueryBuilder builder) {

		List<PayloadDocument> result = new ArrayList<>();

		try {
			sourceBuilder.query(builder);
			SearchRequest searchRequest = getSearchRequest();
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hit : searchHits) {
				JsonNode root = mapper.readTree(hit.getSourceAsString());
				PayloadDocument doc = mapper.treeToValue(root, PayloadDocument.class);
				result.add(doc);
			}
			return result;
		} catch (Exception e) {
			log.error("Can't get list of documents ", e);
		}

		return Collections.emptyList();
	}

	public void refreshRequest() throws IOException {
		final RefreshRequest refreshRequest = new RefreshRequest(props.getIndex().getName());
		client.indices().refresh(refreshRequest, RequestOptions.DEFAULT);
	}

	/**
	 *
	 * @param id
	 * @throws IOException
	 */
	public void deleteDocument(String id) {
		try {
			final DeleteRequest deleteRequest = new DeleteRequest(props.getIndex().getName(), id);
			client.delete(deleteRequest, RequestOptions.DEFAULT);
		} catch (Exception ex) {
			log.error("The exception was thrown in deleting document .", ex);
		}
	}

	/**
	 * Delete the entire index
	 * 
	 * @throws IOException
	 */
	public boolean deleteIndex() throws IOException {

		DeleteIndexRequest deleteIndexRequest = Requests.deleteIndexRequest(props.getIndex().getName());
		AcknowledgedResponse response = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
		return response.isAcknowledged();

	}

	public List<PayloadDocument> wildcardQueryByField(JsonNode after) {

		List<PayloadDocument> result = new ArrayList<>();

		try {
			QueryBuilder builder = QueryBuilders.nestedQuery("after",
					QueryBuilders.boolQuery()
							.must(QueryBuilders.termQuery("after.RI_COMPANY", after.get("RI_COMPANY").asText()))
							.must(QueryBuilders.termQuery("after.MNE_COMPANY", after.get("MNE_COMPANY").asText()))
							.must(QueryBuilders.termQuery("after.LIB_COMPANY", after.get("LIB_COMPANY").asText()))
							.must(QueryBuilders.termQuery("after.ACTIF", after.get("ACTIF").asInt()))
							.must(QueryBuilders.termQuery("after.KEY_BOOK", after.get("KEY_BOOK").asText()))
							.must(QueryBuilders.termQuery("after.KEY_BOOK_PERE", after.get("KEY_BOOK_PERE").asText()))
							.must(QueryBuilders.termQuery("after.NIVEAU", after.get("NIVEAU").asInt()))
							.must(QueryBuilders.termQuery("after.time_stamp", after.get("time_stamp").asText())),
					ScoreMode.None);
			BoolQueryBuilder filter = new BoolQueryBuilder().should(builder);
			result = getDocuments(filter);// QueryBuilders.multi//matchQuery("after", after));
			return result;
		} catch (Exception ex) {
			log.error("The exception was thrown in searching for documents by criteria ", ex);
		}
		return null;
	}
}
