package ru.utmn.budget.account;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.utmn.budget.model.domain.Account;
import ru.utmn.budget.specdto.accounts.AccountCreateRequest;
import ru.utmn.budget.specdto.accounts.AccountDto;
import ru.utmn.budget.specdto.accounts.AccountUpdateRequest;
import ru.utmn.budget.specdto.common.CurrencyCode;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AccountMapper {

    AccountDto toDto(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "archived", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    Account toEntity(AccountCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    void update(AccountUpdateRequest request, @MappingTarget Account account);

    default String map(CurrencyCode currencyCode) {
        return currencyCode == null ? null : currencyCode.value();
    }

    default CurrencyCode map(String currency) {
        return currency == null ? null : new CurrencyCode(currency);
    }
}