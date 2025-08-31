package com.household_repair.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.household_repair.entity.Contractor;
import com.household_repair.service.ContractorService;



@RestController
@RequestMapping("/api/contractors")
public class ContractorController {

    @Autowired
    private ContractorService contractorService;

    // ✅ Get all contractors
    @GetMapping
    public List<Contractor> getAllContractors() {
        return  contractorService.getAllContractor();
    }

    // ✅ Get one contractor by ID
    @GetMapping("/{id}")
    public Contractor getContractorById(@PathVariable Long id) {
        return  contractorService.getOneContractor(id);
    }

    // ✅ Add a new contractor
    @PostMapping("/add")
    public Contractor addContractor(@RequestBody Contractor newContractor) {
        return  contractorService.addContractor(newContractor);
    }

    // ✅ Get contractors by name (partial or full)
    @GetMapping("/search")
    public List<Contractor> getContractorsByName(@RequestParam String name) {
        return contractorService.getContractorByName(name);
    }

    // ✅ Update a contractor
    @PutMapping("/update")
    public String updateContractor(@RequestBody Contractor contractor) {
    	 contractorService.updateContractor(contractor);
        return "Contractor updated successfully.";
    }

    // ✅ Delete a contractor
    @DeleteMapping("/delete/{id}")
    public String deleteContractor(@PathVariable Long id) {
    	 contractorService.deleteContractor(id);
        return "Contractor with ID " + id + " deleted successfully.";
    }
}
