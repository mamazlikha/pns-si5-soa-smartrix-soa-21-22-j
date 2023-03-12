package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.RegionProviders;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RegionProvidersRepository extends MongoRepository<RegionProviders, String> {


    public Optional<RegionProviders> findRegionProvidersByRegion(String region);
}
