package io.github.paulushcgcj.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Entity
@Table(name = "persons",schema = "users")
@Cacheable
@org.hibernate.annotations.Cache(region = "personCache",usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Persistable<String> {

  @Id
  private String id;

  @NotNull
  @NotEmpty
  private String name;

  @NotNull
  @NotEmpty
  @Pattern(regexp = "([a-z])+@([a-zA-Z0-9._-])+\\.([a-z])+", message = "Email should match the pattern (a-z)+@(a-zA-Z0-9._-)+\\.(a-z)+")
  private String email;


  @Transient
  @Builder.Default
  @JsonIgnore
  private boolean newData = false;

  @Override
  @JsonIgnore
  public boolean isNew() {
    return this.newData || id == null;
  }
}
