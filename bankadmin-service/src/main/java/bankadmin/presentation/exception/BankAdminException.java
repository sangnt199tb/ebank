package bankadmin.presentation.exception;

public class BankAdminException extends TpbException{
    public BankAdminException(){
        super();
    }

    public BankAdminException(String errorCode){
        super(errorCode);
    }

    public BankAdminException(String errorCode, String additionErrorCode, String additionInfo){
        super(errorCode, additionErrorCode, additionInfo);
    }

    public BankAdminException(String errorCode, String replaceStr){
        super(errorCode);
        setResponse(ErrorHelper.buildResponseWithoutDesc(errorCode, replaceStr));
    }

    public BankAdminException(Response response){
        super(response.getErrorMessage().getErrorCode());
        setResponse(response);
    }
}
