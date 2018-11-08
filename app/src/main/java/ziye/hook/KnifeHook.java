package ziye.hook;

/**
 * Created by Administrator on 2018/10/24 0024.
 */

public class KnifeHook extends Knife{

    private OnUseWeaponAttackListener onUseWeaponAttackListener;

    @Override
    public void attack() {
        super.attack();
        if (onUseWeaponAttackListener!=null){
            onUseWeaponAttackListener.getKnifeDamage(damage);
        }
    }

    public void setOnUseWeaponAttackListener(OnUseWeaponAttackListener onUseWeaponAttackListener) {
        this.onUseWeaponAttackListener = onUseWeaponAttackListener;
    }


    public interface OnUseWeaponAttackListener{
        int getKnifeDamage(int damage);
    }
}
