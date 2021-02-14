package today.vfu.hospitals;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    HospitalService service;
    @Autowired
    UserService uService;


    //    @GetMapping("/createUser")
    public String createUser() {

        User user = new User("i.spirov21@acsbg.org", "Pirogov",
                "103759937935907005566");


        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection
                ("users").document(user.getEmail()).set(user);

        return user.toString();
    }


    /**
     * Gets the hospital that the current
     * user is managing from the getHospital() method.
     * Checks if the user is registered and if not,
     * throws an exception.
     *
     * @return String hospitalName
     */

    @GetMapping("/hos-name-user")
    public String getHospitalNameFromCurrentUser() {


        User currentUser = uService.getCurrentUser();
        if (currentUser == null) {
            return null;
        }

        String hospital = currentUser.getHospital();

        if (hospital.equals("All")) {
            //superAdmin
        }

        if (hospital == null) {
            throw new AccessDeniedException("User not registered as admin");
        }

        return hospital;

    }


    /**
     * In most cases the user would get a permission error.
     * This blankets all other cases.
     *
     * @return error statement
     */

    @GetMapping("/error")
    public String error() {
        return "Unexpected error occured";
    }


}
