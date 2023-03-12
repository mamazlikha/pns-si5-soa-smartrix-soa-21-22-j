package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends MongoRepository<Supplier, String> {
    Optional<Supplier> findByName(String name);

    List<Supplier> findByRegion(String region);
}
