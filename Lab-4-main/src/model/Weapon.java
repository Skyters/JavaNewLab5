package model;

public abstract class Weapon {
    protected String name;
    protected int damage;
    protected int attackSpeed;
    protected int strength;

    public Weapon() {}

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
    public int getDamage()       { return damage; }
    public int getAttackSpeed()  { return attackSpeed; }
    public int getStrength()     { return strength; }
    public boolean isBroken()    { return strength <= 0; }

    public void setName(String name)       { this.name = name; }
    public void setDamage(int damage)      { this.damage = damage; }
    public void setAttackSpeed(int speed)  { this.attackSpeed = speed; }
    public void setStrength(int strength)  { this.strength = strength; }

    public abstract AttackResult attack(int distance);
    public abstract String getInfo();
}
