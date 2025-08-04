import java.util.Scanner;
public class HospitalManagementWithoutOOP {
    public static void main(String[] arg){
        Scanner s = new Scanner(System.in);

        int[] Patient_Id = new int[25];
        String[] Patient_Name = new String[25]; // Changed from 60 to match other arrays
        int[] Patient_Age = new int[25];
        String[] Patient_Disease = new String[25];
        boolean[] patientDischarged = new boolean[25]; // Added missing array
        int[] patientRoomNumbers = new int[25]; // Added missing array
        int PatientCount = 0;
        int option = 0;
        int age = 0;
        boolean[] available_room = new boolean[10]; // 101-110
        // Initialize all rooms as available
        for (int i = 0; i < available_room.length; i++) {
            available_room[i] = true;
        }

        // Doctor-related arrays
        String[] doctorSpecialties = {"heart", "lung", "cancer", "general"};
        boolean[] doctorAvailable = {true, true, true, true};
        int[] assignedDoctors = new int[25]; // For each patient, stores their assigned doctor index

        for (int i = 0; i < assignedDoctors.length; i++) {
            assignedDoctors[i] = -1; // -1 means no doctor assigned
        }

        int id;
        String disease = "";
        int room;
        boolean patientFound = false;
        int patientIndex;
        double[] patientBills = new double[25];

        do{
            System.out.println("=====Hospital System====");
            System.out.println("1 -> : Register Patient ");
            System.out.println("2 -> : Assign Doctor ");
            System.out.println("3 -> : Check-In Patient to Room ");
            System.out.println("4 -> : Bill for Patient ");
            System.out.println("5 -> : Discharge Patient ");
            System.out.println("6 -> : Patient's Information ");
            System.out.println("7 -> : Exit the System ");
            System.out.print("Input option : ");
            option = s.nextInt();
            s.nextLine();

            switch (option){
                case 1 -> {
                    // Register Patient
                    String name;

                    System.out.println("Please enter the patient's id ");
                    id = s.nextInt();
                    s.nextLine();

                    do {
                        System.out.println("Please enter the patient's name :");
                        name = s.nextLine().trim();
                        if (name.isEmpty()) {
                            System.out.println("****Name cannot be empty. Please enter patient's name again.****");
                        }
                    } while (name.isEmpty());

                    do {
                        System.out.println("Please Enter Age : ");
                        age = s.nextInt();
                        s.nextLine();

                        if (age < 0) {
                            System.out.println("****Age cannot be negative. Please enter a valid age.****");
                        }
                    } while (age < 0);

                    do {
                        System.out.println("Please enter the patient disease : ");
                        disease = s.nextLine().trim();

                        if (disease.isEmpty()){
                            System.out.println("****Disease cannot be empty. Please enter patient's disease again.****");
                        }
                    } while (disease.isEmpty());

                    // Save patient information
                    Patient_Id[PatientCount] = id;
                    Patient_Name[PatientCount] = name;
                    Patient_Age[PatientCount] = age;
                    Patient_Disease[PatientCount] = disease;
                    patientDischarged[PatientCount] = false;
                    patientRoomNumbers[PatientCount] = -1; // -1 means no room assigned yet

                    System.out.println("Patient registered successfully!");
                    PatientCount++;
                }
                case 2->{
                    // Assign Doctor
                    System.out.println("Enter the patient's id : ");
                    id = s.nextInt();
                    s.nextLine();

                    patientFound = false;
                    patientIndex = -1;

                    for (int i = 0; i < PatientCount; i++) {
                        if (Patient_Id[i] == id && !patientDischarged[i]) {
                            patientFound = true;
                            patientIndex = i;
                            break;
                        }
                    }

                    if (patientFound) {
                        System.out.println("Patient exists in the system");

                        String specialization;
                        do {
                            System.out.println("Enter Dr. Specialization (Heart/Lung/Cancer/General) : ");
                            specialization = s.nextLine().toLowerCase();

                            if (!specialization.equals("heart") && !specialization.equals("lung")
                                    && !specialization.equals("cancer") && !specialization.equals("general")) {
                                System.out.println("Invalid specialization. Please try again.");
                            }
                        } while (!specialization.equals("heart") && !specialization.equals("lung")
                                && !specialization.equals("cancer") && !specialization.equals("general"));

                        // Find doctor index
                        int doctorIndex = -1;
                        for (int i = 0; i < doctorSpecialties.length; i++) {
                            if (doctorSpecialties[i].equals(specialization) && doctorAvailable[i]) {
                                doctorIndex = i;
                                break;
                            }
                        }

                        if (doctorIndex != -1) {
                            doctorAvailable[doctorIndex] = false; // Mark doctor as unavailable
                            assignedDoctors[patientIndex] = doctorIndex; // Assign doctor to patient
                            System.out.println(specialization.substring(0, 1).toUpperCase() + specialization.substring(1) +
                                    " specialist assigned to patient " + Patient_Name[patientIndex]);
                        } else {
                            System.out.println("No " + specialization + " specialist is currently available.");
                        }
                    } else {
                        System.out.println("Patient doesn't exist in the system or has been discharged.");
                    }
                }
                case 3-> {
                    // Check-In Patient to Room
                    System.out.println("Enter patient ID: ");
                    id = s.nextInt();
                    s.nextLine();

                    patientFound = false;
                    patientIndex = -1;

                    for (int i = 0; i < PatientCount; i++) {
                        if (Patient_Id[i] == id && !patientDischarged[i]) {
                            patientFound = true;
                            patientIndex = i;
                            break;
                        }
                    }

                    if (patientFound) {
                        System.out.println("Patient exists in the system");

                        // Check if patient already has a room
                        if (patientRoomNumbers[patientIndex] != -1) {
                            System.out.println("Patient is already checked into room " + patientRoomNumbers[patientIndex]);
                            continue;
                        }

                        System.out.println("\n--- Current Room Status ---");
                        for (int j = 0; j < available_room.length; j++) {
                            int roomNumber = j + 101;
                            String status = available_room[j] ? "Available" : "Occupied";
                            System.out.println("Room " + roomNumber + ": " + status);
                        }

                        while (true) {
                            System.out.println("\nEnter room number to check-in (101-110) or 0 to exit: ");
                            room = s.nextInt();
                            s.nextLine();

                            if (room == 0) {
                                System.out.println("Room assignment cancelled.");
                                break;
                            }

                            if (room < 101 || room > 110) {
                                System.out.println("Invalid room number! Please enter a room number between 101-110.");
                                continue;
                            }

                            int index = room - 101; // Convert room number to array index

                            if (available_room[index]) {
                                // Room is available
                                available_room[index] = false; // Mark as occupied
                                patientRoomNumbers[patientIndex] = room; // Assign room to patient
                                System.out.println("Patient successfully checked into room " + room);
                                break;
                            } else {
                                // Room is already occupied
                                System.out.println("Room " + room + " is not available. It's already occupied.");
                            }
                        }

                        // Display current room status after check-in
                        System.out.println("\n--- Current Room Status ---");
                        for (int j = 0; j < available_room.length; j++) {
                            int roomNumber = j + 101;
                            String status = available_room[j] ? "Available" : "Occupied";
                            System.out.println("Room " + roomNumber + ": " + status);
                        }
                    } else {
                        System.out.println("Patient doesn't exist in the system or has been discharged.");
                    }
                }
                case 4 -> {
                    // Bill for Patient
                    System.out.print("Enter Patient ID: ");
                    id = s.nextInt();
                    s.nextLine();

                    // Find patient index
                    patientIndex = -1;
                    for (int i = 0; i < PatientCount; i++) {
                        if (Patient_Id[i] == id && !patientDischarged[i]) {
                            patientIndex = i;
                            break;
                        }
                    }

                    if (patientIndex == -1) {
                        System.out.println("Patient not found or already discharged.");
                        break;
                    }

                    double roomCost = 100.0;
                    double medicine = 50.0;
                    double test = 25.0;

                    patientBills[patientIndex] = roomCost + medicine + test;

                    System.out.println("Bill for Patient ID " + Patient_Id[patientIndex]);
                    System.out.println("Patient Name: " + Patient_Name[patientIndex]);
                    System.out.println("Room: $100");
                    System.out.println("Medicine: $50");
                    System.out.println("Test: $25");
                    System.out.println("Total: $" + patientBills[patientIndex]);
                }
                case 5 -> {
                    // Discharge Patient
                    System.out.print("Enter Patient ID: ");
                    id = s.nextInt();
                    s.nextLine();

                    // Find patient index
                    patientIndex = -1;
                    for (int i = 0; i < PatientCount; i++) {
                        if (Patient_Id[i] == id && !patientDischarged[i]) {
                            patientIndex = i;
                            break;
                        }
                    }

                    if (patientIndex == -1) {
                        System.out.println("Patient not found or already discharged.");
                        break;
                    }

                    String patientName = Patient_Name[patientIndex];

                    // 1. Set the doctor back to available
                    int assignedDoctor = assignedDoctors[patientIndex];
                    if (assignedDoctor != -1 && assignedDoctor < doctorSpecialties.length) {
                        doctorAvailable[assignedDoctor] = true;
                        System.out.println(doctorSpecialties[assignedDoctor] + " specialist is now available.");
                    }

                    // 2. Set the room back to available
                    int patientRoom = patientRoomNumbers[patientIndex];
                    if (patientRoom != -1) {
                        int roomIndex = patientRoom - 101;
                        available_room[roomIndex] = true;
                        System.out.println("Room " + patientRoom + " is now available.");
                    }

                    // 3. Mark the patient as discharged
                    patientDischarged[patientIndex] = true;
                    System.out.println("Patient " + patientName + " has been discharged.");
                }
                case 6 -> {
                    // Patient's Information
                    if (PatientCount == 0) {
                        System.out.println("No patients registered in the system!");
                    } else {
                        System.out.println("====Patient's Information====");
                        System.out.println("Total Patient Count: " + PatientCount);

                        for (int i = 0; i < PatientCount; i++) {
                            System.out.println("\nPatient " + (i + 1));
                            System.out.println("Patient's Id: " + Patient_Id[i]);
                            System.out.println("Patient's Name: " + Patient_Name[i]);
                            System.out.println("Patient's Age: " + Patient_Age[i]);
                            System.out.println("Patient's Disease: " + Patient_Disease[i]);
                            System.out.println("Status: " + (patientDischarged[i] ? "Discharged" : "Active"));

                            if (!patientDischarged[i]) {
                                // Show additional info for active patients
                                if (assignedDoctors[i] != -1) {
                                    System.out.println("Doctor: " + doctorSpecialties[assignedDoctors[i]] + " specialist");
                                } else {
                                    System.out.println("Doctor: Not assigned");
                                }

                                if (patientRoomNumbers[i] != -1) {
                                    System.out.println("Room: " + patientRoomNumbers[i]);
                                } else {
                                    System.out.println("Room: Not assigned");
                                }

                                if (patientBills[i] > 0) {
                                    System.out.println("Bill: $" + patientBills[i]);
                                } else {
                                    System.out.println("Bill: Not generated");
                                }
                            }
                        }
                    }
                }
                case 7 -> {
                    System.out.println("Exiting Hospital System. Goodbye!");
                }
                default -> {
                    System.out.println("Invalid option. Please try again.");
                }
            }
        } while(option != 7);
        s.close();
    }
}