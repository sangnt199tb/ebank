package common.presentation.exception;

public class CommonNotFoundException extends TpbException{
    public CommonNotFoundException(String errorCode){
        super(errorCode);
    }
}
