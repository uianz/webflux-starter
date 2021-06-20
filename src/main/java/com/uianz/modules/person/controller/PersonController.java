package com.uianz.modules.person.controller;

import com.uianz.modules.person.bean.Person;
import com.uianz.modules.person.repository.PersonRepository;
import com.uianz.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author uianz
 * @date 2021/5/14
 */
@RestController
@RequestMapping("/person")
@Api(tags = "person")
public class PersonController {

    PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<R<Person>> add(@RequestBody Mono<Person> person) {
        return R.ok(personRepository.saveAll(person).next());
    }

    @GetMapping("/{id}")
    public Mono<R<Person>> getById(@PathVariable String id) {
        return R.ok(personRepository.findById(id));
    }

    @GetMapping
    public Mono<R<Person>> list() {
        return R.ok(personRepository.findAll());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") String id) {
        return personRepository.deleteById(id);
    }

    @GetMapping("/test")
    @ApiOperation("test mono")
    public Mono<R<Integer>> test(){
        return R.ok(Mono.just(1));
    }

    @GetMapping("/test2")
    @ApiOperation("test flux")
    public Mono<R<Integer>> test2(){
        return R.ok(Flux.just(1, 2));
    }

    @GetMapping("/test4")
    @ApiOperation("test error")
    public Mono<R<Integer>> test3(){
        return R.fail("is a error message");
    }
}
