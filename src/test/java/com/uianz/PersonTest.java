package com.uianz;

import cn.hutool.core.thread.ThreadUtil;
import com.uianz.modules.person.bean.Person;
import com.uianz.modules.person.bean.QPerson;
import com.uianz.modules.person.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author uianz
 * @date 2021/5/14
 */
public class PersonTest extends TestBase{

    @Autowired
    PersonRepository personRepository;

    @Test
    public void get(){
        personRepository.findById(1)
                .doOnNext(System.out::println)
                .subscribe();
        ThreadUtil.sleep(5000);
    }

    @Test
    public void insert(){
        var result = personRepository.saveAll(List.of(new Person(null, "zzz4", 4),
                new Person(null, "zzz2", 2),
                new Person(null, "zzz3", 3)));
        System.out.println(block(result));
    }

    @Test
    public void queryDsl(){
        var p =  QPerson.person;
        var result = personRepository.findAll(p.age.gt(1).and(p.name.isNotNull()));
        block(result).forEach(System.out::print);
    }

}
