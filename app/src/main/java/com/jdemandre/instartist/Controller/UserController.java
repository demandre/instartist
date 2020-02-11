package com.jdemandre.instartist.Controller;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
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

    public static Task<Void> createUser(String id, String userName, String description, List<Publication> publications, List<String> interests, String profilePic, String email, String phone, double earnings, List<User> following) {
        User userToCreate = new User(id, userName, description, publications, interests, profilePic, email, phone, earnings, following);
        return UserController.getUsersCollection().document(id).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String id) {
        Source source = Source.SERVER;
        return UserController.getUsersCollection().document(id).get(source);
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String id, String username) {
        return UserController.getUsersCollection().document(id).update("userName", username);
    }

    public static Task<Void> updateInterests(String id, String interests) {
        return UserController.getUsersCollection().document(id).update("interests", interests);
    }

    public static Task<Void> updateDescription(String id, String description) {
        return UserController.getUsersCollection().document(id).update("description", description);
    }

    public static Task<Void> updateprofilePic(String id, String profilePic) {
        return UserController.getUsersCollection().document(id).update("profilePic", profilePic);
    }

    public static Task<Void> updateEmail(String id, String email) {
        return UserController.getUsersCollection().document(id).update("email", email);
    }

    public static Task<Void> updatePhone(String id, String phone) {
        return UserController.getUsersCollection().document(id).update("phone", phone);
    }

    public static Task<Void> updateEarnings(String id, float earnings) {
        return UserController.getUsersCollection().document(id).update("earnings", earnings);
    }

    public static Task<Void> updateFollowing(String id, float following) {
        return UserController.getUsersCollection().document(id).update("following", following);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String id) {
        return UserController.getUsersCollection().document(id).delete();
    }


}
