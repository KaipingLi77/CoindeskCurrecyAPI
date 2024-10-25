package org.example.currency_price_api.Service;

import org.example.currency_price_api.Model.Currency;
import org.example.currency_price_api.Repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    //查詢所有Currency
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    //藉由id搜尋Currency
    public Currency getCurrencyById (Long id) {
        return currencyRepository.findById(id).map(fetchedCurrency -> {
            return fetchedCurrency;
        }).orElseThrow(() -> new RuntimeException("Fetch currency failed, Currency not found with id: " + id));
    }

    //藉由幣別搜尋Currency
    public Currency getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    //新增or更新Currency
    public Currency saveOrUpdateCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    //更新Currency
    public Currency updateCurrencyById(Long id, Currency currency) {
        return currencyRepository.findById(id).map(oldCurrency -> {
            if(currency.getCode() != null && !currency.getCode().isEmpty()) oldCurrency.setCode(currency.getCode());
            if(currency.getChineseName() != null && !currency.getChineseName().isEmpty()) oldCurrency.setChineseName(currency.getChineseName());
            return  currencyRepository.save(oldCurrency);
        }).orElseThrow(() -> new RuntimeException("Update failed, Currency not found with id: " + id));
    }
    //藉由id刪除Currency
    public Boolean deleteCurrencyById(Long id) {
        return currencyRepository.findById(id).map(currencyToDelete -> {
            currencyRepository.deleteById(id);
            return true;
        }).orElseThrow(() -> new RuntimeException("Delete failed, Currency not found with id: " +id));
    }
}
