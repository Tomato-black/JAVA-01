package cz.loong.common;


import java.util.function.Supplier;

/**
 * @Author: zhang.cao
 */
public class LoongException extends RuntimeException implements Supplier<RuntimeException> {
	private static final long serialVersionUID = 1L;
	private Integer code;

	  public LoongException(Integer code, String msg) {
	        super(msg);
	        this.code =code;
	    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public RuntimeException get() {
        return null;
    }
}
