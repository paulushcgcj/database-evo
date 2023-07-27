package io.github.paulushcgcj.repositories.companies;


import io.github.paulushcgcj.entities.companies.Company;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyRepository extends CrudRepository<Company,String>, PagingAndSortingRepository<Company, String> {

  @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
  Page<Company> findAllByName(String name, Pageable pageable);
}
