package customer.presentation.exception.handler;


import customer.presentation.exception.ErrorHelper;
import customer.presentation.exception.Response;
import customer.presentation.exception.TpbException;
import customer.presentation.exception.CustomerNotFoundException;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
@Order(value = PriorityOrdered.HIGHEST_PRECEDENCE)
public class CustomerResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Throwable.class})
    protected ResponseEntity<Response> handlerTpbExp(Throwable ex, WebRequest request){
        HttpHeaders headers = new HttpHeaders();

        if(ex instanceof CustomerNotFoundException){
            CustomerNotFoundException notFoundException = (CustomerNotFoundException) ex;
            return new ResponseEntity<>(notFoundException.getResponse(), headers, HttpStatus.NOT_FOUND);
        }

        if(ex instanceof TpbException){
            TpbException tpbException = (TpbException) ex;
            String message = tpbException.getMessage() == null ? tpbException.getResponse().toString() : tpbException.getMessage();
            return new ResponseEntity<>(tpbException.getResponse(), headers, HttpStatus.BAD_GATEWAY);
        }

        return new ResponseEntity<>(ErrorHelper.buildInternalServerError(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
