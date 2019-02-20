package pl.coderstrust;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.coderstrust.invoices.controller.InvoiceController;
import pl.coderstrust.invoices.database.DatabaseOperationException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private InvoiceController controller;

    @Test
    public void test() throws DatabaseOperationException {
        assertThat(controller).isNotNull();
    }

}
