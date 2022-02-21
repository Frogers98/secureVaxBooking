package app;

public class UserAptDetails {
    private String name;
    private String surname;
    private Long id;
    private Long apt_id;
    private Long apt_location;

    @Override
    public String toString() {
        return "UserAptDetails [name=" + name + ", " +
                "surname=" + surname + ", " +
                "id= " + id + ", " +
                "apt_id=" + apt_id + ", " +
                "apt_location" + apt_location + "]";
    }
}
