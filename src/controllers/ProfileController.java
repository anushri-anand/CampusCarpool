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

    public void setView(ProfileView view) {
        this.profileView = view;
    }

    public void loadProfile() {
        User user = profileService.getUserProfile(currentUser.getId());
        List<Rating> ratings = profileService.getUserRatings(currentUser.getId());
        List<Ride> rides = profileService.getRideHistory(currentUser);

        profileView.showProfileDetails(user, ratings, rides); 
    }

    public void changePassword(String oldPass, String newPass) {
        boolean success = profileService.updatePassword(currentUser.getId(), oldPass, newPass);
        if (success) {
            profileView.showMessage("Password changed successfully!");
        } else {
            profileView.showError("Incorrect old password!");
        }
    }

    public void updateProfile(User updatedUser) {
        boolean success = profileService.updateProfile(updatedUser);
        if (success) {
            profileView.showMessage("Profile updated successfully!");
            this.currentUser = updatedUser; 
            loadProfile(); 
        } else {
            profileView.showError("Error updating profile. Try again.");
        }
    }
}
