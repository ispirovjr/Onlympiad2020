package today.vfu.hospitals;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class AuditService {

    /**
     * Uploads a given audit to Firebase.
     * It's named as the department name + the date of change a UUID
     * so that collisions are impossible.
     */
    public void uploadAudit(Audit audit) {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        String name = audit.getChangedDepartament() + " " + audit
                .getDateOfChange() + " " + UUID.randomUUID();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection
                ("audit").document(name).set(audit);
        try {
            System.out.println(collectionsApiFuture.get().getUpdateTime()
                    .toString());
        } catch (InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,null,e);
        } catch (ExecutionException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,null,e);
        }

    }

}
