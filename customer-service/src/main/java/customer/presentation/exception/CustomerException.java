package customer.presentation.exception;

public class CustomerException extends TpbException{
    public CustomerException(){
        super();
    }

    public CustomerException(String errorCode){
        super(errorCode);
    }

    public CustomerException(String errorCode, String additionErrorCode, String additionInfo){
        super(errorCode, additionErrorCode, additionInfo);
    }

    public CustomerException(String errorCode, String replaceStr){
        super(errorCode);
        setResponse(ErrorHelper.buildResponseWithoutDesc(errorCode, replaceStr));
    }

    public CustomerException(Response response){
        super(response.getErrorMessage().getErrorCode());
        setResponse(response);
    }
}
