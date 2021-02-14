package today.vfu.hospitals;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class HospitalController {

    @Autowired
    HospitalService service;
    @Autowired
    AuditService auditService;
    @Autowired
    UserService userService;

    /**
     * Root page of the site.
     *
     * @return a welcome message
     */
    @GetMapping("/")
    public String hello() {
        return "Welcome to the hospital management system!";
    }

    /**
     * @return a hospital with the given name
     */
    @GetMapping("/hospital/{name}")
    public Department getHospitalByName(@PathVariable(value = "name") String
                                                    name) {
        return service.getDepartmentByName(name);
    }

    /**
     * Used either as Superadmin or for debugging
     *
     * @return the names of all parent hospitals
     */
    @GetMapping("/all-hos-names")
    public ArrayList<String> getHospitalNames() {
        ArrayList<Department> deps = getAllDepartments();

        ArrayList<String> names = new ArrayList<>();


        for (Department dep : deps) {
            if (!names.contains(dep.getHospital())) {
                names.add(dep.getHospital());
            }
        }

        Collections.sort(names);

        return names;
    }

    /**
     * Used either as Superadmin or for debugging
     *
     * @return the names of all departments
     */
    @GetMapping("/all-dep-names")
    public ArrayList<String> getDepartamentNames() {
        ArrayList<Department> deps = getAllDepartments();

        ArrayList<String> names = new ArrayList<>();


        for (Department dep : deps) {
            names.add(dep.getName());
        }

        Collections.sort(names);

        return names;
    }

    /**
     * Gets all departments with more than 0 free beds and makes sure they
     * are sorted.
     *
     * @return all hospitals with more than 0 free beds
     */
    @GetMapping("/avaliableHospitals")
    public ArrayList<Department> getFreeDepsOrdered() {
        ArrayList<Department> hosps = getFreeHospitals();


        hosps.sort(Comparator.comparing(Department::getFreebeds));

        Collections.reverse(hosps);


        return hosps;
    }


    ArrayList<Department> getFreeHospitals() {

        ArrayList<Department> hosps = new ArrayList<>();
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();

            Query query = dbFirestore.collection("hospitals").orderBy
                    ("freebeds", Query.Direction.DESCENDING)
                    .whereGreaterThan("freebeds", 0);
            List<QueryDocumentSnapshot> i = query.get().get().getDocuments();

            for (DocumentSnapshot document : i) {
                Department hospitall = null;
                if (document.exists()) {
                    hospitall = document.toObject(Department.class);

                    if (hospitall.getFreebeds() > 0) {
                        hosps.add(hospitall);
                    }
                } else {
                    System.out.println("null");
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,null,e);
        }
        return hosps;
    }

    /**
     * @param hospital name of the parent hospital
     * @return all departments belonging to that hospital
     */
    @GetMapping("/deps-from-name/{name}")
    public ArrayList<Department> getDepsByHospital(@PathVariable(value =
            "name") String hospital) {
        ArrayList<Department> deps = getAllDepartments();
        ArrayList<Department> hospitalDeps = new ArrayList();

        for (Department dep : deps) {
            if (dep.getHospital().equals(hospital)) {
                hospitalDeps.add(dep);
            }
        }



        return hospitalDeps;

    }

    /**
     * Returns all departments.
     * Used either for debugging or as superadmin.
     *
     * @return all existing departments.
     * ! May overload momory if database is too large!
     */
    @GetMapping("/all-hospitals")
    public ArrayList<Department> getAllDepartments() {
        ArrayList<Department> hosps = new ArrayList<>();
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ArrayList<DocumentReference> documentReferences = new ArrayList<>();

            Iterable<DocumentReference> iterable = dbFirestore.collection
                    ("hospitals").listDocuments();

            for (DocumentReference docRef : iterable
                    ) {
                documentReferences.add(docRef);

            }

            ArrayList<DocumentSnapshot> docs = new ArrayList<>();

            for (DocumentReference documentReference : documentReferences) {
                ApiFuture<DocumentSnapshot> future = documentReference.get();
                DocumentSnapshot document = future.get();
                docs.add(document);
            }
            for (DocumentSnapshot document : docs) {
                Department department = null;
                if (document.exists()) {
                    department = document.toObject(Department.class);
                    hosps.add(department);
                } else {
                 }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,null,e);
        }
        return hosps;
    }


    /**
     * Updates a department. If a department with the given name exists an
     * audit with the previous state is generated.
     * Otherwise the previous state is blank.
     *
     * @param department is the new department state.
     */
    @PostMapping("/addition")
    public void postDepartment(@RequestBody Department department) {


        if (department.getFreebeds() < 0 || department.getOccupied() < 0 ||
                department.getTotal() < 1) {
            System.err.println("Invalid department data imputed! - " +
                    department);
            return;
        }

        Firestore dbFirestore = FirestoreClient.getFirestore();

        try {
            if (dbFirestore.collection("hospitals").document(department
                    .getName()).get().get().exists()) {

                Department oldDep = dbFirestore.collection("hospitals")
                        .document(department.getName()).get().get().toObject
                                (Department.class);

                Audit audit = new Audit(oldDep, department, userService
                        .getCurrentUser().getEmail());
                auditService.uploadAudit(audit);


            } else {
                Department oldDep = new Department();
                Audit audit = new Audit(oldDep, department, userService
                        .getCurrentUser().getEmail());
                auditService.uploadAudit(audit);


            }
        } catch (InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,null,e);
        } catch (ExecutionException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,null,e);
        }

        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection
                ("hospitals").document(department.getName()).set(department);

    }


    /**
     * Returns a report of Audits from Firebase sorted by date
     *
     * @param num          is the number of Audits to be returned
     * @param startingFrom Index of the first Audit to be returned
     * @return returns an ArrayList of Audits num long
     */

    @GetMapping("/Audit/{num}/{starting}")
    public ArrayList<Audit> getLastXAudits(@PathVariable(value = "num") int
                                                       num, @PathVariable
            (value = "starting") Integer startingFrom) {

        System.out.println("Recieved: " + num + " " + startingFrom);

        boolean isAuthorized = userService.isInDataBase();

        ArrayList<Audit> audits = new ArrayList<>();
        if (startingFrom == null) {
            startingFrom = 0;
        }
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            Query query = dbFirestore.collection("audit").orderBy
                    ("dateInMils", Query.Direction.DESCENDING).offset
                    (startingFrom).limit(num);
            List<QueryDocumentSnapshot> i = query.get().get().getDocuments();


            for (DocumentSnapshot document : i) {
                Audit audit = null;
                if (document.exists()) {
                    audit = document.toObject(Audit.class);
                    audits.add(audit);
                } else {
                    System.out.println("null");
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,null,e);
        }
        audits.sort(Comparator.comparing(Audit::getDateInMils));
        Collections.reverse(audits);


        if (!isAuthorized) {
            for (Audit audit : audits) {
                audit.hideUser();
            }
        }


        return audits;
    }

}
