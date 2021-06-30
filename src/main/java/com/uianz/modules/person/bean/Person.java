package com.uianz.modules.person.bean;

import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;

/**
 * @author uianz
 * @date 2021/5/14
 */
@Value
public class Person{
    @With
    @Id
    Integer id;
    String name;
    Integer age;
}
