package io.github.paulushcgcj.repositories;


import io.github.paulushcgcj.entities.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;

public interface CompanyRepository extends PagingAndSortingRepository<Company, String> {

  @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
  Page<Company> findAllByName(String name,Pageable pageable);
}
