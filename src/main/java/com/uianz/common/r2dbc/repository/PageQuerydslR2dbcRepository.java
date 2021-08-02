package com.uianz.common.r2dbc.repository;

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.uianz.common.page.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author uianz
 * @date 2021/7/23
 */
@NoRepositoryBean
public interface PageQuerydslR2dbcRepository<T, ID> extends QuerydslR2dbcRepository<T, ID> {
    /**
     * 用findAll会报错
     * https://stackoverflow.com/questions/58874827/spring-data-r2dbc-and-pagination/63787029#63787029
     * https://github.com/spring-projects/spring-data-r2dbc/issues/595
     */
    Flux<T> findBy(Pageable pageable);

    Flux<T> findBy(Pageable pageable, Predicate predicate);

    default Mono<PageResult<T>> page(Pageable pageable) {
        return page(pageable, new BooleanBuilder().not());
    }

    default Mono<PageResult<T>> page(Pageable pageable, Predicate predicate) {
        return findBy(pageable,predicate).collectList()
                .zipWith(count(predicate))
                .map(tuple2 -> PageResult.of(pageable.getPageNumber(), pageable.getPageSize(), tuple2.getT2(),
                        tuple2.getT1()))
                .switchIfEmpty(Mono.just(PageResult.of(pageable.getPageNumber(), pageable.getPageSize())));
    }
}
