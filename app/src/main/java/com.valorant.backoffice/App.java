package com.valorant.backoffice;

import com.valorant.backoffice.managers.*;
import com.valorant.models.ModelFactory;
import com.valorant.repositories.RepositoryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = System.out;

        // Display the banner
        displayBanner(out);

        Properties properties = new Properties();
        properties.load(App.class.getResourceAsStream("/app.properties"));

        RepositoryFactory repositoryFactory;
        ModelFactory modelFactory = (ModelFactory) Class.forName(properties.getProperty("modelFactory")).getDeclaredConstructor().newInstance();

        String repositoryFactoryClassName = properties.getProperty("repositoryFactory");
        if (repositoryFactoryClassName.contains("JpaRepositoryFactory")) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("valorant-mysql");
            EntityManager entityManager = emf.createEntityManager();
            repositoryFactory = (RepositoryFactory) Class.forName(repositoryFactoryClassName)
                    .getConstructor(EntityManager.class)
                    .newInstance(entityManager);
        } else {
            repositoryFactory = (RepositoryFactory) Class.forName(repositoryFactoryClassName).getDeclaredConstructor().newInstance();
        }

        BackOffice backOffice = new BackOffice(System.in, System.out, repositoryFactory, modelFactory);
        backOffice.start();
    }

    private static void displayBanner(PrintStream out) {
        try {
            Files.lines(Paths.get(App.class.getResource("/banner.txt").toURI()))
                    .forEach(out::println);
        } catch (Exception e) {
            out.println("Welcome to the Valorant Back Office");
        }
    }
}
