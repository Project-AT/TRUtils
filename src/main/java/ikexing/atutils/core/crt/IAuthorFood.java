package ikexing.atutils.core.crt;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import ikexing.atutils.core.item.AuthorFood;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.atutils.IAuthorFood")
public class IAuthorFood {

    // need PreInit to reg
    @ZenMethod
    public static void addAuthor(String number, String author, int healAmount, float saturationModifier) {
        CraftTweakerAPI.apply(new IAction() {
            @Override public void apply() {
                AuthorFood.AUTHOR_QQ_NUMBER.add(AuthorFood.AuthorInformation.of(number, author, healAmount, saturationModifier));
            }

            @Override public String describe() {
                return "Add author food " + author + " " + healAmount + " " + saturationModifier;
            }
        });
    }

}
