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

    private InvoiceService invoiceService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("")
    @ApiOperation(value = "Show all invoices", notes = "Retrieving all invoices", response = Invoice[].class)
    public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        logger.trace("Request for all invoices.");
        Collection<Invoice> result;
        try {
            result = invoiceService.getAllInvoices();
            logger.info("{} invoices were read", result.size());
        } catch (DatabaseOperationException e) {
            logger.error("Unable to obtain invoices from service. {}", e.getLocalizedMessage());
            throw e;
        }
        return result;
    }

    @GetMapping("/byDates")
    @ApiOperation(value = "Find invoices from the time range", notes = "Retrieving time range (from date - to date)",
        response = Invoice[].class)
    public Collection<Invoice> getAllOfRange(
        @ApiParam @RequestParam(value = "fromDate") String fromDate,
        @ApiParam @RequestParam(value = "toDate") String toDate) throws DatabaseOperationException {
        LocalDate startDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(fromDate));
        LocalDate endDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(toDate));
        logger.trace("Request for invoices from dates: [{} - {}]", startDate.toString(), endDate.toString());
        Collection<Invoice> result;
        try {
            result = invoiceService.getAllOfRange(startDate, endDate);
            logger.info("{} invoices were read", result.size());
        } catch (DatabaseOperationException e) {
            logger.error("Unable to obtain invoices from service. {}", e.getLocalizedMessage());
            throw e;
        }
        return result;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find invoice by its unique ID", notes = "Retrieving the invoice of ID", response = Invoice.class)
    public Invoice getInvoiceById(@PathVariable Object id) throws DatabaseOperationException {
        logger.trace("Request for invoice with id = [{}]", id);
        Invoice result;
        try {
            result = invoiceService.getInvoiceById(id);
            logger.info("Invoice was read successfully. {}", result);
        } catch (DatabaseOperationException e) {
            logger.error("Unable to obtain invoices from service. {}", e.getLocalizedMessage());
            throw e;
        }
        return result;
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add or update invoice",
        notes = "Retrieving JSON body of new invoice (ID = 0), or invoice to update", response = Invoice.class)
    public Invoice addInvoice(@RequestBody Invoice invoice) throws DatabaseOperationException {
        logger.trace("Request for save invoice. {}", invoice);
        Invoice result;
        try {
            result = invoiceService.saveInvoice(invoice);
            logger.info("Invoice was saved successfully. {}", result);
        } catch (DatabaseOperationException e) {
            logger.error("Unable to obtain invoices from service. {}", e.getLocalizedMessage());
            throw e;
        }
        return result;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Find invoice by its unique ID and delete it", notes = "Retrieving the invoice of ID")
    public void deleteInvoice(@PathVariable Object id) throws DatabaseOperationException {
        logger.trace("Request for remove invoice with id = [{}]", id);
        try {
            invoiceService.deleteInvoice(id);
            logger.info("Invoice with selected id=[{}] has been deleted.", id);
        } catch (DatabaseOperationException e) {
            logger.error("Unable to obtain invoices from service. {}", e.getLocalizedMessage());
            throw e;
        }
    }
}
