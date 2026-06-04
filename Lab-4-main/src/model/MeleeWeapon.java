package model;

public class MeleeWeapon extends Weapon {
    private int length;

    public MeleeWeapon() {
        super("Камень", 1, 1, 10);
        this.length = 10;
    }

    public MeleeWeapon(String name, int damage, int attackSpeed, int strength, int length) {
        super(name, damage, attackSpeed, strength);
        this.length = length;
    }

    @Override
    public AttackResult attack(int distance) {
        if (isBroken()) {
            return new AttackResult(0, "Оружие сломано, атаковать нельзя", false);
        }
        if (distance > length) {
            return new AttackResult(0, "Манекен слишком далеко — атака невозможна (дистанция "
                    + distance + " > длина " + length + ")", false);
        }
        strength--;
        int totalDamage = damage * attackSpeed;
        return new AttackResult(totalDamage, "Удар нанесён! Урон — " + totalDamage, true);
    }

    @Override
    public String getInfo() {
        return "Тип — Оружие ближнего боя\n"
                + "Урон — " + damage + "\n"
                + "Скорость атаки — " + attackSpeed + "\n"
                + "Прочность — " + strength + "\n"
                + "Длина — " + length;
    }
}