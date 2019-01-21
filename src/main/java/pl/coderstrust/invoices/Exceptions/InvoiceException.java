package pl.coderstrust.invoices.Exceptions;

public class InvoiceException extends Exception {

    public InvoiceException(String exception, Exception e) {
        super(exception, e);
    }
}
