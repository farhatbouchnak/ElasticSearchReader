package com.farhat.config;

import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.farhat.prop.ConfigProps;
import com.google.gson.Gson;

@Configuration
public class Config {

    private final ConfigProps props;

    public Config(final ConfigProps props){
        this.props = props;
    }

    @Bean(destroyMethod = "close")
    public RestHighLevelClient getRestClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(props.getClients().getHostname(),
                props.getClients().getHttpPort(), props.getClients().getScheme())));
    }

    @Bean
    public SearchSourceBuilder getSearchSourceBuilder(){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(props.getIndex().getFrom());
        sourceBuilder.size(props.getIndex().getSize());
        sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        sourceBuilder.timeout(new TimeValue(props.getIndex().getTimeout(), TimeUnit.SECONDS));

        return sourceBuilder;
    }

    @Bean
    public Gson getGson(){
        return new Gson();
    }
}
