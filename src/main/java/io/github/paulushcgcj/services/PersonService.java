package io.github.paulushcgcj.services;

import io.github.paulushcgcj.entities.users.Person;
import io.github.paulushcgcj.exceptions.CompanyAlreadyExistException;
import io.github.paulushcgcj.exceptions.CompanyNotFoundException;
import io.github.paulushcgcj.exceptions.CompanyPersistenceException;
import io.github.paulushcgcj.exceptions.NullCompanyException;
import io.github.paulushcgcj.repositories.users.PersonRepository;
import io.github.paulushcgcj.validators.PersonValidator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
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
public class PersonService {

  @Getter private final PersonRepository personRepository;

  @Getter private final PersonValidator validator;

  public List<Person> listPersons(long page, long size, String name) {
    log.info("Listing persons {} {} {}", page, size, name);

    Page<Person> personPage = null;

    if (StringUtils.isNotBlank(name))
      personPage = personRepository.findAllByName(name, PageRequest.of((int) page, (int) size));
    else personPage = personRepository.findAll(PageRequest.of((int) page, (int) size));

    return personPage.stream()
        .peek(companies -> log.info("{} persons found", companies))
        .collect(Collectors.toList());
  }

  @Transactional
  public String addPerson(Person person) {
    log.info("Adding person {}", person);
    if (person != null) {
      if (listPersons(0, 1, person.getName()).isEmpty()) {
        String id = savePerson(person.withNewData(true));
        log.info("Person added with ID {}", person);
        return id;
      }
      throw new CompanyAlreadyExistException(person.getName());
    }
    throw new NullCompanyException();
  }

  public Person getPerson(String id) {
    log.info("Searching for person with id {}", id);
    return personRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
  }

  @Transactional
  public void updatePerson(String id, Person person) {
    log.info("Updating person with ID {} to {}", id, person);
    if (person != null) {
      Optional.ofNullable(getPerson(id)).map(person1 -> savePerson(person.withId(id)));
      return;
    }
    throw new NullCompanyException();
  }

  @Transactional
  public void removePerson(String id) {
    log.info("Removing person with id {}", id);
    Optional.ofNullable(getPerson(id)).ifPresent(company -> personRepository.deleteById(id));
  }

  private String savePerson(Person person) {

    Errors errors = new BeanPropertyBindingResult(person, Person.class.getName());
    validator.validate(person, errors);

    if (!errors.hasErrors()) return personRepository.save(person).getId();

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
