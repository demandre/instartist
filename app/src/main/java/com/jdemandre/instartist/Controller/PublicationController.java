package com.jdemandre.instartist.Controller;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jdemandre.instartist.Model.Publication;

public class PublicationController {

    private static final String COLLECTION_NAME = "Publications";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getPublicationsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createPublication(String id, String imageUrl, String description, String author) {
        Publication publicationToCreate = new Publication(id, imageUrl, description, author);
        return PublicationController.getPublicationsCollection().document(id).set(publicationToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getPublication(String id){
        return PublicationController.getPublicationsCollection().document(id).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateDescription(String id, String imageUrl) {
        return PublicationController.getPublicationsCollection().document(id).update("imageUrl", imageUrl);
    }

    public static Task<Void> updateprofilePic(String id, String description) {
        return PublicationController.getPublicationsCollection().document(id).update("description", description);
    }

    public static Task<Void> updateEmail(String id, String author) {
        return PublicationController.getPublicationsCollection().document(id).update("author", author);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String id) {
        return PublicationController.getPublicationsCollection().document(id).delete();
    }

}
