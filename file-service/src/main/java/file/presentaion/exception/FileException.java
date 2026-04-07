package file.presentaion.exception;

public class FileException extends TpbException{
    public FileException(){
        super();
    }

    public FileException(String errorCode){
        super(errorCode);
    }

    public FileException(String errorCode, String additionErrorCode, String additionInfo){
        super(errorCode, additionErrorCode, additionInfo);
    }

    public FileException(String errorCode, String replaceStr){
        super(errorCode);
        setResponse(ErrorHelper.buildResponseWithoutDesc(errorCode, replaceStr));
    }

    public FileException(Response response){
        super(response.getErrorMessage().getErrorCode());
        setResponse(response);
    }
}
