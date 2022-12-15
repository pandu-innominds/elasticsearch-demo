package com.innominds;

import com.google.gson.Gson;
import com.innominds.model.Medication;
import com.innominds.model.Patient;
import com.innominds.model.Prescription;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TestDataGenerator {

    public static String[] surnamesList = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Garcia",
            "Rodriguez", "Wilson", "Martinez", "Anderson", "Taylor", "Thomas", "Hernandez", "Moore", "Martin",
            "Jackson", "Thompson", "White", "Lopez", "Lee", "Gonzalez", "Harris", "Clark", "Lewis", "Robinson",
            "Walker", "Perez", "Hall", "Young", "Allen", "Sanchez", "Wright", "King", "Scott", "Green", "Baker",
            "Adams", "Nelson", "Hill", "Ramirez", "Campbell", "Mitchell", "Roberts", "Carter", "Phillips", "Evans",
            "Turner", "Torres", "Parker", "Collins", "Edwards", "Stewart", "Flores", "Morris", "Nguyen", "Murphy",
            "Rivera", "Cook", "Rogers", "Morgan", "Peterson", "Cooper", "Reed", "Bailey", "Bell", "Gomez", "Kelly",
            "Howard", "Ward", "Cox", "Diaz", "Richardson", "Wood", "Watson", "Brooks", "Bennett", "Gray", "James",
            "Reyes", "Cruz", "Hughes", "Price", "Myers", "Long", "Foster", "Sanders", "Ross", "Morales", "Powell",
            "Sullivan", "Russell", "Ortiz", "Jenkins", "Gutierrez", "Perry", "Butler", "Barnes", "Fisher",
            "Henderson", "Coleman", "Simmons", "Patterson", "Jordan", "Reynolds", "Hamilton", "Graham", "Kim",
            "Gonzales", "Alexander", "Ramos", "Wallace", "Griffin", "West", "Cole", "Hayes", "Chavez", "Gibson",
            "Bryant", "Ellis", "Stevens", "Murray", "Ford", "Marshall", "Owens", "Mcdonald", "Harrison", "Ruiz",
            "Kennedy", "Wells", "Alvarez", "Woods", "Mendoza", "Castillo", "Olson", "Webb", "Washington",
            "Tucker", "Freeman", "Burns", "Henry", "Vasquez", "Snyder", "Simpson", "Crawford", "Jimenez", "Porter",
            "Mason", "Shaw", "Gordon", "Wagner", "Hunter", "Romero", "Hicks", "Dixon", "Hunt", "Palmer", "Robertson",
            "Black", "Holmes", "Stone", "Meyer", "Boyd", "Mills", "Warren", "Fox", "Rose", "Rice", "Moreno", "Schmidt",
            "Patel", "Ferguson", "Nichols", "Herrera", "Medina", "Ryan", "Fernandez", "Weaver", "Daniels", "Stephens",
            "Gardner", "Payne", "Kelley", "Dunn", "Pierce", "Arnold", "Tran", "Spencer", "Peters", "Hawkins", "Grant",
            "Hansen", "Castro", "Hoffman", "Hart", "Elliott", "Cunningham", "Knight", "Bradley", "Carroll", "Hudson",
            "Duncan", "Armstrong", "Berry", "Andrews", "Johnston", "Ray", "Lane", "Riley", "Carpenter", "Perkins",
            "Aguilar", "Silva", "Richards", "Willis", "Matthews", "Chapman", "Lawrence", "Garza", "Vargas", "Watkins",
            "Wheeler", "Larson", "Carlson", "Harper", "George", "Greene", "Burke", "Guzman", "Morrison", "Munoz"};

    public static String[] maleNamesList = {"James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Joseph",
            "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", "Edward",
            "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey",
            "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis",
            "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger", "Joe",
            "Juan", "Jack", "Albert", "Jonathan", "Justin", "Terry", "Gerald", "Keith", "Samuel", "Willie", "Ralph",
            "Lawrence", "Nicholas", "Roy", "Benjamin", "Bruce", "Brandon", "Adam", "Harry", "Fred", "Wayne", "Billy",
            "Steve", "Louis", "Jeremy", "Aaron", "Randy", "Howard", "Eugene", "Carlos", "Russell", "Bobby", "Victor",
            "Martin", "Ernest", "Phillip", "Todd", "Jesse", "Craig", "Alan", "Shawn", "Clarence", "Sean", "Philip",
            "Chris", "Johnny", "Earl", "Jimmy", "Antonio", "Danny", "Bryan", "Tony", "Luis", "Mike", "Stanley",
            "Leonard", "Nathan", "Dale", "Manuel", "Rodney", "Curtis", "Norman", "Allen", "Marvin", "Vincent",
            "Glenn", "Jeffery", "Travis", "Jeff", "Chad", "Jacob", "Lee", "Melvin", "Alfred", "Kyle", "Francis"};

    public static String[] femaleNamesList = {"Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer", "Maria", "Susan",
            "Margaret", "Dorothy", "Lisa", "Nancy", "Karen", "Betty", "Helen", "Sandra", "Donna", "Carol", "Ruth",
            "Sharon", "Michelle", "Laura", "Sarah", "Kimberly", "Deborah", "Jessica", "Shirley", "Cynthia", "Angela",
            "Melissa", "Brenda", "Amy", "Anna", "Rebecca", "Virginia", "Kathleen", "Pamela", "Martha", "Debra",
            "Amanda", "Stephanie", "Carolyn", "Christine", "Marie", "Janet", "Catherine", "Frances", "Ann", "Joyce",
            "Diane", "Alice", "Julie", "Heather", "Teresa", "Doris", "Gloria", "Evelyn", "Jean", "Cheryl", "Mildred",
            "Katherine", "Joan", "Ashley", "Judith", "Rose", "Janice", "Kelly", "Nicole", "Judy", "Christina", "Kathy",
            "Theresa", "Beverly", "Denise", "Tammy", "Irene", "Jane", "Lori", "Rachel", "Marilyn", "Andrea", "Kathryn",
            "Louise", "Sara", "Anne", "Jacqueline", "Wanda", "Bonnie", "Julia", "Ruby", "Lois", "Tina", "Phyllis", "Norma",
            "Paula", "Diana", "Annie", "Lillian", "Emily", "Robin", "Peggy", "Crystal", "Gladys", "Rita", "Dawn", "Connie",
            "Florence", "Tracy", "Edna", "Tiffany", "Carmen", "Rosa", "Cindy", "Grace", "Wendy", "Victoria", "Edith", "Kim",
            "Sherry", "Sylvia", "Josephine", "Thelma", "Shannon", "Sheila", "Ethel", "Ellen", "Elaine", "Marjorie",
            "Carrie", "Charlotte", "Monica", "Esther", "Pauline", "Emma", "Juanita", "Anita", "Rhonda", "Hazel",
            "Amber", "Eva", "Debbie", "April", "Leslie", "Clara", "Lucille", "Jamie", "Joanne", "Eleanor", "Valerie",
            "Danielle", "Megan", "Alicia", "Suzanne", "Michele", "Gail", "Bertha", "Darlene", "Veronica", "Jill",
            "Erin", "Geraldine", "Lauren", "Cathy", "Joann", "Lorraine", "Lynn", "Sally", "Regina", "Erica", "Beatrice",
            "Dolores", "Bernice", "Audrey", "Yvonne", "Annette", "June", "Samantha", "Marion", "Dana", "Stacy", "Ana",
            "Renee", "Ida", "Vivian", "Roberta", "Holly", "Brittany", "Melanie", "Loretta", "Yolanda", "Jeanette",
            "Laurie", "Katie", "Kristen", "Vanessa", "Alma", "Sue", "Elsie", "Beth", "Jeanne", "Vicki", "Carla"};

    public static String[] cities = {"Achampet", "Adilabad", "Adoni", "Alampur", "Allagadda", "Alur", "Amalapuram", "Amangallu", "Anakapalle", "Anantapur", "Andole", "Araku", "Armoor", "Asifabad", "Aswaraopet", "Atmakur", "B. Kothakota", "Badvel", "Banaganapalle", "Bandar", "Bangarupalem", "Banswada", "Bapatla", "Bellampalli", "Bhadrachalam", "Bhainsa", "Bheemunipatnam", "Bhimadole", "Bhimavaram", "Bhongir", "Bhooragamphad", "Boath", "Bobbili", "Bodhan", "Chandoor", "Chavitidibbalu", "Chejerla", "Chepurupalli", "Cherial", "Chevella", "Chinnor", "Chintalapudi", "Chintapalle", "Chirala", "Chittoor", "Chodavaram", "Cuddapah", "Cumbum", "Darsi", "Devarakonda", "Dharmavaram", "Dichpalli", "Divi", "Donakonda", "Dronachalam", "East Godavari", "Eluru", "Eturnagaram", "Gadwal", "Gajapathinagaram", "Gajwel", "Garladinne", "Giddalur", "Godavari", "Gooty", "Gudivada", "Gudur", "Guntur", "Hindupur", "Hunsabad", "Huzurabad", "Huzurnagar", "Hyderabad", "Ibrahimpatnam", "Jaggayyapet", "Jagtial", "Jammalamadugu", "Jangaon", "Jangareddygudem", "Jannaram", "Kadiri", "Kaikaluru", "Kakinada", "Kalwakurthy", "Kalyandurg", "Kamalapuram", "Kamareddy", "Kambadur", "Kanaganapalle", "Kandukuru", "Kanigiri", "Karimnagar", "Kavali", "Khammam", "Khanapur (AP)", "Kodangal", "Koduru", "Koilkuntla", "Kollapur", "Kothagudem", "Kovvur", "Krishna", "Krosuru", "Kuppam", "Kurnool", "Lakkireddipalli", "Madakasira", "Madanapalli", "Madhira", "Madnur", "Mahabubabad", "Mahabubnagar", "Mahadevapur", "Makthal", "Mancherial", "Mandapeta", "Mangalagiri", "Manthani", "Markapur", "Marturu", "Medachal", "Medak", "Medarmetla", "Metpalli", "Mriyalguda", "Mulug", "Mylavaram", "Nagarkurnool", "Nalgonda", "Nallacheruvu", "Nampalle", "Nandigama", "Nandikotkur", "Nandyal", "Narasampet", "Narasaraopet", "Narayanakhed", "Narayanpet", "Narsapur", "Narsipatnam", "Nazvidu", "Nelloe", "Nellore", "Nidamanur", "Nirmal", "Nizamabad", "Nuguru", "Ongole", "Outsarangapalle", "Paderu", "Pakala", "Palakonda", "Paland", "Palmaneru", "Pamuru", "Pargi", "Parkal", "Parvathipuram", "Pathapatnam", "Pattikonda", "Peapalle", "Peddapalli", "Peddapuram", "Penukonda", "Piduguralla", "Piler", "Pithapuram", "Podili", "Polavaram", "Prakasam", "Proddatur", "Pulivendla", "Punganur", "Putturu", "Rajahmundri", "Rajampeta", "Ramachandrapuram", "Ramannapet", "Rampachodavaram", "Rangareddy", "Rapur", "Rayachoti", "Rayadurg", "Razole", "Repalle", "Saluru", "Sangareddy", "Sathupalli", "Sattenapalle", "Satyavedu", "Shadnagar", "Siddavattam", "Siddipet", "Sileru", "Sircilla", "Sirpur Kagaznagar", "Sodam", "Sompeta", "Srikakulam", "Srikalahasthi", "Srisailam", "Srungavarapukota", "Sudhimalla", "Sullarpet", "Tadepalligudem", "Tadipatri", "Tanduru", "Tanuku", "Tekkali", "Tenali", "Thungaturthy", "Tirivuru", "Tirupathi", "Tuni", "Udaygiri", "Ulvapadu", "Uravakonda", "Utnor", "V.R. Puram", "Vaimpalli", "Vayalpad", "Venkatgiri", "Venkatgirikota", "Vijayawada", "Vikrabad", "Vinjamuru", "Vinukonda", "Visakhapatnam", "Vizayanagaram", "Vizianagaram", "Vuyyuru", "Wanaparthy", "Warangal", "Wardhannapet", "Yelamanchili", "Yelavaram", "Yeleswaram", "Yellandu", "Yellanuru", "Yellareddy", "Yerragondapalem", "Zahirabad"};

    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final String[] medicationSchedule = {"001", "010", "011", "100", "101", "110", "111"};


    public List<Patient> generatePatients(int count) throws InterruptedException {
        List<Patient> patients = new ArrayList<>();
        while (count > 0) {
            patients.add(generatePatient());
            count--;
        }
        return patients;
    }

    private Patient generatePatient() throws InterruptedException {
        Patient patient = new Patient();
        patient.setId(Long.valueOf(format.format(new Date())));
        patient.setName(femaleNamesList[RandomUtils.nextInt(0, femaleNamesList.length)]
                + " " + surnamesList[RandomUtils.nextInt(0, surnamesList.length)]);
        patient.setAge(RandomUtils.nextInt(0, 95));
        patient.setAddress(cities[RandomUtils.nextInt(0, cities.length)]);
        patient.setPrescriptions(generatePrescriptions(2));
        Thread.sleep(10);
        return patient;
    }

    private List<Prescription> generatePrescriptions(int count) {
        List<Prescription> prescriptions = new ArrayList<>();
        while (count > 0) {
            count--;
            prescriptions.add(generateDummyPrescription());
        }
        return prescriptions;
    }

    private Prescription generateDummyPrescription() {
        Prescription prescription = new Prescription();
        prescription.setId(RandomUtils.nextInt(1, 50));
        prescription.setDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        prescription.setDiagnosis(RandomStringUtils.random(50, 65, 127, true, true));
        prescription.setTemperature(getRandomDouble(90.0, 104.0));
        prescription.setBloodPressure(getRandomDouble(90.0, 120.0));
        prescription.setWeight(getRandomDouble(3.0, 130.0));
        prescription.setMedications(generateMedications(3));
        return prescription;
    }

    private List<Medication> generateMedications(int count) {
        List<Medication> medications = new ArrayList<>();
        while (count > 0) {
            count--;
            medications.add(generateMedication());
        }
        return medications;
    }

    private Medication generateMedication() {
        Medication medication = new Medication();
        medication.setCode(RandomStringUtils.random(10, 65, 90, true, false));
        medication.setName(RandomStringUtils.random(15, 95, 127, true, false));
        medication.setNumberOfDays(RandomUtils.nextInt(5, 30));
        medication.setNumberOfUnits(RandomUtils.nextInt(10, 20));
        medication.setDaySchedule(medicationSchedule[RandomUtils.nextInt(0, 7)]);
        return medication;
    }

    private Double getRandomDouble(double start, double end) {
        return BigDecimal.valueOf(RandomUtils.nextDouble(start, end))
                .setScale(2, RoundingMode.FLOOR).doubleValue();
    }

    public static void main(String[] args) throws InterruptedException {
        TestDataGenerator gen = new TestDataGenerator();
        System.out.println(new Gson().toJson(gen.generatePatients(1)));
    }
}
