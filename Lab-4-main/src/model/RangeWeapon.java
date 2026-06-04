package model;

public class RangeWeapon extends Weapon {
    private int ammunition;
    private int accuracy;

    public RangeWeapon() {
        super("Рогатка", 1, 1, 10);
        this.ammunition = 20;
        this.accuracy = 50;
    }

    public RangeWeapon(String name, int damage, int attackSpeed, int strength, int ammunition, int accuracy) {
        super(name, damage, attackSpeed, strength);
        this.ammunition = ammunition;
        this.accuracy = accuracy;
    }

    @Override
    public AttackResult attack(int distance) {
        if (isBroken() && ammunition == 0) {
            return new AttackResult(0, "Оружие сломано и боеприпасы закончились", false);
        }
        if (ammunition == 0) {
            return new AttackResult(0, "Нет боеприпасов для выстрела", false);
        }
        if (isBroken()) {
            return new AttackResult(0, "Оружие сломано, стрельба невозможна", false);
        }
        strength--;
        ammunition--;
        int totalDamage = damage * attackSpeed * accuracy;
        return new AttackResult(totalDamage,
                "Выстрел! Урон — " + totalDamage + " (патронов осталось — " + ammunition + ")", true);
    }

    @Override
    public String getInfo() {
        return "Тип — Дальнобойное оружие\n"
                + "Урон — " + damage + "\n"
                + "Скорость атаки — " + attackSpeed + "\n"
                + "Прочность — " + strength + "\n"
                + "Боеприпасы — " + ammunition + "\n"
                + "Точность — " + accuracy + "%";
    }
}
