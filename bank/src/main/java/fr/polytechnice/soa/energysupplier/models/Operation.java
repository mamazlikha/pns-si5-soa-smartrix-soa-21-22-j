package fr.polytechnice.soa.energysupplier.models;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public record Operation(@NotNull BankAccount from, @NotNull BankAccount to, @PositiveOrZero double amount,
        LocalDateTime date) {

}
