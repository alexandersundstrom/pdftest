package com.pdf.test.Database;

import com.pdf.test.model.Signature;
import org.springframework.data.repository.CrudRepository;

public interface SignatureRepository extends CrudRepository<Signature, Integer> {
}
