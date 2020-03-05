package com.study.wang.tenement.back;
 
 
import lombok.*;
import lombok.experimental.Accessors;
 
import java.io.Serializable;
 
/**
 *  <p>
 *      统一返回数据
 *  </p>
 *
 * @author 秋枫艳梦
 * @date 2020-01-11
 * */
@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class Back<T> implements Serializable {
    private static final long serialVersionUID = 1L;
 
    /**
     * 运行成功
     */
    final String RUN_SUCCESS = "0";
 
    /**
     * 运行失败
     */
    final String RUN_ERROR = "1";
 
    /**
     * 业务失败
     */
    final Boolean BACK_ERROR = false;
 
    /**
     * 业务成功
     */
    final Boolean BACK_SUCCESS = true;
 
 
    @Getter
    @Setter
    private String code = RUN_SUCCESS;
 
    @Getter
    @Setter
    private String msg = "success";
 
 
    @Getter
    @Setter
    private Boolean state = BACK_SUCCESS;
 
    @Getter
    @Setter
    private Long count = null;
 
 
    @Getter
    @Setter
    private T data;
 
    public Back() {
        super();
    }
 
    public Back(T data) {
        super();
        this.data = data;
    }
 
 
    public Back<T> error(String msg) {
        this.state = BACK_ERROR;
        this.msg = msg;
        return this;
    }
 
    public Back<T> msg(String msg) {
        this.msg = msg;
        return this;
    }
}