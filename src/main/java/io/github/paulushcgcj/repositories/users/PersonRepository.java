package io.github.paulushcgcj.repositories.users;

import io.github.paulushcgcj.entities.companies.Company;
import io.github.paulushcgcj.entities.users.Person;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends CrudRepository<Person,String>, PagingAndSortingRepository<Person,String> {
  @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
  Page<Person> findAllByName(String name, Pageable pageable);
}
