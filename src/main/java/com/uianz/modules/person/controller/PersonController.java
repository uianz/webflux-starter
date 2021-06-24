package com.uianz.modules.person.controller;

import com.uianz.common.resp.R;
import com.uianz.modules.person.bean.Person;
import com.uianz.modules.person.repository.PersonRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author uianz
 * @date 2021/5/14
 */
@RestController
@RequestMapping("/person")
@Api(tags = "person")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @PostMapping
    @ApiOperation(value = "新增用户")
    public Mono<R<Person>> add(@RequestBody Person person) {
        return R.ok(personRepository.save(person));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "id获取单个用户", response = Person.class)
    public Mono<R<Person>> getById(@PathVariable String id) {
        return R.ok(personRepository.findById(id));
    }

    @GetMapping
    @ApiOperation(value = "获取所有用户")
    public Mono<R<List<Person>>> list() {
        return R.ok(personRepository.findAll());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "id删除用户")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return personRepository.deleteById(id);
    }

    @GetMapping("/test")
    @ApiOperation("test mono")
    public Mono<R<Integer>> test() {
        return R.ok(Mono.just(1));
    }

    @GetMapping("/test2")
    @ApiOperation("test flux")
    public Mono<R<List<Integer>>> test2() {
        return R.ok(Flux.just(1, 2));
    }

    @GetMapping("/test4")
    @ApiOperation("test error")
    public Mono<R<Integer>> test3() {
        return R.fail("is a error message");
    }

    @GetMapping("/error")
    @ApiOperation("throw exception")
    public Mono testError(){
        return Mono.error(RuntimeException::new);
    }
}
