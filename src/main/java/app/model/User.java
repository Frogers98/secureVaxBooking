package app.model;

import app.model.forum.Post;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    private static final long OTP_DURATION = 5 * 60 * 1000;   // 5 minutes

    @Id
    @GeneratedValue
    private Long user_id;

    // Each user can have many posts. This is represented by the "user_id" column in the database or the "user"
    // attribute in the Post class
    @OneToMany(mappedBy="user")
    private Set<Post> posts;

    @NotBlank
    @Convert(converter = AttributeEncryptor.class)
    private String dob;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    @Convert(converter = AttributeEncryptor.class)
    private String ppsn;
    @NotBlank
    private String address;
    @NotBlank
    @Convert(converter = AttributeEncryptor.class)
    private String phone;
    @NotBlank
    private String nationality;
    @NotBlank
    private String sex;
    @NotBlank
    @Column(unique = true)
    private String email;
//    @OneToOne(cascade = CascadeType.ALL)
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "apt_id")
    private Appointment apt_id;

    private String dose1;

    private String dose1Date;

    private String dose2;

    private String dose2Date;

    @NotBlank
    private String password;

    @Column(name = "one_time_password")
    private String oneTimePassword;

    @Column(name = "otp_requested_time")
    @DateTimeFormat
    private Date otpRequestedTime;


    public boolean isOTPRequired() {
        if (this.getOneTimePassword() == null) {
            return false;
        }
        // Get current time in milliseconds
        long currentTimeInMilliseconds = System.currentTimeMillis();
        long otpRequestedTimeInMillis = this.otpRequestedTime.getTime();

        // Compare two times, return false if already expired
        if (otpRequestedTimeInMillis + OTP_DURATION < currentTimeInMilliseconds) {
            return false;
        }
        return true;
    }

    public User() {
        super();
    }

    public User(String dob,
                String name,
                String surname,
                String ppsn,
                String address,
                String phone,
                String nationality,
                String sex,
                String email,
                String password) {
        super();
        this.dob = dob;
        this.name = name;
        this.surname = surname;
        this.ppsn = ppsn;
        this.address = address;
        this.phone = phone;
        this.nationality = nationality;
        this.sex = sex;
        this.email = email;
        this.password = password;
    }

    // These columns were added while following the following guide:
    // https://www.codejava.net/frameworks/spring-boot/spring-security-limit-login-attempts-example
    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;
    @Column(name = "failed_attempt")
    private int failedAttempt;
    @Column(name = "lock_time")
    private Date lockTime;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public int getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(int failAttempts) {
        this.failedAttempt = failAttempts;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public Date getLockTime() {
        return lockTime;
    }
    // Attributes need getters and setters
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getDose1() {
        return dose1;
    }

    public void setDose1(String dose1) {
        this.dose1 = dose1;
    }

    public String getDose1Date() {
        return dose1Date;
    }

    public void setDose1Date(String dose1Date) {
        this.dose1Date = dose1Date;
    }

    public String getDose2() {
        return dose2;
    }

    public void setDose2(String dose2) {
        this.dose2 = dose2;
    }

    public String getDose2Date() {
        return dose2Date;
    }

    public void setDose2Date(String dose2Date) {
        this.dose2Date = dose2Date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOneTimePassword() {
        return oneTimePassword;
    }

    public void setOneTimePassword(String oneTimePassword) {
        this.oneTimePassword = oneTimePassword;
    }

    public Date getOtpRequestedTime() {
        return otpRequestedTime;
    }

    public void setOtpRequestedTime(Date otpRequestedTime) {
        this.otpRequestedTime = otpRequestedTime;
    }
}