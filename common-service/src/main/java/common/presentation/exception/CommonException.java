package common.presentation.exception;

public class CommonException extends TpbException{
    public CommonException(){
        super();
    }

    public CommonException(String errorCode){
        super(errorCode);
    }

    public CommonException(String errorCode, String additionErrorCode, String additionInfo){
        super(errorCode, additionErrorCode, additionInfo);
    }

    public CommonException(String errorCode, String replaceStr){
        super(errorCode);
        setResponse(ErrorHelper.buildResponseWithoutDesc(errorCode, replaceStr));
    }

    public CommonException(Response response){
        super(response.getErrorMessage().getErrorCode());
        setResponse(response);
    }
}
