package io.github.paulushcgcj.validators;

import io.github.paulushcgcj.entities.companies.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class CompanyValidator implements Validator {
  @Override
  public boolean supports(Class<?> clazz) {
    return Company.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
  }
}
