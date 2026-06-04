package controller;

import model.Arsenal;
import model.AttackResult;
import model.MeleeWeapon;
import model.RangeWeapon;
import model.Weapon;

import java.util.List;

public class WeaponController {
    private static final int ATTACK_DISTANCE   = 5;
    private static final int MANNEQUIN_DEFENSE = 100;

    private final Arsenal arsenal;
    private Weapon selectedWeapon;
    private int selectedIndex = -1;

    public WeaponController() {
        arsenal = new Arsenal();
        arsenal.addWeapon(new MeleeWeapon());
        arsenal.addWeapon(new RangeWeapon());
    }

    public void addMeleeWeapon(String name, int damage, int attackSpeed, int strength, int length) {
        arsenal.addWeapon(new MeleeWeapon(name, damage, attackSpeed, strength, length));
    }

    public void addRangeWeapon(String name, int damage, int attackSpeed, int strength, int ammunition, int accuracy) {
        arsenal.addWeapon(new RangeWeapon(name, damage, attackSpeed, strength, ammunition, accuracy));
    }

    public void selectWeapon(int index) {
        selectedWeapon = arsenal.getWeapon(index);
        if (selectedWeapon != null) {
            selectedIndex = index;
        } else {
            selectedIndex = -1;
        }
    }

    public void removeWeapon(int index) {
        if (selectedIndex == index) {
            selectedWeapon = null;
            selectedIndex  = -1;
        } else if (selectedIndex > index) {
            selectedIndex--;
        }
        arsenal.removeWeapon(index);
    }

    public String attack() {
        if (selectedWeapon == null) {
            return "Оружие не выбрано!";
        }
        AttackResult result = selectedWeapon.attack(ATTACK_DISTANCE);
        if (!result.isSuccess()) {
            return result.getMessage();
        }
        int damage = result.getDamage();
        String penetration;
        if (damage >= MANNEQUIN_DEFENSE) {
            penetration = "Броня пробита! Манекен получил урон.";
        } else {
            penetration = "Броня выдержала. Урон поглощён (нужно "
                    + MANNEQUIN_DEFENSE + ", нанесено " + damage + ").";
        }
        return result.getMessage() + "\n" + penetration;
    }

    public Weapon getSelectedWeapon() { return selectedWeapon; }
    public List<Weapon> getWeapons()  { return arsenal.getWeapons(); }
    public int getWeaponCount()       { return arsenal.getWeaponCount(); }
}