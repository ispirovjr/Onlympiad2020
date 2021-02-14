package today.vfu.hospitals;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Service
public class FBInitialize {

    /**
     * Initializes the Firebase database
     * serviceAccount should be in resources/serviceAccount
     */
    @PostConstruct
    public void initialize() throws Exception {
        InputStream serviceAccount = FBInitialize.class.getResourceAsStream("/serviceAccount.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);

    }
}
