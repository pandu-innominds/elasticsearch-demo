package com.innominds.service;

import com.innominds.model.Patient;

import java.util.List;

public interface PatientService {

    void createIndex(String indexName);

    String createIndexWithMappings(String indexName);

    Long addPatient(Patient patient);

    List<Patient> getPatients(Integer limit, String sortField, String sortOrder);

    Patient getPatientByName(String name, boolean isSensitive);

    void updatePatientById(Integer patientId, String nameToBeUpdated);

    void updatePatients(String searchByAddress, String addressToBeUpdated);

    void deletePatient(Integer patientId);

    void createPatients(String indexName, Integer numOfPatients);
}
