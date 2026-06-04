package model;

public abstract class Weapon {
    protected String name;
    protected int damage;
    protected int attackSpeed;
    protected int strength;

    public Weapon(String name, int damage, int attackSpeed, int strength) {
        this.name = name;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.strength = strength;
    }

    public Weapon(Weapon other) {
        this.name = other.name;
        this.damage = other.damage;
        this.attackSpeed = other.attackSpeed;
        this.strength = other.strength;
    }

    public String getName()      { return name; }
    public boolean isBroken()    { return strength <= 0; }

    public abstract AttackResult attack(int distance);
    public abstract String getInfo();
}
