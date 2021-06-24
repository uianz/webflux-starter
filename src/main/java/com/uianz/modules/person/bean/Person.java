package com.uianz.modules.person.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author uianz
 * @date 2021/5/14
 */
@Data
@Table
@ApiModel("person")
public class Person{
    @Id
    private Integer id;
    private String name;
    private Integer age;
}
