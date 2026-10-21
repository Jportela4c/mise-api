package common.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final String code;
    private final Response.Status status;

    protected BusinessException(
            String code,
            String message,
            Response.Status status
    ) {
        super(message);
        this.code = code;
        this.status = status;
    }

}