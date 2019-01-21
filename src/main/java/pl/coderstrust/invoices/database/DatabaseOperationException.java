package pl.coderstrust.invoices.database;

public class DatabaseOperationException extends Exception {

    public DatabaseOperationException() {
        super();
    }

    public DatabaseOperationException(String message) {
        super(message);
    }

    public DatabaseOperationException(String message, Exception exception) {
        super(message, exception);
    }
}
