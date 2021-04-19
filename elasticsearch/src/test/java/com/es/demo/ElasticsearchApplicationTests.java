package com.es.demo;

import com.alibaba.fastjson.JSONObject;
import com.es.demo.model.Company;
import com.es.demo.model.User;
import com.es.demo.service.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.util.Streamable;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@SpringBootTest
class ElasticsearchApplicationTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ElasticsearchRestTemplate esRestTemplate;


    @Test
    public void saveTest() {
        Company c = new Company("百度在线网络技术（北京）有限公司", "北京市海淀区中关村街道", "18510367878", "123@baidu.com", "互联网",
                "https://www.baidu.com");

        User p = new User(2L, "李四", "北京市海淀区中关村街道", "165187413544", "123@baidu.com", LocalDate.of(1998, 2, 12),
                "3213021982121478", c);
        User save = orderRepository.save(p);
        log.info("保存后的结果 {} ", JSONObject.toJSONString(save,true));
    }


    @Test
    public void deleteByIdTest() {
        long id = 0L;
        orderRepository.deleteById(id);
        log.info("删除数据id {}", id);
    }

    @Test
    public void findAllSortTest() {
        // 排序的列
        Sort sort = Sort.by("birthday");
        // 升序
        log.info("升序 {}", JSONObject.toJSONString(orderRepository.findAll(sort),true));
        // sort.descending() 倒序
        log.info("倒序 {}", JSONObject.toJSONString(orderRepository.findAll(sort.descending()),true));
    }

    @Test
    public void countByNameTest() {
        String name = "三";
        long count = orderRepository.countByName(name);
        log.info("姓名：{}，数量: {}", name, count);
    }

    @Test
    public void countByAddressTest() {
        String address = "北京";
        long count = orderRepository.countByAddress(address);
        log.info("地址：{}，数量: {}", address, count);
    }

    @Test
    public void deleteByNameTest() {
        String name = "张四";
        long count = orderRepository.deleteByName(name);
        log.info("删除姓名：{}，删除数量: {}", name, count);
    }

    @Test
    public void findByNameTest() {
        String name = "李四";
        List<User> list = orderRepository.findByName(name);
        log.info("姓名：{}，结果: {}", name, JSONObject.toJSONString(list, true));
    }

    @Test
    public void findByEmailAndNameTest() {
        String name = "阿良";
        String email = "aliang@huawei.com";
        List<User> list = orderRepository.findByEmailAndName(email, name);
        log.info("email：{}，name: {}，结果: {}", email, name, JSONObject.toJSONString(list, true));
    }

    @Test
    public void findDistinctByEmailOrMobile() {
        String email = "liyiyi@huawei.com";
        String mobile = "16518741234";
        List<User> list = orderRepository.findDistinctByEmailOrMobile(email, mobile);
        log.info("email：{}，mobile: {}，结果: {}", email, mobile, JSONObject.toJSONString(list, true));
    }

    @Test
    public void findByNamePageTest() {
        // 分页查询
        String name = "李四";
        // 从0开始
        int page = 0;
        // 必须大于0
        int size = 2;

        // 排序可以这样
        // 方式1
//        Sort sort = Sort.by("id");
//        PageRequest.of(page, size, sort);

        // 方式2
        Sort.TypedSort<User> typedSort = Sort.sort(User.class);
        Sort sort = typedSort.by(User::getId).ascending().and(typedSort.by(User::getBirthday).descending());

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> result = orderRepository.findByName(name, pageable);
        log.info("name: {}, 分页结果: {}", name, JSONObject.toJSONString(result, true));
    }

    @Test
    public void findByNameNoPageTest() {
        // 不分页
        String name = "李四";
        Pageable unpaged = Pageable.unpaged();
        Page<User> result = orderRepository.findByName(name, unpaged);
        log.info("name: {}, 分页结果: {}", name, JSONObject.toJSONString(result, true));
    }

    @Test
    public void findByNameOrderByIdDescTest() {
        String name = "李四";
        // 从0开始
        int page = 0;
        // 必须大于0
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);
        Page<User> result = orderRepository.findByNameOrderByIdDesc(name, pageable);
        log.info("name: {}, 分页结果: {}", name, JSONObject.toJSONString(result, true));
    }

    @Test
    public void findByCompanyTest() {
        // 从0开始
        int page = 0;
        // 必须大于0
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);
        String companyName = "华为";
        Page<User> result = orderRepository.findByCompanyName(companyName, pageable);
        log.info("分页结果: {}", JSONObject.toJSONString(result, true));
    }

    @Test
    public void findByCompanyNameOrCompanyNatureTest() {
        String companyName = "度";
        String ompanyNature = "硬件";
        List<User> list = orderRepository.findByCompanyNameOrCompanyNature(companyName, ompanyNature);
        log.info("结果: {}", JSONObject.toJSONString(list, true));
    }

    @Test
    public void findByIdBetweenTest() {
        long start = 1L;
        long end = 3L;
        List<User> list = orderRepository.findByIdBetween(start, end);
        log.info("结果: {}", JSONObject.toJSONString(list, true));
    }

    @Test
    public void findByIdLessThanTest() {
        long id = 3L;
        List<User> list = orderRepository.findByIdLessThan(id);
        log.info("结果: {}", JSONObject.toJSONString(list, true));
    }

    @Test
    public void findByIdBetweenAndNameTest() {
        long start = 1L;
        long end = 5L;
        String name = "李";
        List<User> list = orderRepository.findByNameLikeAndIdBetween(name, start, end);
        log.info("结果: {}", JSONObject.toJSONString(list, true));
    }

    @Test
    public void findByEmailIgnoreCaseTest() {
        String email = "Huawei";
        List<User> list = orderRepository.findByEmailIgnoreCaseLike(email);
        log.info("结果: {}", JSONObject.toJSONString(list, true));
    }

    @Test
    public void streamableTest() {
        // 将结果合并
        Streamable<User> streamable = orderRepository.queryByIdGreaterThan(5L)
                .and(orderRepository.queryByIdLessThan(2L));
        List<User> list = streamable.toList();
        log.info("结果: {}", JSONObject.toJSONString(list, true));
    }

    @Test
    public void findOneByNameTest() throws InterruptedException, ExecutionException {
        String name = "李四";
        CompletableFuture<User> future = orderRepository.findOneByName(name);
        User User = future.get();
        log.info("结果: {}", JSONObject.toJSONString(User, true));

    }

    @Test
    public void queryBySqlTest() {
        List<User> list = orderRepository.queryByIdSql(1L, 3L);
        log.info("结果: {}", JSONObject.toJSONString(list, true));
    }

    @Test
    public void queryByNameSqlTest() {
        List<User> list = orderRepository.queryByNameSql("李四");
        log.info("结果: {}", JSONObject.toJSONString(list, true));
    }

}
