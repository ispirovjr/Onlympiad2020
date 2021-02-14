package today.vfu.hospitals;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HospitalService {

    /**
     * Pulls the given department with the given name from the database.
     */
    public Department getDepartmentByName(String name) {

        Department hospitall = null;

        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection
                    ("hospitals").document(name);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                hospitall = document.toObject(Department.class);
                System.out.println(hospitall);
            } else {
                System.out.println("null");
            }
            return hospitall;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,null,e);
        }

    }

}
