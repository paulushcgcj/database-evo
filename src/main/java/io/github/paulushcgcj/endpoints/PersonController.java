package io.github.paulushcgcj.endpoints;

import io.github.paulushcgcj.entities.users.Person;
import io.github.paulushcgcj.services.PersonService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

  private final PersonService service;

  @GetMapping
  public List<Person> listPersons(
      @RequestParam(required = false, defaultValue = "0") long page,
      @RequestParam(required = false, defaultValue = "10") long size,
      @RequestParam(required = false) String name) {
    return service.listPersons(page, size, name);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void addPerson(@RequestBody @Valid Person per, HttpServletResponse response) {
    String id = service.addPerson(per);
    response.addHeader("Location", String.format("/api/persons/%s", id));
  }

  @GetMapping("/{id}")
  public Person getPerson(@PathVariable String id) {
    return service.getPerson(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updatePerson(@PathVariable String id, @RequestBody Person person) {
    service.updatePerson(id, person);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removePerson(@PathVariable String id) {
    service.removePerson(id);
  }
}
