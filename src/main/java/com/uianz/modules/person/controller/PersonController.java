package com.uianz.modules.person.controller;

import com.uianz.common.page.PageRequest;
import com.uianz.common.page.PageResult;
import com.uianz.common.resp.R;
import com.uianz.modules.person.bean.Person;
import com.uianz.modules.person.repository.PersonRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostMapping
    @ApiOperation(value = "新增用户")
    public Mono<R<Person>> add(@RequestBody Person person) {
        return R.ok(personRepository.save(person));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "id获取单个用户", response = Person.class)
    public Mono<R<Person>> getById(@PathVariable Integer id) {
        return R.ok(personRepository.findById(id));
    }

    @GetMapping("/mono")
    public Mono<R<String>> getMono(Mono<String> name) {
        return R.ok(name);
    }

    @GetMapping("/vavrList")
    public Mono<R<io.vavr.collection.List<String>>> getVavrList(io.vavr.collection.List<String> names) {
        return R.ok(Mono.just(names));
    }

    @GetMapping
    @ApiOperation(value = "获取所有用户")
    public Mono<R<List<Person>>> list() {
        return R.ok(personRepository.findAll());
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页列表")
    public Mono<R<PageResult<Person>>> page(PageRequest request) {
        return R.ok(personRepository.page(request.newPageRequest()));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "id删除用户")
    public Mono<Void> delete(@PathVariable("id") Integer id) {
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
    public Mono testError() {
        return Mono.error(RuntimeException::new);
    }
}
