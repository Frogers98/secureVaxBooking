
package app.model;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String dob;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String ppsn;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
    @NotBlank
    private String email;

    // Unsure exactly how to represent appointment id as a foreign key here
    // was following this guide https://www.baeldung.com/jpa-mapping-single-entity-to-multiple-tables
    // but unsure on whether we have it as one to many or one to one. Will wait to continue the model until
    // we know for sure
//
//    @OneToOne
//    @PrimaryKeyJoinColumn(name = "aptId")
//    private long nextAppointmentId;

}