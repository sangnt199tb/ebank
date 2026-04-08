package authen.presentation.exception;

public class AuthNotFoundException extends TpbException{
    public AuthNotFoundException(String errorCode){
        super(errorCode);
    }
}
