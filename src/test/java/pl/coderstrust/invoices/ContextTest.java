package pl.coderstrust.invoices;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.coderstrust.invoices.controllers.InvoiceController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContextTest {

  @Autowired
  private InvoiceController controller;

  @Test
  public void contextLoads() throws Exception {
    assertThat(controller).isNotNull();
  }

}
