package com.uianz.modules.person.controller;

import com.uianz.modules.person.bean.Person;
import com.uianz.modules.person.repository.PersonRepository;
import com.uianz.resp.R;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author uianz
 * @date 2021/5/14
 */
@RestController
@RequestMapping("/person")
public class PersonController {

    PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Person> add(@RequestBody Mono<Person> person) {
        return personRepository.saveAll(person).next();
    }

    @GetMapping("/{id}")
    public Mono<Person> getById(@PathVariable String id) {
        return personRepository.findById(id);
    }

    @GetMapping("/test")
    public Mono<R<Integer>> test(){
        return R.ok(Mono.just(1));
    }

    @GetMapping("/test2")
    public Mono test2(){
        return R.ok(Flux.just(1, 2));
    }

    @GetMapping("/test3")
    public Mono test3(){
        return R.fail("is a error message");
    }

//    @GetMapping
//    public Mono list() {
//        return R.ok(personRepository.findAll());
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") String id) {
        return personRepository.deleteById(id);
    }
}
