package com.valorant.backoffice.managers;

import com.valorant.models.ModelFactory;
import com.valorant.models.Weapon;
import com.valorant.repositories.WeaponRepository;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.valorant.backoffice.utils.IOUtils.readLine;

public class WeaponsManager {
    private final PrintStream out;
    private final BufferedReader in;
    private final WeaponRepository weaponRepository;
    private final ModelFactory modelFactory;

    public WeaponsManager(BufferedReader in, PrintStream out, WeaponRepository weaponRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.weaponRepository = weaponRepository;
        this.modelFactory = modelFactory;
    }

    public void start() throws Exception {
        String command;
        do {
            showMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> createWeapon();
                case "2" -> updateWeapon();
                case "3" -> deleteWeapon();
                case "4" -> viewWeapon();
                case "5" -> listWeapons();
                case "back" -> out.println("Returning to main menu...");
                default -> out.println("Invalid command");
            }
        } while (!command.equalsIgnoreCase("back"));
    }

    private void showMenu() {
        out.println("1. Create Weapon");
        out.println("2. Update Weapon");
        out.println("3. Delete Weapon");
        out.println("4. View Weapon");
        out.println("5. List Weapons");
        out.println("Type 'back' to return to the main menu");
    }

    private void createWeapon() {
        out.println("Creating a new Weapon");
        Weapon weapon = modelFactory.createWeapon();
        out.print("Enter Weapon Name: ");
        weapon.setName(readLine(in));
        out.print("Enter Weapon Type: ");
        weapon.setType(readLine(in));
        weaponRepository.save(weapon);
        out.println("Weapon created successfully");
    }

    private void updateWeapon() {
        out.print("Enter Weapon ID to update: ");
        int id = Integer.parseInt(readLine(in));
        Weapon weapon = weaponRepository.get(id);
        if (weapon != null) {
            out.print("Enter new Weapon Name (current: " + weapon.getName() + "): ");
            weapon.setName(readLine(in));
            out.print("Enter new Weapon Type (current: " + weapon.getType() + "): ");
            weapon.setType(readLine(in));
            weaponRepository.save(weapon);
            out.println("Weapon updated successfully");
        } else {
            out.println("Weapon not found");
        }
    }

    private void deleteWeapon() {
        out.print("Enter Weapon ID to delete: ");
        int id = Integer.parseInt(readLine(in));
        Weapon weapon = weaponRepository.get(id);
        if (weapon != null) {
            weaponRepository.delete(weapon);
            out.println("Weapon deleted successfully");
        } else {
            out.println("Weapon not found");
        }
    }

    private void viewWeapon() {
        out.print("Enter Weapon ID to view: ");
        int id = Integer.parseInt(readLine(in));
        Weapon weapon = weaponRepository.get(id);
        if (weapon != null) {
            out.println("Weapon ID: " + weapon.getId());
            out.println("Weapon Name: " + weapon.getName());
            out.println("Weapon Type: " + weapon.getType());
        } else {
            out.println("Weapon not found");
        }
    }

    private void listWeapons() {
        out.println("Listing all weapons");
        List<Weapon> sortedWeapons = weaponRepository.getAll().stream()
                .sorted(Comparator.comparingInt(Weapon::getId))
                .collect(Collectors.toList());
        for (Weapon weapon : sortedWeapons) {
            out.println("Weapon ID: " + weapon.getId());
            out.println("Weapon Name: " + weapon.getName());
            out.println("Weapon Type: " + weapon.getType());
            out.println("----");
        }
    }
}
