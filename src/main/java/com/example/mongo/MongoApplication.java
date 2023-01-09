package com.example.mongo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MongoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate) {
        return args -> {
            Address address = new Address(
                    "Ghana",
                    "Accra",
                    "MC"
            );
            String email = "ckoomson75@gmail.com";
            Student student = new Student(
                    "clement",
                    "koomson",
                    email,
                    Gender.Male,
                    address,
                    List.of("ComputerScience", "Account"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );

            //usingMongoTemplate(repository, mongoTemplate, email, student);

            repository.findStudentByEmail(email).ifPresentOrElse(stu -> {
                System.out.println("Student already exist " + stu);
            }, () -> {
                System.out.println("Inserting student " + student);
                repository.insert(student);
            });


        };
    }

    private static void usingMongoTemplate(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
        //filter by query

        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 1) {
            throw new IllegalStateException("found many student with the same email");
        }

        if (students.isEmpty()) {
            System.out.println("inserting student " + student);
            repository.insert(student);
        } else {
            System.out.println("student all ready exist " + student);
        }
    }
}
