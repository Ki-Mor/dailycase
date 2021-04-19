package com.es.demo.service;

import com.es.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName OrderRepository
 * @Description 自定义Repository
 * @Author wangchen
 * @Date 2021/4/14 4:50 下午
 **/
@Repository
public interface OrderRepository extends ElasticsearchRepository<User,Long> {

    /**
     * 根据名字查询数量
     *
     * @param name
     * @return
     */
    long countByName(String name);

    /**
     * 根据地址查询
     *
     * @param address
     * @return
     */
    long countByAddress(String address);

    /**
     * 根据名字删除，并返回删除数量
     *
     * @param name
     * @return
     */
    long deleteByName(String name);

    /**
     * 根据名字删除并返回删除对象
     *
     * @param name
     * @return
     */
    List<User> removeByName(String name);

    /**
     * 根据名字查询
     *
     * @param name
     * @return
     */
    List<User> findByName(String name);

    /**
     * 根据eamil和名字查询
     *
     * @param email
     * @param name
     * @return
     */
    List<User> findByEmailAndName(String email, String name);

    /**
     * 根据email或者手机号查询
     *
     * @param email
     * @param mobile
     * @return
     */
    List<User> findDistinctByEmailOrMobile(String email, String mobile);

    /**
     * 根据名字分页查询
     *
     * @param name
     * @param page
     * @return
     */
    Page<User> findByName(String name, Pageable page);

    /**
     * 根据名字按照id倒序排列查询
     *
     * @param name
     * @param page
     * @return
     */
    Page<User> findByNameOrderByIdDesc(String name, Pageable page);

    /**
     * 根据company对象下的name属性查询
     *
     * @param companyName
     * @param page
     * @return
     */
    Page<User> findByCompanyName(String companyName, Pageable page);

    /**
     * 根据company对象下的name或者根据company对象下的nature属性查询
     *
     * @param companyName
     * @param ompanyNature
     * @return
     */
    List<User> findByCompanyNameOrCompanyNature(String companyName, String ompanyNature);

    /**
     * 根据id范围查询
     *
     * @param start
     * @param end
     * @return
     */
    List<User> findByIdBetween(Long start, Long end);

    /**
     * id小于参数查询
     *
     * @param id
     * @return
     */
    List<User> findByIdLessThan(Long id);

    /**
     * 名字模糊查询并且根据id范围查询
     *
     * @param name
     * @param from
     * @param to
     * @return
     */
    List<User> findByNameLikeAndIdBetween(String name, Long from, Long to);

    /**
     * 忽略email大小写查询
     *
     * @param email
     * @return
     */
    List<User> findByEmailIgnoreCaseLike(String email);

    /**
     * 查询前10
     *
     * @param name
     * @param sort
     * @return
     */
    List<User> findFirst10ByName(String name, Sort sort);

    /**
     * 查询前3
     *
     * @param name
     * @param pageable
     * @return
     */
    Slice<User> findTop3ByName(String name, Pageable pageable);

    /**
     * 根据名字查询前10分页
     *
     * @param name
     * @param pageable
     * @return
     */
    Page<User> queryFirst10ByName(String name, Pageable pageable);

    /**
     * 小于参数id查询
     *
     * @param id
     * @return
     */
    Streamable<User> queryByIdLessThan(Long id);

    /**
     * 大于id参数查询
     *
     * @param id
     * @return
     */
    Streamable<User> queryByIdGreaterThan(Long id);

    /**
     * 根据名字异步查询
     *
     * @param name
     * @return
     */
    @Async
    CompletableFuture<User> findOneByName(String name);

    @Query("{\"range\":{\"id\":{\"from\":\"?0\",\"to\":\"?1\"}}}")
    List<User> queryByIdSql(Long start, Long end);

    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    List<User> queryByNameSql(String name);

}
