package com.example;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.example.model.Person;
import com.example.model.Product;

public class App {
    public static void main(String[] args) {
        Person p1 = new Person(1, "Mito", LocalDate.of(1991, 1, 21));
        Person p2 = new Person(2, "Code", LocalDate.of(1990, 2, 21));
        Person p3 = new Person(3, "Jaime", LocalDate.of(1980, 6, 23));
        Person p4 = new Person(4, "Duke", LocalDate.of(2019, 5, 15));
        Person p5 = new Person(5, "James", LocalDate.of(2010, 1, 14));

        Product pr1 = new Product(1, "Ceviche", 15.0);
        Product pr2 = new Product(2, "Chilaquies", 25.50);
        Product pr3 = new Product(3, "Bandeja Paisa", 35.50);
        Product pr4 = new Product(4, "Ceviche", 15.0);

        List<Person> persons = Arrays.asList(p1, p2, p3, p4, p5);

        List<Product> products = Arrays.asList(pr1, pr2, pr3, pr4);

        // HAY VARIOS PARADIGMAS, PERO SE USAN DE ACUERDO A LA NACESIDAD, Y A VECES HAY
        // QUE SACRIFICAR PERFONMANCE POR PRODUCTIVIDAD O AL REVES

// ES MÁS EFICIENTE TRAER LOS REGISTROS CON LAS QUERY POR BASE DE DATOS QUE TRABAJARLOS EN MEMORIA CON JAVA

        // persons.forEach(p->System.out.println(p));

        // Si yo tengo el parametro a la izquierda, entonces se conoce que la accion a
        // la derecha va a trabajar con ese parametro, ahi se puede usar metodo por
        // referencia

        // persons.forEach(System.out::println);

        // 1-Filter (param: Predicate)

        // El filter necesita un Predicate, que es una expresión que retorna ture o
        // false

        // maayores a 18
        // a nivel sql seria SELECT * PERSON p WHERE P.edad >= 18;
        // SQL también es declarativo, no le programamos como por dentro el motor de
        // base de datos hace wl where, nos interesa no mas que cumpla la condición,
        // pero en este caso en java
        // en vez de stream() esta tambien parallelStream() que trabaja con hilos
        // internamente, pero hay q ver cuando ocuparlo, a veces no rinde bien, hay que
        // investigar. Pero la programación asincronica lo reemplaza, miw upm
        List<Person> filteredList = persons.stream().filter(p -> App.getAge(p.getBirthDate()) >= 18)
                .collect(Collectors.toList());
        // App.printList(filteredList);

        // 2-Map Pide como parametro una función, no un predicate, la función espera
        // devolver algun parametro o valor segun la función indicada

        // transforma elementos de una colección. De un tipo A a un tipo B
        List<Integer> filteredList2 = persons.stream()
                // .filter(p -> App.getAge(p.getBirthDate()) >= 18)
                .map(p -> App.getAge(p.getBirthDate())).collect(Collectors.toList());

        // App.printList(filteredList2);

        // el primer String es el de entrada y el segundo salida
        // Para reciblar lambda
        Function<String, String> coderFunction = name -> "Coder " + name;

        List<String> filteredList2String = persons.stream().map(Person::getName) // p-> p.getName() // sin esto da error
                                                                                 // abajo, porque estaria concatenando
                                                                                 // un objeto persona con un string. aca
                                                                                 // lo transofrmo en cadena de texto
                                                                                 // para concatenar
                .map(coderFunction).collect(Collectors.toList());
        // App.printList(filteredList2String);

        // 3- sorted // para comparar 3mil 4mil datos esta bien, pero ya vienen framew o
        // librerias para gran cantidad de datos como spark
        // Para reciblar comparador
        Comparator<Person> byNameAsc = (Person o1, Person o2) -> o1.getName().compareTo(o2.getName());
        Comparator<Person> byNameDesc = (Person o1, Person o2) -> o2.getName().compareTo(o1.getName());
        Comparator<Person> byBirthDate = (Person o1, Person o2) -> o1.getBirthDate().compareTo(o2.getBirthDate());

        List<Person> filteredList3 = persons.stream()
                // de forma nata puede comparar string y numeros pero no objetos, pero es una
                // lista de objetos, por lo tanto hay que indicarle algun criterio, un
                // comparador como arametro
                .sorted(byBirthDate).collect(Collectors.toList());
        // App.printList(filteredList3);

        // 4- Match (param: Predicate), devuelve un true o false

        // Para reciclar un Predicate, para cada persona que venga yo quiero extrer su
        // nombre y que empieze con "J"
        Predicate<Person> startsWithPredicate = person -> person.getName().startsWith("J");

        // Variantes de Match

        // anyMatch: No evalua todo el stream, termina cuando encuentra una coincidencia

        boolean rpta1 = persons.stream().anyMatch(startsWithPredicate);

        // System.out.println(rpta1);

        // allMatch todos coinciden

        boolean rpta2 = persons.stream().allMatch(startsWithPredicate);
        // System.out.println(rpta1);

        // noneMatch ninguno coinciden

        boolean rpta3 = persons.stream().allMatch(startsWithPredicate);
        // System.out.println(rpta1);

        // 5-Limit/skip
        int pageNumber = 0; // int pageNumber = 1; int pageNumber = 2; paginas de a 2, muestra dos despues
                            // salta dos
        int pageSize = 2;

        // Para simular una paginación con el skip saltamos lo que ya mostramos y con el
        // liit la cantidad de elementos que se van a mostrar por pagina
        // El orden es indistinto, dependiendo de la logica que se quiera programar

        // con pageRequest se podria meter ahi
        List<Person> filteredList4 = persons.stream().skip(pageNumber * pageSize) // salta los dos primeros elementos si
                                                                                  // le comocaramos solo un número 2
                .limit(pageSize) // va a mostrar los dos primeros no más si le colocamos solo un 2.
                .collect(Collectors.toList());

        // App.printList(filteredList4);

        // 6- Collectors

        // GroupBy
        // PUedo devolver el resultado agrupado por precio o nombre , como en SQL
        Map<Double, List<Product>> collect1 = products.stream().filter(p -> p.getPrice() > 20)
                .collect(Collectors.groupingBy(Product::getPrice));

        // System.out.println(collect1);
        // todos los valores
        // System.out.println(collect1.values());
        // todas las entradas

        // System.out.println(collect1.entrySet());
        // System.out.println(collect1.keySet());

        // Counting

        Map<String, Long> collect2 = products.stream().collect(Collectors.groupingBy(
                // le voy a pasar dos criterios, que agrupe primero por nombre y que despues me
                // cuente cada vez que aparece ese nombre

                Product::getName, Collectors.counting()

        ));

        // Hacer esto con codigo imperativo llevaria mucho tiempo y codigo

        //System.out.println(collect2);


        // Agrupando por nombre producto y sumando
// String es el nombre y double es el tipo de retorno
        Map<String, Double> collect3 = products.stream().collect(Collectors.groupingBy(
//Agrupar por nombre y sumar los precio de cada grupo, osea de cada producto
                Product::getName, 
                Collectors.summingDouble(Product::getPrice) // en vez de contarlos vamos a sumarlo

        ));


        //System.out.println(collect3);


        // Obteniendo suma y resumen

      DoubleSummaryStatistics statistics =   products.stream()
                .collect(Collectors.summarizingDouble(Product::getPrice));

                //System.out.println(statistics);
                //System.out.println(statistics.getAverage());

        // 7- Reduce
// El reduce devuelve un optional, es usado para evitar los null exception
Optional<Double> sum = 
        products.stream()
                .map(Product::getPrice)
                .reduce(Double::sum); //reduce(acumulador) el acumulador puede ser una suma total, min , max, promedio 
                // seria igual a .reduce( (a,b) -> a+b)

                System.out.println(sum.get());


    }

    public static int getAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // El simbolo de interrogaci´n es un generico para poder recibir listas de
    // cualquier tipo
    // <?> es lo mismo que <? extend Objects> es un tema de genericos
    public static void printList(List<?> list) {
        list.forEach(System.out::println);
    }
}
