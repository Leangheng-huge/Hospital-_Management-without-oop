import java.util.*;

// Doctor class
class Doctor {
    private String name;
    private String specialization;
    private boolean available;

    public Doctor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
        this.available = true;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

// Patient class
class Patient {
    private int id;
    private String name;
    private int age;
    private String disease;
    private String assignedDoctor;
    private int roomNumber;
    private double bill;
    private boolean admitted;
    private boolean discharged;

    public Patient(int id, String name, int age, String disease) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.disease = disease;
        this.assignedDoctor = "Not assigned";
        this.roomNumber = 0;
        this.bill = 0.0;
        this.admitted = false;
        this.discharged = false;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getDisease() { return disease; }
    public String getAssignedDoctor() { return assignedDoctor; }
    public void setAssignedDoctor(String assignedDoctor) { this.assignedDoctor = assignedDoctor; }
    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }
    public double getBill() { return bill; }
    public void setBill(double bill) { this.bill = bill; }
    public boolean isAdmitted() { return admitted; }
    public void setAdmitted(boolean admitted) { this.admitted = admitted; }
    public boolean isDischarged() { return discharged; }
    public void setDischarged(boolean discharged) { this.discharged = discharged; }
}

// Main Hospital Management System
public class HospitalManagement {
    private List<Doctor> doctors;
    private List<Patient> patients;
    private boolean[] rooms; // true = occupied, false = available
    private Scanner scanner;

