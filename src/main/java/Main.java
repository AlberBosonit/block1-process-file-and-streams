import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Main {
    public static final String SEPARATOR = ":";

    public static void main(String[] args) {

        ArrayList<Person> peopleFromCSVArrayList = peopleFromCSV("src/people.txt");

        showListWithConditions(peopleFromCSVArrayList);

    }
    private static ArrayList<Person> peopleFromCSV(String nameOfFile) {
        ArrayList<Person> personList = new ArrayList<>();
        int numberOfLine = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(nameOfFile))){

            String line = br.readLine();

            while (line != null) {
                numberOfLine++;
                String[] afterSplitting = line.split(SEPARATOR);
                if (afterSplitting.length != 0 && !afterSplitting[0].isBlank()) {
                    Person per = new Person(afterSplitting[0]);
                    per.setTown(afterSplitting.length > 1 ? Optional.of(afterSplitting[1]) : Optional.empty());
                    per.setAge(afterSplitting.length > 2 ? Optional.of(Integer.parseInt(afterSplitting[2])) : Optional.of(0));
                    personList.add(per);
                } else
                    throw new InvalidLineFormatException("No hay nombre / Línea número " + numberOfLine);
                line = br.readLine();
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found.");
        } catch (IOException ioe) {
            System.out.println("I/O Exception.");
        } catch (InvalidLineFormatException invalidLineFormatException) {
            invalidLineFormatException.printStackTrace();
            System.out.println(invalidLineFormatException.getMessage());
        }
        return personList;
    }

    private static void showListWithConditions(ArrayList<Person> peopleFromCSVArrayList) {

        //Muestro toda la lista peopleFromCSVArrayList
        System.out.println("\nAll persons:");
        printListOfPersons(peopleFromCSVArrayList);

        ////////////////////////////////////////////////
        //Creo un flujo a partir de la lista peopleFromCSVArrayList y filtro por menores de 25 años
        Stream<Person> personStream = peopleFromCSVArrayList.stream();
        System.out.println("\nPersons under 25:");
        List<Person> peopleUnder25 = personStream.filter( person -> person.getAge().isPresent())
                        .filter(person -> Integer.parseInt(person.getAge().get().toString()) < 25)
                        .filter(person -> Integer.parseInt(person.getAge().get().toString()) != 0)
                        .toList();

        printListOfPersons(peopleUnder25);
        ////////////////////////////////////////////////
        //Creo un flujo a partir de la lista peopleFromCSVArrayList y filtro por nombre que comience por 'A'
        personStream = peopleFromCSVArrayList.stream();
        System.out.println("\nPersons with names not starting with 'A':");
        List<Person> startingWithA = personStream.filter( person -> person.getName().charAt(0) != 'A'
                ).toList();

        printListOfPersons(startingWithA);
        ////////////////////////////////////////////////
        //Creo un flujo a partir de la lista startingWithA y filtro por la ciudad. Cojo el primer resultado
        //De Madrid
        personStream = startingWithA.stream();
        firstPersonFrom(personStream,"Madrid");
        //De Barcelona
        personStream = startingWithA.stream();
        firstPersonFrom(personStream,"Barcelona");
    }

    private static void firstPersonFrom(Stream<Person> personStream, String town) {
        System.out.println("\nFirst person from '" + town + "':");
        List<Person> firstFromTown = personStream.filter( person -> person.getTown().isPresent())
                .filter( person -> person.getTown().get().equals(town))
                .findFirst().stream().toList();
        if(!firstFromTown.isEmpty())
            printListOfPersons(firstFromTown);
        else
            System.out.println("There is no one from '" + town + "'.");
    }

    private static void printListOfPersons(List<Person> peopleFromCSVArrayList) {
        peopleFromCSVArrayList.forEach(person -> {
            System.out.print("Name: " + person.getName());
            if (person.getTown().isPresent())
                System.out.printf((". Town = " + (person.getTown().get().isEmpty() ? "Unknown" : person.getTown().get())));
            if (person.getAge().isPresent())
                System.out.printf(". Age = " + (person.getAge().get() == 0 ? "Unknown" : person.getAge().get()) + "\n");
        });
    }


}