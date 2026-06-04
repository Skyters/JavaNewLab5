package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Arsenal {
    private final List<Weapon> weapons = new ArrayList<>();

    public void addWeapon(Weapon weapon) { weapons.add(weapon); }

    public void removeWeapon(int index) {
        if (index >= 0 && index < weapons.size()) {
            weapons.remove(index);
        }
    }

    public Weapon getWeapon(int index) {
        if (index >= 0 && index < weapons.size()) {
            return weapons.get(index);
        }
        return null;
    }

    public List<Weapon> getWeapons()  { return Collections.unmodifiableList(weapons); }
    public int getWeaponCount()       { return weapons.size(); }
}
