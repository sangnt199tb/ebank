package fundtransfer.presentation.exception;

public class TransferException extends TpbException{
    public TransferException(){
        super();
    }

    public TransferException(String errorCode){
        super(errorCode);
    }

    public TransferException(String errorCode, String additionErrorCode, String additionInfo){
        super(errorCode, additionErrorCode, additionInfo);
    }

    public TransferException(String errorCode, String replaceStr){
        super(errorCode);
        setResponse(ErrorHelper.buildResponseWithoutDesc(errorCode, replaceStr));
    }

    public TransferException(Response response){
        super(response.getErrorMessage().getErrorCode());
        setResponse(response);
    }
}
