package tshirt.ecommerce.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tshirt.ecommerce.library.model.Size;
import tshirt.ecommerce.library.repository.SizeRepository;

import java.util.List;

@Service
@Transactional
public class SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    public List<Size> findAll() {
        return (List<Size>) sizeRepository.findAll();
    }

    public Size findByName(String name) {
        return sizeRepository.findByName(name);
    }

    public void save(Size product) {
        sizeRepository.save(product);
    }

    public Size get(long id) {
        return sizeRepository.findById(id).get();
    }

    public void delete(long id) {
        sizeRepository.deleteById(id);
    }

}
