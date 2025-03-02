package tshirt.ecommerce.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tshirt.ecommerce.library.model.Material;
import tshirt.ecommerce.library.repository.MaterialRepository;

import java.util.List;

@Service
@Transactional
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> findAll() {
        return (List<Material>) materialRepository.findAll();
    }

    public Material findByName(String name) {
        return materialRepository.findByName(name);
    }

    public void save(Material product) {
        materialRepository.save(product);
    }

    public Material get(long id) {
        return materialRepository.findById(id).get();
    }

    public void delete(long id) {
        materialRepository.deleteById(id);
    }

}
