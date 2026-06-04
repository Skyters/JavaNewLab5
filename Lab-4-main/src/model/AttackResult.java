package model;

public class AttackResult {
    private final int damage;
    private final String message;
    private final boolean success;

    public AttackResult(int damage, String message, boolean success) {
        this.damage = damage;
        this.message = message;
        this.success = success;
    }

    public int getDamage()    { return damage; }
    public String getMessage(){ return message; }
    public boolean isSuccess(){ return success; }
}
