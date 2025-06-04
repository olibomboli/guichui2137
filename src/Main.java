import view.MainMenuWindow;
import controller.MainMenuController;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuWindow mainMenu = new MainMenuWindow();
            new MainMenuController(mainMenu);
            mainMenu.setVisible(true);
        });
    }
}