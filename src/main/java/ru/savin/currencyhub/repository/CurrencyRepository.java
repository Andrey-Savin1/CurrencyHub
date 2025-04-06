package ru.savin.currencyhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.savin.currencyhub.model.Currency;

import java.util.List;


public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    @Query(value = "SELECT code FROM currencies WHERE active = true", nativeQuery = true)
    List<String> findAllCodeByActiveIsTrue();

    Currency findByCode(String code);

}
