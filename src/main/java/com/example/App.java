package com.example;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
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

        // persons.forEach(p->System.out.println(p));

        // Si yo tengo el parametro a la izquierda, entonces se conoce que la accion a
        // la derecha va a trabajar con ese parametro, ahi se puede usar metodo por
        // referencia

        // persons.forEach(System.out::println);

        // 1-Filter (param: Predicate)

        // El filter necesita un Predicate, que es una expresión que retorna ture o false

        // maayores a 18
        // a nivel sql seria SELECT * PERSON p WHERE P.edad >= 18;
        //SQL también es declarativo, no le programamos como por dentro el motor de base de datos hace wl where, nos interesa no mas que cumpla la condición, pero en este caso en java
       // en vez de stream() esta tambien parallelStream() que trabaja con hilos internamente, pero hay q ver cuando ocuparlo, a veces no rinde bien, hay que investigar. Pero la programación asincronica  lo reemplaza, miw upm
        List<Person> filteredList = persons.stream().filter(p -> App.getAge(p.getBirthDate()) >= 18)
                                                     .collect(Collectors.toList());
       // App.printList(filteredList);    

       // 2-Map     Pide como parametro una función, no un predicate, la función espera devolver algun parametro o valor segun la función indicada
       
       // transforma elementos de una colección. De un tipo A a un tipo B
      List<Integer> filteredList2 =  persons.stream()
      //.filter(p -> App.getAge(p.getBirthDate()) >= 18)
                .map(p-> App.getAge(p.getBirthDate()) )
                .collect(Collectors.toList());

               // App.printList(filteredList2);  

      List<String> filteredList2String = persons.stream()
                                            .map(p -> "Coders "+p.getName()).collect(Collectors.toList());
        App.printList(filteredList2String);
    }

    public static int getAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    //El simbolo de interrogaci´n es un generico para poder recibir listas de cualquier tipo
   // <?> es lo mismo que <? extend Objects> es un tema de genericos
    public static void printList(List<?> list) {
        list.forEach(System.out::println);
    }
}
