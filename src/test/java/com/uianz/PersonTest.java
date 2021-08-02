package com.uianz;

import com.querydsl.core.types.dsl.Expressions;
import com.uianz.modules.person.bean.Person;
import com.uianz.modules.person.bean.QPerson;
import com.uianz.modules.person.repository.PersonRepository;
import io.r2dbc.postgresql.codec.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;

import java.util.List;

import static com.querydsl.core.types.Projections.bean;

/**
 * @author uianz
 * @date 2021/5/14
 */
public class PersonTest extends TestBase {

    @Autowired
    PersonRepository personRepository;

    @Test
    public void get() {
        var person = personRepository.findById(2);
        System.out.println(block(person));
    }

    @Test
    public void testJsonPath(){
        var p = QPerson.person;
        var all = personRepository.query(query ->
                query.select(bean(Person.class, p.id,p.name, p.data))
                        .from(p)
                        .where(Expressions.stringTemplate("data->>'hello'").eq("world"))
        ).all();
        block(all).forEach(System.out::println);
    }

    @Test
    public void testJsonPath2(){
        var p = QPerson.person;
        var all = personRepository.query(query ->
                query.select(Expressions.stringTemplate("coalesce(\n" +
                        "                  case when data ->> 'hello' is not null then data->>'hello'\n" +
                        "                  else data->>'hello3' end\n" +
                        "                  ,'{}'\n" +
                        "              )" ))
                        .from(p)
//                .where(p.id.eq(2))
        ).all();
        block(all).forEach(System.out::println);
    }

    @Test
    public void testPage(){
        var p = QPerson.person;
        var pageRequest = PageRequest.of(1, 2, QSort.by(p.age.desc()));
        var page =  personRepository.page(pageRequest,p.age.eq(1));
        var result = block(page);
        System.out.println("page size = " + result.getPageSize());
        result.getList().forEach(System.out::println);
    }

    @Test
    public void insert2() {
        var persons = List.of(
                new Person(null, "zzz1", 1, Json.of("{\"hello\":\"world1\"}")),
                new Person(null, "zzz2", 2, Json.of("{\"hello\":\"world2\"}")),
                new Person(null, "zzz3", 3, Json.of("{\"hello3\":\"world3\"}"))
        );
        var result = personRepository.saveAll(persons);
        System.out.println(block(result));
    }


    @Test
    public void queryDsl() {
        var p = QPerson.person;
        var result = personRepository.findAll(p.age.gt(1).and(p.name.isNotNull()));
        block(result).forEach(System.out::print);
    }

}
