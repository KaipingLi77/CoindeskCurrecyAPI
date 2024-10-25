package org.example.currency_price_api.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

@Service
public class CoindeskService {

    @Autowired
    private CurrencyService currencyService;

    public String getCurrentPriceFromCoindesk() {
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        //解析JSON並取得返回的資料
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response);

            return jsonNode.toString();
        } catch (Exception e) {
            throw  new RuntimeException("Failed to parse JSON from Coindesk: ", e);
        }
    }

    public String getTransformedDataWithCoindesk() {
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response);

            //取的返回的bpi
            JsonNode bpiNode = jsonNode.get("bpi");
            JsonNode timeNode = jsonNode.get("time");


            //處理返回的更新時間及時間格式
            String updateTime = timeNode.get("updatedISO").asText();
            OffsetDateTime dateTime = OffsetDateTime.parse(updateTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateTime.format(formatter);

            ArrayNode resultData = mapper.createArrayNode();
            Iterator<String> fieldNames = bpiNode.fieldNames();
            while (fieldNames.hasNext()) {
                String code = fieldNames.next();
                JsonNode currentCurrencyInfo = bpiNode.get(code);
                ObjectNode currentData = mapper.createObjectNode();
                currentData.put("code", code);
                currentData.put("chineseName", currencyService.getCurrencyByCode(code).getChineseName());
                currentData.put("rate", currentCurrencyInfo.get("rate").asText());
                resultData.add(currentData);
            }

            return resultData.toString();
        } catch (Exception e) {
            throw  new RuntimeException("Failed to parse JSON from Coindesk: ", e);
        }
    }

}
