import java.util.Optional;

public class Person {
    private String name;
    private Optional<String> town;
    private Optional<Integer> age;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public Optional<String> getTown() {
        return town;
    }
    public void setTown(Optional<String> town) {
        this.town = town;
    }
    public Optional<Integer> getAge() {
        return age;
    }
    public void setAge(Optional<Integer> age) {
        this.age = age;
    }
}
