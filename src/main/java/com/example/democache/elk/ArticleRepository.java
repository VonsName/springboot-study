package com.example.democache.elk;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author ASUS
 */
public interface ArticleRepository  extends ElasticsearchRepository<Article,Integer> {
    /**
     * 自定义方法
     * @param name 参数
     * @return list
     */
    public List<Article> findArticleByName(String name);
}
