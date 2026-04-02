package onboard.presentation.exception;

public class OnboardingException extends TpbException{
    public OnboardingException(){
        super();
    }

    public OnboardingException(String errorCode){
        super(errorCode);
    }

    public OnboardingException(String errorCode, String additionErrorCode, String additionInfo){
        super(errorCode, additionErrorCode, additionInfo);
    }

    public OnboardingException(String errorCode, String replaceStr){
        super(errorCode);
        setResponse(ErrorHelper.buildResponseWithoutDesc(errorCode, replaceStr));
    }

    public OnboardingException(Response response){
        super(response.getErrorMessage().getErrorCode());
        setResponse(response);
    }
}
