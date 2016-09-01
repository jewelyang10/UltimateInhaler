package monash.ultimateinhaler;

/**
 * Created by jewel on 8/27/16.
 */
public class TabMessage {
    public static String get(int menuItemId, boolean isReselection) {
        String message = "Content for ";

        switch (menuItemId) {
            case R.id.nav_home:
                message += "home";
                break;
            case R.id.nav_hospital:
                message += "map";
                break;
            case R.id.nav_tips:
                message += "tips";
                break;
            case R.id.nav_diary:
                message += "diary";
                break;
            case R.id.nav_prediction:
                message += "prediction";
                break;
        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return message;
    }
}
