package customer.presentation.exception;

public class CustomerNotFoundException extends TpbException{
    public CustomerNotFoundException(String errorCode){
        super(errorCode);
    }
}
