package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.PrintFile;

import java.util.Optional;

public interface PrintFileRepository {
    PrintFile save(PrintFile printFile);

    Optional<PrintFile> findById(Long id);
}
