package com.uianz.modules.person.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author uianz
 * @date 2021/5/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "person")
public class Person {
    @Id
    private String id;
    private String name;
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
