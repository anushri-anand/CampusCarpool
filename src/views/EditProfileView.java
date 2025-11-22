package views;

import controllers.ProfileController;

import javax.swing.*;

public class EditProfileView extends JFrame {

    public EditProfileView(ProfileController controller) {
        setTitle("Edit Profile");
        setSize(300, 200);
        setLayout(null);

        setVisible(true);
    }
}
