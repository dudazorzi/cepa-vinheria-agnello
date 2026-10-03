package br.com.cepa.data;

import br.com.cepa.model.Wine;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class FirestoreWineRepository implements WineRepository {
    private final Firestore db;
    public FirestoreWineRepository(String projectId) throws Exception {
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .setProjectId(projectId).build();
        FirebaseApp app = FirebaseApp.initializeApp(options, "cepa-web");
        db = FirestoreClient.getFirestore(app);
        if (db.collection("vinhos").limit(1).get().get().isEmpty()) {
            for (Wine wine : SeedWines.all()) save(wine);
        }
    }
    public List<Wine> findAll() throws Exception {
        List<Wine> result = new ArrayList<>();
        for (QueryDocumentSnapshot doc : db.collection("vinhos").get().get().getDocuments()) {
            result.add(Wine.fromMap(doc.getId(), doc.getData()));
        }
        result.sort(Comparator.comparing(Wine::name, String.CASE_INSENSITIVE_ORDER));
        return result;
    }
    public Optional<Wine> findById(String id) throws Exception {
        var doc = db.collection("vinhos").document(id).get().get();
        return doc.exists() ? Optional.of(Wine.fromMap(doc.getId(), doc.getData())) : Optional.empty();
    }
    public void save(Wine wine) throws Exception {
        db.collection("vinhos").document(wine.id()).set(wine.toMap()).get();
    }
    public String mode() { return "Cloud Firestore"; }
}
