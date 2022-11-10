
package io.github.paulushcgcj.entities.companies;

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
@Table(name = "companies",schema = "company")
@Cacheable
@org.hibernate.annotations.Cache(region = "companyCache",usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company implements Persistable<String> {

  @Id
  private String id;

  @NotNull
  @NotEmpty
  private String name;

  @NotNull
  @NotEmpty
  @Pattern(regexp = "(https?:\\/\\/)?([\\w\\-])+\\.{1}([a-zA-Z]{2,63})([\\/\\w-]*)*\\/?\\??([^#\\n\\r]*)?#?([^\\n\\r]*)",message = "Permalink should be a hyperlink")
  private String permalink;

  @NotNull
  @NotEmpty
  @Pattern(regexp = "([a-z])+@([a-zA-Z0-9._-])+\\.([a-z])+", message = "Email should match the pattern (a-z)+@(a-zA-Z0-9._-)+\\.(a-z)+")
  private String email;

  @NotNull
  @NotEmpty
  @Pattern(regexp = "^\\+?[1-9]([0-9-]){1,14}$",message = "Phone should follow E164 pattern")
  private String phone;

  @NotNull
  @NotEmpty
  private String description;

  @NotNull
  @NotEmpty
  private String overview;

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
