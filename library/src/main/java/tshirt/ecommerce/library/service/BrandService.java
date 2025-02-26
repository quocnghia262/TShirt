package tshirt.ecommerce.library.service;


import tshirt.ecommerce.library.model.Brand;
import tshirt.ecommerce.library.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> findAll() {
        return (List<Brand>) brandRepository.findAll();
    }

    public Brand findByName(String name) {
        return brandRepository.findByName(name);
    }

    public List<Brand> getBrands() {
        return brandRepository.findAll();
    }



    public void save(Brand product) {
        brandRepository.save(product);
    }

    public Brand get(long  id) {
        return brandRepository.findById(id).get();
    }

    public void delete(long id) {
        brandRepository.deleteById(id);
    }
}