package org.example.currency_price_api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.currency_price_api.Controller.CoindeskController;
import org.example.currency_price_api.Service.CoindeskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CoindeskController.class)
public class CoindeskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoindeskService coindeskService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCurrencyFromCoindesk() throws Exception  {
        String mockResponse = "{ \"time\": { \"updated\": \"Oct 25, 2024 09:02:33 UTC\" }, \"bpi\": { \"USD\": { \"code\": \"USD\", \"rate\": \"67,586.575\" } } }";

        // 模擬 CoindeskService 返回的值
        when(coindeskService.getCurrentPriceFromCoindesk()).thenReturn(mockResponse);

        // 使用 MockMvc 發送 GET 請求到 /api/coindesk
        MvcResult response = mockMvc.perform(get("/api/coindesk")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 驗證狀態碼是否為 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // 驗證返回的 Content-Type
                .andExpect(jsonPath("$.bpi.USD.code").value("USD")) // 驗證返回的 JSON 中的 bpi.USD.code 是否為 "USD"
                .andExpect(jsonPath("$.bpi.USD.rate").value("67,586.575")) // 驗證返回的 JSON 中的 bpi.USD.rate 是否為 "67,586.575"
                .andReturn();
        String responseContent = response.getResponse().getContentAsString();
        System.out.println(responseContent);
    }
    @Test
    public void testGetTransformedCurrencyFromCoindesk() throws Exception {
        String mockResponse = "[{\"code\":\"USD\",\"chineseName\":\"美金\",\"rate\":\"67,586.575\"}," +
                "{\"code\":\"GBP\",\"chineseName\":\"英鎊\",\"rate\":\"52,109.52\"}," +
                "{\"code\":\"EUR\",\"chineseName\":\"歐元\",\"rate\":\"62,445.535\"}]";

        when(coindeskService.getTransformedDataWithCoindesk()).thenReturn(mockResponse);

        MvcResult response = mockMvc.perform(get("/api/coindesk/transformed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].code").value("USD")) // 驗證 JSON 返回中的第一個對象的 code 是否是 "USD"
                .andExpect(jsonPath("$[0].chineseName").value("美金")) // 驗證 JSON 返回中的第一個對象的 chineseName 是否是 "美金"
                .andExpect(jsonPath("$[0].rate").value("67,586.575")) // 驗證 JSON 返回中的第一個對象的 rate 是否是 "67,586.575"
                .andExpect(jsonPath("$[1].code").value("GBP")) // 驗證第二個對象的 code 是否是 "GBP"
                .andExpect(jsonPath("$[1].chineseName").value("英鎊")) // 驗證第二個對象的 chineseName 是否是 "英鎊"
                .andExpect(jsonPath("$[2].code").value("EUR")) // 驗證第三個對象的 code 是否是 "EUR"
                .andExpect(jsonPath("$[2].chineseName").value("歐元"))// 驗證第三個對象的 chineseName 是否是 "歐元"
                .andReturn();
        String responseContent = response.getResponse().getContentAsString();
        System.out.println(responseContent);
    }
}
