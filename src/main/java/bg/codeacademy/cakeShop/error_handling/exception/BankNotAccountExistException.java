package bg.codeacademy.cakeShop.error_handling.exception;

public class BankNotAccountExistException extends RuntimeException {
    public BankNotAccountExistException(String message) {
        super(message);
    }
}
