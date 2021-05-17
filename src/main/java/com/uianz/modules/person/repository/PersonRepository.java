package com.uianz.modules.person.repository;

import com.uianz.modules.person.bean.Person;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;

public interface PersonRepository extends ReactiveElasticsearchRepository<Person, String> {
}
