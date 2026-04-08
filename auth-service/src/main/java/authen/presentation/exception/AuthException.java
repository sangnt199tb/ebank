package authen.presentation.exception;

public class AuthException extends TpbException{
    public AuthException(){
        super();
    }

    public AuthException(String errorCode){
        super(errorCode);
    }

    public AuthException(String errorCode, String additionErrorCode, String additionInfo){
        super(errorCode, additionErrorCode, additionInfo);
    }

    public AuthException(String errorCode, String replaceStr){
        super(errorCode);
        setResponse(ErrorHelper.buildResponseWithoutDesc(errorCode, replaceStr));
    }

    public AuthException(Response response){
        super(response.getErrorMessage().getErrorCode());
        setResponse(response);
    }
}
