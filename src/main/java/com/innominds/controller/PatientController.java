package com.innominds.controller;

import com.innominds.model.Patient;
import com.innominds.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PatientController {

    @Autowired
    ElasticSearchService elasticSearchService;

    @GetMapping(value = "/")
    public ResponseEntity<Object> getHomePage() {
        return new ResponseEntity("Home Page", HttpStatus.OK);
    }

    @PostMapping(value = "/patients")
    public ResponseEntity<Object> getListOfPatients(@RequestBody Map<String, String> body) {
        Integer limit = Integer.valueOf(body.get("size"));
        String sortField = body.get("sortBy");
        String sortOrder = body.get("order");
        List<Patient> patients = elasticSearchService.getPatients(limit, sortField, sortOrder);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping(value = "/patients/{name}/{sensitive}")
    public ResponseEntity<Object> getPatientByName(@PathVariable(value = "name") String name,
                                                   @PathVariable(value = "sensitive") boolean isSensitive) {
        Patient patient = elasticSearchService.getPatientByName(name, isSensitive);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @PostMapping(value = "/index/{indexName}")
    public ResponseEntity<String> createIndex(@PathVariable(value = "indexName") String indexName) {
        elasticSearchService.createIndex(indexName);
        return new ResponseEntity<>("Elastic index :" + indexName + " created successfully.", HttpStatus.OK);
    }

    @PostMapping(value = "/index-with-mappings/{indexName}")
    public ResponseEntity<String> createIndexWithMappings(@PathVariable(value = "indexName") String indexName) {
        String indexWithMappings = elasticSearchService.createIndexWithMappings(indexName);
        return new ResponseEntity<>("Elastic index :" + indexWithMappings + " created successfully.", HttpStatus.OK);
    }

    @PostMapping(value = "/patient")
    public ResponseEntity<String> createPatient(@RequestBody Patient patient) {
        Integer patientId = elasticSearchService.addPatient(patient);
        return new ResponseEntity<>("Patient created in the system with id : " + patientId, HttpStatus.OK);
    }

    @PutMapping(value = "/patient/{id}")
    public ResponseEntity<String> updatePatient(@PathVariable(value = "id") Integer patientId,
                                                @RequestBody Map<String, String> body) {
        String nameToBeUpdated = body.get("name");
        elasticSearchService.updatePatientById(patientId, nameToBeUpdated);
        return new ResponseEntity<>("Patient updated in the system with id : " + patientId, HttpStatus.OK);
    }

    @PutMapping(value = "/update-patients")
    public ResponseEntity<String> updatePatients(
            @RequestBody Map<String, String> body) {
        String searchByAddress = body.get("address");
        String addressToBeUpdated = body.get("address-to-be");
        elasticSearchService.updatePatients(searchByAddress, addressToBeUpdated);
        return new ResponseEntity<>("Patients updated in the system.", HttpStatus.OK);
    }

    @DeleteMapping(value = "/patient/{id}")
    public ResponseEntity<String> deletePatient(
            @PathVariable(value = "id") Integer patientId) {
        elasticSearchService.deletePatient(patientId);
        return new ResponseEntity<>("Patient with id " + patientId + " deleted successfully.", HttpStatus.OK);
    }
}
