package app.model;

import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class Data {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column
    private String dob;

    @Column
    private String sex;

    @Column
    private String nationality;

    public Long getId() {
        return user_id;
    }

    public void setId(Long id) {
        this.user_id = id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        nationality = nationality;
    }

    @Override
    public String toString() {
        return "data{" +
                "id=" + user_id +
                ", dob='" + dob + '\'' +
                ", sex='" + sex + '\'' +
                ", Nationality='" + nationality + '\'' +
                '}';
    }
}
