package pl.coderstrust.invoices.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.service.InvoiceService;

@RequestMapping("/invoices")
@RestController
public class InvoiceController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("")
    @ApiOperation(value = "Show all invoices", notes = "Retrieving all invoices", response = Invoice[].class)
    public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        Collection<Invoice> result;
        try {
            result = invoiceService.getAllInvoices();
            logger.info("All invoices[\" + invoiceId + \"] have been shown.");
        } catch (DatabaseOperationException e) {
            logger.error("Selected invoices [\" + invoiceId + \"] can't be displayed.");
            throw e;
        }
        return result;
    }

    @GetMapping("/byDates")
    @ApiOperation(value = "Find invoices from the time range", notes = "Retrieving time range (from date - to date)", response = Invoice[].class)
    public Collection<Invoice> getAllOfRange(
        @ApiParam @RequestParam(value = "fromDate") String fromDate,
        @ApiParam @RequestParam(value = "toDate") String toDate) throws DatabaseOperationException {
        LocalDate startDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(fromDate));
        LocalDate endDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(toDate));
        invoiceService.getAllOfRange(startDate, endDate);
        {
            Collection<Invoice> result;
            try {
                result = invoiceService.getAllOfRange(startDate, endDate);
                logger.info("All invoices in selected date range have been shown.");
            } catch (DatabaseOperationException e) {
                logger.error("Selected invoices can't be displayed.");
                throw e;
            }
            return result;
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find invoice by its unique ID", notes = "Retrieving the invoice of ID (Long number)", response = Invoice.class)
    public Invoice getInvoiceById(@PathVariable Long id) throws DatabaseOperationException {
        invoiceService.getInvoiceById(id);
        {
            Invoice result;
            try {
                result = invoiceService.getInvoiceById(id);
                logger.info("This is invoice with selected id [\" + invoiceId + \"].");
            } catch (DatabaseOperationException e) {
                logger.error("Selected invoice with id=[%] can't be displayed.",e);
                throw e;
            }
            return result;
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add or update invoice", notes = "Retrieving JSON body of new invoice (ID = 0), or invoice to update")
    public Invoice addInvoice(@RequestBody Invoice invoice) throws DatabaseOperationException {
        invoiceService.saveInvoice(invoice);
        {
            Invoice result;
            try {
                result = invoiceService.saveInvoice(invoice);
                logger.info("The invoice with selected id [\" + invoiceId + \"] has been added/updated successfully.");
            } catch (DatabaseOperationException e) {
                logger.error("An error occurred while adding/updating invoice with selected id=[%].");
                throw e;
            }
            return result;
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Find invoice by its unique ID and delete it", notes = "Retrieving the invoice of ID (Long number)")
    public void deleteInvoice(@PathVariable Long id) throws DatabaseOperationException {
        invoiceService.deleteInvoice(id);
        {
            try {
                invoiceService.deleteInvoice(id);
                logger.info("Invoice with selected id [\" + invoiceId + \"]. has been deleted.");
            } catch (DatabaseOperationException e) {
                logger.error("An error occurred while adding/updating invoice with selected id [\" + invoiceId + \"]..");
                throw e;
            }
        }
    }
}
