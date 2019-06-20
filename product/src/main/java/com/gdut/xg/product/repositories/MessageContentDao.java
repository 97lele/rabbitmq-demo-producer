package com.gdut.xg.product.repositories;


import com.gdut.xg.product.entity.MessageContent;

import com.gdut.xg.product.util.Object2JsonUtil;
import com.google.gson.Gson;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.query.QuerySearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author lulu
 * @Date 2019/6/18 10:50
 */
@org.springframework.stereotype.Repository
public class MessageContentDao {
    @Value("${elk.index}")
    private String index;


    @Autowired
    private RestHighLevelClient client;

    public Boolean insert(MessageContent c) {
        try {
            IndexRequest i = new IndexRequest(index).id(c.getMessageId()).source(Object2JsonUtil.ObjectToMap(c));
            IndexResponse response = client.index(i, RequestOptions.DEFAULT);
            if (response.getResult() == DocWriteResponse.Result.CREATED || response.getResult() == DocWriteResponse.Result.UPDATED) {
                return true;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    public List<MessageContent> queryByRetryCount() {

        SearchRequest request = new SearchRequest(index);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //这里找出status是小于0即重投状态-1并且重投次数小于3的
        boolQueryBuilder.must(QueryBuilders.matchQuery("status", -1));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        request.searchType(SearchType.DEFAULT).source(searchSourceBuilder);
        try {
            List<MessageContent> messageContents = new ArrayList<>();
            for (SearchHit hit : client.search(request, RequestOptions.DEFAULT).getHits().getHits()) {
                MessageContent m = Object2JsonUtil.MapToObject(hit.getSourceAsMap(), MessageContent.class);
                messageContents.add(m);
            }
            return messageContents;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MessageContent queryById(String id) {
        GetRequest req = new GetRequest(index, id);
        try {
            GetResponse response = client.get(req, RequestOptions.DEFAULT);
            MessageContent m = Object2JsonUtil.MapToObject(response.getSourceAsMap(), MessageContent.class);
            m.setMessageId(id);
            return m;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean update(MessageContent c) {
        try {
            UpdateRequest u = new UpdateRequest(index, c.getMessageId()).doc(Object2JsonUtil.ObjectToMap(c));
            UpdateResponse r = client.update(u, RequestOptions.DEFAULT);
            if (r.getResult() == DocWriteResponse.Result.UPDATED) {
                return true;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean batchInsert(Collection<MessageContent> c) {
        BulkRequest bulkRequest = new BulkRequest();
        for (MessageContent m : c) {
            try {
                bulkRequest.add(new IndexRequest(index).id(m.getMessageId()).source(Object2JsonUtil.ObjectToMap(m)));
                BulkResponse r = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                if (r.hasFailures()) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }






 /*   ActionListener<BulkResponse> listener=new ActionListener<BulkResponse>() {
        @Override
        public void onResponse(BulkResponse bulkItemResponses) {

        }

        @Override
        public void onFailure(Exception e) {

        }
    };*/


}
