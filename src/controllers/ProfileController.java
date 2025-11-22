package controllers;
import java.util.List;
import services.ProfileService;
import views.ProfileView;

import models.User;
import models.Rating;
import models.Ride;


public class ProfileController {

    private ProfileService profileService;
    private ProfileView profileView;
    private User currentUser;

    public ProfileController(ProfileService profileService, User currentUser) {
        this.profileService = profileService;
        this.currentUser = currentUser;
    }

    // CONNECT VIEW TO CONTROLLER
    public void setView(ProfileView view) {
        this.profileView = view;
    }

    // CALLED BY ProfileView AFTER INIT
    public void loadProfile() {
        User user = profileService.getUserProfile(currentUser.getId());
        List<Rating> ratings = profileService.getUserRatings(currentUser.getId());
        List<Ride> rides = profileService.getRideHistory(currentUser);

        profileView.showProfileDetails(user, ratings, rides); // CALL VIEW FUNCTION
    }

    // HANDLE PASSWORD CHANGE
    public void changePassword(String oldPass, String newPass) {
        boolean success = profileService.updatePassword(currentUser.getId(), oldPass, newPass);
        if (success) {
            profileView.showMessage("Password changed successfully!");
        } else {
            profileView.showError("Incorrect old password!");
        }
    }

    // HANDLE EDIT PROFILE
    public void updateProfile(User updatedUser) {
        boolean success = profileService.updateProfile(updatedUser);
        if (success) {
            profileView.showMessage("Profile updated successfully!");
            this.currentUser = updatedUser; // update memory
            loadProfile(); // refresh view
        } else {
            profileView.showError("Error updating profile. Try again.");
        }
    }
}
