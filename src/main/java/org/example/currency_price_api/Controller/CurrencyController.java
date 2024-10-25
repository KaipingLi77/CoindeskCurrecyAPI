package org.example.currency_price_api.Controller;

import org.example.currency_price_api.Model.Currency;
import org.example.currency_price_api.Service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.xml.ws.Response;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<Currency>> getCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrencyById(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.getCurrencyById(id));
    }

    @PostMapping
    public ResponseEntity<Currency> createCurrency(@RequestBody Currency currency) {
        return ResponseEntity.ok(currencyService.saveOrUpdateCurrency(currency));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable Long id, @RequestBody Currency currency) {
        return ResponseEntity.ok(currencyService.updateCurrencyById(id, currency));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCurrency(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.deleteCurrencyById(id));
    }
}
