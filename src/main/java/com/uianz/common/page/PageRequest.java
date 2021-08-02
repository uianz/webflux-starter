package com.uianz.common.page;

//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.querydsl.QPageRequest;

import java.io.Serializable;
import java.util.Objects;

@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static int DEFAULT_CURRENT = 1;

    private final static int DEFAULT_SIZE = 20;
    /**
     * 页码
     */
//    @ApiModelProperty(value = "页码", dataType = "int", example = DEFAULT_CURRENT + "", required = true)
    private Integer pageNum;

    /**
     * 每页结果数
     */
//    @ApiModelProperty(value = "每页显示数量", dataType = "int", example = DEFAULT_SIZE + "", required = true)
    private Integer pageSize ;

    public Integer getPageNum(){
        return Objects.isNull(this.pageNum) || this.pageNum <= 0 ? DEFAULT_CURRENT : this.pageNum;
    }

    public Integer getPageSize(){
        return Objects.isNull(this.pageSize) || this.pageSize <= 0 ? DEFAULT_SIZE : this.pageSize;
    }

    public QPageRequest newPageRequest(){
        return QPageRequest.of(getPageNum() - 1, getPageSize());
    }

}
