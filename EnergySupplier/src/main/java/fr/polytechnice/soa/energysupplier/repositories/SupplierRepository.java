package fr.polytechnice.soa.energysupplier.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.polytechnice.soa.energysupplier.models.Supplier;

public interface SupplierRepository extends MongoRepository<Supplier,String>{
    Optional<Supplier> findByName(String name);
}
