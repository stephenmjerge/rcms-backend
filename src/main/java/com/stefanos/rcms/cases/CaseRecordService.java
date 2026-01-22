package com.stefanos.rcms.cases;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.stefanos.rcms.cases.dto.CaseCreateRequest;
import com.stefanos.rcms.cases.dto.CaseResponse;
import com.stefanos.rcms.cases.dto.CaseUpdateRequest;

@Service
@Transactional
public class CaseRecordService {

    private final CaseRecordRepository repository;

    public CaseRecordService(CaseRecordRepository repository) {
        this.repository = repository;
    }

    public CaseResponse create(CaseCreateRequest request) {
        if (repository.existsByExternalReference(request.getExternalReference())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "externalReference already exists");
        }

        CaseRecord record = new CaseRecord();
        record.setExternalReference(request.getExternalReference());
        record.setTitle(request.getTitle());
        record.setDescription(request.getDescription());
        record.setStatus(request.getStatus());

        CaseRecord saved = repository.save(record);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CaseResponse getById(Long id) {
        CaseRecord record = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Case not found"));
        return toResponse(record);
    }

    @Transactional(readOnly = true)
    public List<CaseResponse> listAll() {
        return repository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    public CaseResponse update(Long id, CaseUpdateRequest request) {
        CaseRecord record = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Case not found"));

        record.setTitle(request.getTitle());
        record.setDescription(request.getDescription());
        record.setStatus(request.getStatus());

        CaseRecord saved = repository.save(record);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Case not found");
        }
        repository.deleteById(id);
    }

    private CaseResponse toResponse(CaseRecord record) {
        CaseResponse response = new CaseResponse();
        response.setId(record.getId());
        response.setExternalReference(record.getExternalReference());
        response.setTitle(record.getTitle());
        response.setDescription(record.getDescription());
        response.setStatus(record.getStatus());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        return response;
    }
}
