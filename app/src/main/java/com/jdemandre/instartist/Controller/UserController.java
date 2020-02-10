package com.jdemandre.instartist.Controller;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jdemandre.instartist.Model.Publication;
import com.jdemandre.instartist.Model.User;

import java.util.List;

public class UserController {

    private static final String COLLECTION_NAME = "Users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String userName, String description, List<Publication> publications, List<String> interests, String profilePic, String email, Integer phone, float earnings, List<User> following) {
        User userToCreate = new User(userName, description, publications, interests, profilePic, email, phone, earnings, following);
        return UserController.getUsersCollection().document(userName).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String userName){
        return UserController.getUsersCollection().document(userName).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateDescription(String userName, String description) {
        return UserController.getUsersCollection().document(userName).update("description", description);
    }

    public static Task<Void> updateprofilePic(String userName, String profilePic) {
        return UserController.getUsersCollection().document(userName).update("profilePic", profilePic);
    }

    public static Task<Void> updateEmail(String userName, String email) {
        return UserController.getUsersCollection().document(userName).update("email", email);
    }

    public static Task<Void> updatePhone(String userName, String phone) {
        return UserController.getUsersCollection().document(userName).update("phone", phone);
    }

    public static Task<Void> updateEarnings(String userName, float earnings) {
        return UserController.getUsersCollection().document(userName).update("earnings", earnings);
    }

    public static Task<Void> updateFollowing(String userName, float following) {
        return UserController.getUsersCollection().document(userName).update("following", following);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String userName) {
        return UserController.getUsersCollection().document(userName).delete();
    }


}
