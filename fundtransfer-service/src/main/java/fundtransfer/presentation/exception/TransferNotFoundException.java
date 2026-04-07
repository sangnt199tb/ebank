package fundtransfer.presentation.exception;

public class TransferNotFoundException extends TpbException{
    public TransferNotFoundException(String errorCode){
        super(errorCode);
    }
}
