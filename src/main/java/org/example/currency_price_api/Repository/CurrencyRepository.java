package org.example.currency_price_api.Repository;

import org.example.currency_price_api.Model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByCode(String code);
}
