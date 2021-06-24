package com.uianz.modules.person.repository;

import com.uianz.modules.person.bean.Person;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

//@Repository
public interface PersonRepository extends ReactiveSortingRepository<Person, String> {
    Mono<Person> findByName(String name);
}
