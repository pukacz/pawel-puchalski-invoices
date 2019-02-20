package pl.coderstrust.invoices.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderstrust.invoices.services.InvoiceServiceImplementation;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private InvoiceServiceImplementation service;

  @Test
  public void testGetAllInvoices() throws Exception {
    when(service.getAllInvoices()).thenReturn(new ArrayList<>());
    this.mockMvc.perform(get("/invoices"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("[]")));
  }

//  @Test
//  public void testGetAllInvoicesInDates() throws Exception {
//    LocalDate fromDate = LocalDate.ofEpochDay(2019-01-28);
//    LocalDate toDate = LocalDate.ofEpochDay(2019-01-29);
//    when(service.getAllofRange(fromDate, toDate)).thenReturn(new ArrayList<>());
//    this.mockMvc.perform(get("/invoices/byDates"))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().string(containsString("[]")));
//  }

//  @Test
//  public void testGetInvoiceById() throws Exception {
//    when(service.getInvoiceById(0L)).thenReturn(new Invoice(0, "", "", "", "", ""));
//    this.mockMvc.perform(get("/invoices/0"))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().string(containsString("")));
//  }

//  @Test
//  public void testDeleteInvoiceById() throws Exception {
//    when(service.deleteInvoice(0L)).thenReturn(new Invoice(0, "", "", "", "", ""));
//    this.mockMvc.perform(get("/invoices/0"))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().string(containsString("")));
//  }

}