    public HospitalManagement() {
        // Initialize doctors
        doctors = new ArrayList<>();
        doctors.add(new Doctor("Kim Sabu", "General"));
        doctors.add(new Doctor("Yoon Seo-jung", "Neurology"));
        doctors.add(new Doctor("Kang DOng-ju", "Cardiology"));
        doctors.add(new Doctor("Seo Woo-jin", "Surgery"));
        doctors.add(new Doctor("Cha Eun-jae", "Medicine"));

        // Initialize 10 rooms (101-110)
        rooms = new boolean[10];
        patients = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to Hospital Management System");

        while (true) {
            showMenu();
            int choice = getChoice();

            switch (choice) {
                case 1 -> addPatient();
                case 2 -> assignDoctor();
                case 3 -> admitPatient();
                case 4 -> generateBill();
                case 5 -> dischargePatient();
                case 6 -> viewPatients();
                case 7 -> viewRooms();
                case 8 -> {
                    System.out.println("Thank you for using Hospital Management System!");
                    return;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n===== Hospital Management System =====");
        System.out.println("1. Add Patient");
        System.out.println("2. Assign Doctor");
        System.out.println("3. Admit Patient to Room");
        System.out.println("4. Generate Bill");
        System.out.println("5. Discharge Patient");
        System.out.println("6. View All Patients");
        System.out.println("7. View Room Status");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private int getChoice() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // Clear invalid input
            return -1;
        }
    }

    private void addPatient() {
        System.out.println("\n--- Add New Patient ---");

        System.out.print("Enter Patient ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        // Check if patient ID already exists
        if (findPatient(id) != null) {
            System.out.println("Patient with ID " + id + " already exists!");
            return;
        }

        String name;
        do {
            System.out.println("Please enter the patient's name :");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("****Name cannot be empty. Please enter patient's name again.****");
            }
        } while (name.isEmpty());


        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Disease: ");
        String disease = scanner.nextLine();

        Patient patient = new Patient(id, name, age, disease);
        patients.add(patient);

        System.out.println("Patient added successfully!");
    }

    private void assignDoctor() {
        System.out.println("\n--- Assign Doctor ---");

        System.out.print("Enter Patient ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Patient patient = findActivePatient(id);
        if (patient == null) {
            System.out.println("Patient not found or already discharged!");
            return;
        }

        // Check if patient already has a doctor assigned
        if (!patient.getAssignedDoctor().equals("Not assigned")) {
            System.out.println("Patient already has a doctor assigned: " + patient.getAssignedDoctor());
            return;
        }

        System.out.println("Available Doctors:");
        List<Doctor> availableDoctors = new ArrayList<>();

        // Build list of available doctors and display with correct numbering
        for (Doctor doctor : doctors) {
            if (doctor.isAvailable()) {
                availableDoctors.add(doctor);
            }
        }

        if (availableDoctors.isEmpty()) {
            System.out.println("No doctors available at the moment!");
            return;
        }

        // Display available doctors with correct sequential numbering
        for (int i = 0; i < availableDoctors.size(); i++) {
            Doctor doctor = availableDoctors.get(i);
            System.out.println((i + 1) + ". " + doctor.getName() + " - " + doctor.getSpecialization());
        }

        System.out.print("Choose doctor (enter number): ");
        int choice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (choice >= 0 && choice < availableDoctors.size()) {
            Doctor selectedDoctor = availableDoctors.get(choice);
            patient.setAssignedDoctor(selectedDoctor.getName());
            selectedDoctor.setAvailable(false);
            System.out.println("Doctor " + selectedDoctor.getName() + " assigned to " + patient.getName());
        } else {
            System.out.println("Invalid choice!");
        }
    }

    private void admitPatient() {
        System.out.println("\n--- Admit Patient to Room ---");

        System.out.print("Enter Patient ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Patient patient = findActivePatient(id);
        if (patient == null) {
            System.out.println("Patient not found or already discharged!");
            return;
        }

        if (patient.isAdmitted()) {
            System.out.println("Patient is already admitted to room " + patient.getRoomNumber());
            return;
        }

        showAvailableRooms();

        System.out.print("Enter room number (101-110): ");
        int roomNum = scanner.nextInt();
        scanner.nextLine();

        if (roomNum < 101 || roomNum > 110) {
            System.out.println("Invalid room number!");
            return;
        }

        int roomIndex = roomNum - 101;
        if (rooms[roomIndex]) {
            System.out.println("Room " + roomNum + " is already occupied!");
            return;
        }

        rooms[roomIndex] = true;
        patient.setRoomNumber(roomNum);
        patient.setAdmitted(true);
        System.out.println("Patient " + patient.getName() + " admitted to room " + roomNum);
    }

    private void generateBill() {
        System.out.println("\n--- Generate Bill ---");

        System.out.print("Enter Patient ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Patient patient = findActivePatient(id);
        if (patient == null) {
            System.out.println("Patient not found or already discharged!");
            return;
        }

        double roomCharge = 100.0;
        double medicineCharge = 50.0;
        double consultationCharge = 75.0;
        double total = roomCharge + medicineCharge + consultationCharge;

        patient.setBill(total);

        System.out.println("\n--- HOSPITAL BILL ---");
        System.out.println("Patient: " + patient.getName());
        System.out.println("Room Charge: $" + roomCharge);
        System.out.println("Medicine Charge: $" + medicineCharge);
        System.out.println("Consultation Charge: $" + consultationCharge);
        System.out.println("Total Amount: $" + total);
    }

    private void dischargePatient() {
        System.out.println("\n--- Discharge Patient ---");

        System.out.print("Enter Patient ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Patient patient = findActivePatient(id);
        if (patient == null) {
            System.out.println("Patient not found or already discharged!");
            return;
        }

        // Free up the room
        if (patient.isAdmitted()) {
            int roomIndex = patient.getRoomNumber() - 101;
            rooms[roomIndex] = false;
        }

        // Free up the doctor
        String doctorName = patient.getAssignedDoctor();
        if (!doctorName.equals("Not assigned")) {
            for (Doctor doctor : doctors) {
                if (doctor.getName().equals(doctorName)) {
                    doctor.setAvailable(true);
                    break;
                }
            }
        }

        patient.setDischarged(true);
        patient.setAdmitted(false);

        System.out.println("Patient " + patient.getName() + " has been discharged successfully!");
    }

    private void viewPatients() {
        System.out.println("\n--- All Patients ---");

        if (patients.isEmpty()) {
            System.out.println("No patients in the system!");
            return;
        }

        for (Patient patient : patients) {
            System.out.println("ID: " + patient.getId() + " | Name: " + patient.getName() +
                    " | Age: " + patient.getAge() + " | Disease: " + patient.getDisease());
            System.out.println("Doctor: " + patient.getAssignedDoctor() +
                    " | Room: " + (patient.getRoomNumber() == 0 ? "Not assigned" : patient.getRoomNumber()) +
                    " | Status: " + (patient.isDischarged() ? "Discharged" : "Active"));
            System.out.println("Bill: $" + patient.getBill());
            System.out.println("---");
        }
    }

    private void viewRooms() {
        System.out.println("\n--- Room Status ---");
        for (int i = 0; i < rooms.length; i++) {
            int roomNum = i + 101;
            String status = rooms[i] ? "Occupied" : "Available";
            System.out.println("Room " + roomNum + ": " + status);
        }
    }

    private void showAvailableRooms() {
        System.out.println("Available Rooms:");
        for (int i = 0; i < rooms.length; i++) {
            if (!rooms[i]) {
                System.out.print((i + 101) + " ");
            }
        }
        System.out.println();
    }

    private Patient findPatient(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    private Patient findActivePatient(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id && !patient.isDischarged()) {
                return patient;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        HospitalManagement hospital = new HospitalManagement();
        hospital.start();
    }
}