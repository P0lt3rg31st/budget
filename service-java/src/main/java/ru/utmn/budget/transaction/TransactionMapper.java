package ru.utmn.budget.transaction;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.utmn.budget.model.domain.Transaction;
import ru.utmn.budget.specdto.transactions.TransactionCreateRequest;
import ru.utmn.budget.specdto.transactions.TransactionDto;
import ru.utmn.budget.specdto.transactions.TransactionUpdateRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TransactionMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "categoryId", source = "category.id")
    TransactionDto toDto(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Transaction toEntity(TransactionCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(TransactionUpdateRequest request, @MappingTarget Transaction transaction);

    default OffsetDateTime map(Instant value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }

    default Instant map(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }

    default String map(BigDecimal value) {
        return value == null ? null : value.stripTrailingZeros().toPlainString();
    }

    default BigDecimal map(String value) {
        return value == null || value.isBlank() ? null : new BigDecimal(value);
    }
}