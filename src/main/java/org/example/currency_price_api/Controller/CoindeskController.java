package org.example.currency_price_api.Controller;

import org.example.currency_price_api.Service.CoindeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coindesk")
public class CoindeskController {

    @Autowired
    private CoindeskService coindeskService;

    @GetMapping
    public ResponseEntity<String> getCurrencyFromCoindesk() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(coindeskService.getCurrentPriceFromCoindesk());
    }

    @GetMapping("/transformed")
    public ResponseEntity<String> getTransformedCurrencyFromCoindesk() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(coindeskService.getTransformedDataWithCoindesk());
    }

}
