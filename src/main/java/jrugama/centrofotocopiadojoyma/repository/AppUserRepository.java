package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.AppUser;
import java.util.List;
// Optional se utiliza para encapsular de forma segura el resultado de una consulta que podría 
// no encontrar ningún registro (es decir, evitar que se retorne null y cause NullPointerException).
// En esta interfaz se implementa en los métodos de búsqueda (findById, findByEmail, findByName) 
// porque es posible que el usuario que se está buscando no exista en la base de datos.
import java.util.Optional;

public interface AppUserRepository {
    AppUser save(AppUser user);

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByName(String name);

    List<AppUser> findAll();
}
