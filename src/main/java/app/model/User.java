package app.model;

import javax.persistence.*;
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
    @Column(unique = true)
    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "apt_id")
    private Appointment apt_id;

    private String dose1Date;

    private String dose2Date;

    private String lastLogin;
    @NotBlank
    private String password;

    public User() {
        super();
    }

    public User(String dob,
                String name,
                String surname,
                String ppsn,
                String address,
                String phone,
                String email,
                String password) {
        super();
        this.dob = dob;
        this.name = name;
        this.surname = surname;
        this.ppsn = ppsn;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    // Some attributes don't have setter methods as they should immutable once created initially (e.g. ppsn, name etc.)
    public Long getId() {
        return id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPpsn() {
        return ppsn;
    }

    public void setPpsn(String ppsn) {
        this.ppsn = ppsn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Appointment getNextApptId() {
        return this.apt_id;
    }

    public void setNextApptId(Appointment appointment) {

        this.apt_id = appointment;
    }

    public String getDose1Date() {
        return dose1Date;
    }

    public void setDose1Date(String dose1Date) {
        this.dose1Date = dose1Date;
    }

    public String getDose2Date() {
        return dose2Date;
    }

    public void setDose2Date(String dose2Date) {
        this.dose2Date = dose2Date;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}