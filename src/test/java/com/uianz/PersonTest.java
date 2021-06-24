package com.uianz;

import cn.hutool.core.thread.ThreadUtil;
import com.uianz.modules.person.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

/**
 * @author uianz
 * @date 2021/5/14
 */
@SpringBootTest
public class PersonTest {

    @Autowired
    PersonRepository personRepository;

//    @Autowired
//    ReactiveElasticsearchTemplate template;
//
//    @Test
//    void test() {
//        template.save(new Person("Bruce Banner", 42))
//                .doOnNext(System.out::println)
//                .flatMap(person -> template.get(person.getId(), Person.class))
//                .doOnNext(System.out::println)
//                .flatMap(person -> template.delete(person))
//                .doOnNext(System.out::println)
//                .flatMap(id -> template.count(Person.class))
//                .doOnNext(System.out::println)
//                .subscribe();
//        ThreadUtil.sleep(10000);
//    }

    @Test
    public void get(){
        personRepository.findById(Mono.just("wvzcaXkBkbVWwgT7bGXO"))
                .doOnNext(System.out::println)
                .subscribe();
        ThreadUtil.sleep(5000);
    }

}
