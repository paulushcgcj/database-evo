package io.github.paulushcgcj.services;

import io.github.paulushcgcj.entities.companies.Company;
import io.github.paulushcgcj.exceptions.CompanyAlreadyExistException;
import io.github.paulushcgcj.exceptions.CompanyNotFoundException;
import io.github.paulushcgcj.exceptions.CompanyPersistenceException;
import io.github.paulushcgcj.exceptions.NullCompanyException;
import io.github.paulushcgcj.repositories.companies.CompanyRepository;
import io.github.paulushcgcj.validators.CompanyValidator;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

  @Getter private final CompanyRepository companyRepository;

  @Getter private final CompanyValidator validator;

  public List<Company> listCompanies(long page, long size, String name) {
    log.info("Listing companies {} {} {}", page, size, name);

    Page<Company> companyPage = null;

    if (StringUtils.isNotBlank(name))
      companyPage = companyRepository.findAllByName(name, PageRequest.of((int) page, (int) size));
    else companyPage = companyRepository.findAll(PageRequest.of((int) page, (int) size));

    return companyPage.stream()
        .peek(companies -> log.info("{} companies found", companies))
        .collect(Collectors.toList());
  }

  @Transactional
  public String addCompany(Company company) {
    log.info("Adding company {}", company);
    if (company != null) {
      if (listCompanies(0, 1, company.getName()).isEmpty()) {
        String id = saveCompany(company.withId(UUID.randomUUID().toString()).withNewData(true));
        log.info("Company added with ID {}", company);
        return id;
      }
      throw new CompanyAlreadyExistException(company.getName());
    }
    throw new NullCompanyException();
  }

  public Company getCompany(String id) {
    log.info("Searching for company with id {}", id);
    return companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
  }

  @Transactional
  public void updateCompany(String id, Company company) {
    log.info("Updating company with ID {} to {}", id, company);
    if (company != null) {
      Optional.ofNullable(getCompany(id)).map(company1 -> saveCompany(company.withId(id)));
      return;
    }
    throw new NullCompanyException();
  }

  @Transactional
  public void removeCompany(String id) {
    log.info("Removing company with id {}", id);
    Optional.ofNullable(getCompany(id)).ifPresent(company -> companyRepository.deleteById(id));
  }

  private String saveCompany(Company company) {

    Errors errors = new BeanPropertyBindingResult(company, Company.class.getName());
    validator.validate(company, errors);

    if (!errors.hasErrors()) return companyRepository.save(company).getId();

    return errors.getAllErrors().stream()
        .map(FieldError.class::cast)
        .map(DefaultMessageSourceResolvable::getCode)
        .reduce((m1, m2) -> String.join(",", new String[] {m1, m2}))
        .map(
            v -> {
              if (StringUtils.isNotBlank(v)) throw new CompanyPersistenceException(v);
              return v;
            })
        .orElseThrow(() -> new CompanyPersistenceException(""));
  }

  private static <T> Consumer<T> logger() {
    return content -> log.info("{}", content);
  }
}
