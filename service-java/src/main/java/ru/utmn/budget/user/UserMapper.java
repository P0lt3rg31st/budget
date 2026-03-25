package ru.utmn.budget.user;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.utmn.budget.model.domain.User;
import ru.utmn.budget.specdto.users.UserCreateRequest;
import ru.utmn.budget.specdto.users.UserDto;
import ru.utmn.budget.specdto.users.UserUpdateRequest;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "budgetPlans", ignore = true)
    @Mapping(target = "forecastRuns", ignore = true)
    @Mapping(target = "alerts", ignore = true)
    @Mapping(target = "scenarios", ignore = true)
    @Mapping(target = "importJobs", ignore = true)
    User toEntity(UserCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "budgetPlans", ignore = true)
    @Mapping(target = "forecastRuns", ignore = true)
    @Mapping(target = "alerts", ignore = true)
    @Mapping(target = "scenarios", ignore = true)
    @Mapping(target = "importJobs", ignore = true)
    void update(UserUpdateRequest request, @MappingTarget User user);
}