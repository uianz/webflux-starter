package com.uianz.common.page;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Data
@Accessors(chain = true)
public class PageResult<T> {
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页结果数
     */
    private Integer pageSize;
    /**
     * 总数
     */
    private Long total;

    /**
     * 数据
     */
    private List<T> list;

    public PageResult() {
    }

    public PageResult(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageResult(List<T> list, Integer pageNum, Integer pageSize, Long total) {
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    public <P> PageResult<P> map(Function<T, P> func) {
        return new PageResult<>(
                this.list.stream().map(func).collect(Collectors.toList()),
                this.pageNum,
                this.pageSize,
                this.total
        );
    }


    public static <T> PageResult<T> of(PageRequest pageRequest) {
        return new PageResult<T>(pageRequest.getPageNum(), pageRequest.getPageSize()).setTotal(0L).setList(Collections.emptyList());
    }

    public static <T> PageResult<T> of(Integer pageNum, Integer pageSize) {
        return new PageResult<T>(pageNum, pageSize).setTotal(0L).setList(Collections.emptyList());
    }

    public static <T> PageResult<T> of(PageRequest pageRequest, Long count, List<T> list) {
        return new PageResult<T>(pageRequest.getPageNum(), pageRequest.getPageSize()).setTotal(count).setList(list);
    }

    public static <T> PageResult<T> of(Integer pageNumber, Integer pageSize, Long count, List<T> list) {
        return new PageResult<T>(pageNumber,pageSize).setTotal(count).setList(list);
    }

    public static <T> PageResult<T> of(PageRequest pageRequest, Long count, io.vavr.collection.List<T> list) {
        return new PageResult<T>(pageRequest.getPageNum(), pageRequest.getPageSize()).setTotal(count).setList(list.toJavaList());
    }

    public static <T> PageResult<T> of(PageResult<?> pageResult, List<T> list) {
        return new PageResult<T>(list, pageResult.getPageNum(), pageResult.getPageSize(), pageResult.getTotal());
    }

    public void from(PageResult<T> pageResult) {
        setPageSize(pageResult.pageSize);
        setPageSize(pageResult.pageSize);
        setTotal(pageResult.total);
        setList(pageResult.list);
    }

    public PageResult<T> into(PageResult<T> into) {
        into.from(this);
        return into;
    }

}
