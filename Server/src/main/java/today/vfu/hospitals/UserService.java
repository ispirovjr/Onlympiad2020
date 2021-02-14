package today.vfu.hospitals;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    /**
     * The username (email) of the currently logged in user
     * Invokes getCurrentUser and the getEmail() method.
     *
     * @return String email of the user
     */

    public String getUsersName() {
        return getCurrentUser().getEmail();
    }

    /**
     * Uses the current authentication to get the logged user.
     *
     * @return the User object of the currently logged user.
     */

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        String props = authentication.getPrincipal().toString();
        String email = props.substring(props.indexOf("email=") + 6, props
                .length() - 2);


        User user = new User();


        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection
                    ("users").document(email);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                user = document.toObject(User.class);
           } else {
                System.out.println("User is null");
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,null,e);
        }

        return user;

    }

    boolean isInDataBase() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String props = authentication.getPrincipal().toString();
        String email = props.substring(props.indexOf("email=") + 6, props
                .length() - 2);
        User user;
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection
                    ("users").document(email);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                user = document.toObject(User.class);
                System.out.println(user);
                return true;
            } else {
                 return false;
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,null,e);
        }

    }

}
