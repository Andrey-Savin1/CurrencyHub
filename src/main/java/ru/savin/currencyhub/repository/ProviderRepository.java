package ru.savin.currencyhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.savin.currencyhub.model.RateProvider;

@Repository
public interface ProviderRepository extends JpaRepository<RateProvider, String> {

    @Query(value = "SELECT * FROM rate_providers WHERE provider_code = :code", nativeQuery = true)
    RateProvider findByProviderCode(@Param("code") String code);

}
