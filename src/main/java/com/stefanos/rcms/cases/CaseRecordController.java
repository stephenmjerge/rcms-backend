package com.stefanos.rcms.cases;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.stefanos.rcms.cases.dto.CaseCreateRequest;
import com.stefanos.rcms.cases.dto.CaseResponse;
import com.stefanos.rcms.cases.dto.CaseUpdateRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cases")
public class CaseRecordController {

    private final CaseRecordService service;

    public CaseRecordController(CaseRecordService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CaseResponse create(@Valid @RequestBody CaseCreateRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public CaseResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Page<CaseResponse> listAll(Pageable pageable) {
        return service.listAll(pageable);
    }

    @PutMapping("/{id}")
    public CaseResponse update(@PathVariable Long id, @Valid @RequestBody CaseUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
