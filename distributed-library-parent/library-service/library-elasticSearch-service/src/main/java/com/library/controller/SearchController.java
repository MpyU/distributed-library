package com.library.controller;

import com.github.pagehelper.PageInfo;
import com.library.pojo.Book;
import com.library.pojo.Result;
import com.library.utils.ConvertJsonToBean;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/search")
public class SearchController {

   @Autowired
   private RestHighLevelClient restHighLevelClient;

   @Autowired
   private RestTemplate restTemplate;

   @Autowired
   private ElasticsearchTemplate template;

    @GetMapping("/create")
    public String create() throws IOException {

        Result bookResult = restTemplate.getForObject("http://book-service/book/select/10/1", Result.class);
        PageInfo<Book> pageInfo = ConvertJsonToBean.convertMapToBean((Map<String, Object>) bookResult.getData(), PageInfo.class);
        List books = pageInfo.getList();
        List<IndexQuery> indexQueries = new ArrayList<>();
        List<Book> list = new ArrayList<>();

        for(int i = 0;i<books.size();i++){
            Map<String,Object> map = (Map<String, Object>) books.get(i);
            Book book = ConvertJsonToBean.convertMapToBean(map,Book.class);
//            list.add(book);
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(book.getId() + "");
            indexQuery.setObject(book);
            indexQuery.setIndexName("book_index");
            indexQuery.setType("book");
            indexQueries.add(indexQuery);
        }

        if(indexQueries.size() > 0){
            template.bulkIndex(indexQueries);
        }
        return "success";
    }

    @GetMapping("/get")
    public Object get(String string) throws IOException {

        HighlightBuilder.Field context = new HighlightBuilder.Field("bookName").requireFieldMatch(false)
                .preTags("<span style='color:red;'>")
                .postTags("</span>");

        SearchQuery query = new NativeSearchQueryBuilder()
                .withIndices("book_index")
                .withTypes("book")
                .withQuery(QueryBuilders.queryStringQuery(string).field("bookName"))
                .withPageable(PageRequest.of(0,6))
                .withHighlightFields(context)
                .build();

        template.queryForPage(query, Book.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHits hits = searchResponse.getHits();
                System.out.println(hits.totalHits);
                SearchHit[] hits1 = hits.getHits();
                for(SearchHit searchHit : hits1){
                    Map map = mapSearchHit(searchHit, Map.class);
                    System.out.println(map);
                }
                System.out.println(hits1);
                return null;
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                return (T) searchHit.getSourceAsMap();
            }
        });

        return "success";
    }


}
