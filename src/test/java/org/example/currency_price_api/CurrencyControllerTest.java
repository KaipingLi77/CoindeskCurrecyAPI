package org.example.currency_price_api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.currency_price_api.Controller.CurrencyController;
import org.example.currency_price_api.Model.Currency;
import org.example.currency_price_api.Service.CurrencyService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //1. 測試呼叫查詢幣別對應表資料 API,並顯示其內容。
    @Test
    void testGetCurrencyById() throws Exception {
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setCode("USD");
        currency.setChineseName("美元");

        when(currencyService.getCurrencyById(1L)).thenReturn(currency);

        MvcResult result = mockMvc.perform(get("/api/currency/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.chineseName").value("美元"))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println(responseContent);
    }

    //2. 測試呼叫新增幣別對應表資料 API。
    @Test
    void testAddCurrency() throws Exception {
        Currency currency = new Currency();
        currency.setCode("GBP");
        currency.setChineseName("英鎊");

        when(currencyService.saveOrUpdateCurrency(any(Currency.class))).thenReturn(currency);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(currency);

        mockMvc.perform(post("/api/currency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("GBP"))
                .andExpect(jsonPath("$.chineseName").value("英鎊"));
    }

    //3. 測試呼叫更新幣別對應表資料 API,並顯示其內容。
    @Test
    void testUpdateCurrency() throws Exception {
        Currency updateCurrency = new Currency();
        updateCurrency.setId(1L);
        updateCurrency.setCode("EUR");
        updateCurrency.setChineseName("歐元");

        when(currencyService.updateCurrencyById(eq(1L), any(Currency.class))).thenReturn(updateCurrency);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(updateCurrency);

        MvcResult result = mockMvc.perform(put("/api/currency/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("EUR"))
                    .andExpect(jsonPath("$.chineseName").value("歐元"))
                    .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        System.out.println(responseContent);
    }

    @Test
    void testDeleteCurrencyById() throws Exception {

        when(currencyService.deleteCurrencyById(eq(1L))).thenReturn(true);

        mockMvc.perform(delete("/api/currency/1"))
                .andExpect(status().isOk());
    }
}
