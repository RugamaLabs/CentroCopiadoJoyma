package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.PrintFile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryPrintFileRepository implements PrintFileRepository {

    private final Map<Long, PrintFile> database = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public PrintFile save(PrintFile printFile) {
        if (printFile.getId() == null) {
            printFile.setId(idGenerator.getAndIncrement());
        }
        database.put(printFile.getId(), printFile);
        return printFile;
    }

    @Override
    public Optional<PrintFile> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }
}
