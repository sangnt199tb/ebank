package bankadmin.presentation.exception;

public class BankAdminNotFoundException extends TpbException{
    public BankAdminNotFoundException(String errorCode){
        super(errorCode);
    }
}
