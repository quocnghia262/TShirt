package tshirt.ecommerce.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tshirt.ecommerce.library.model.Color;
import tshirt.ecommerce.library.repository.ColorRepository;

import java.util.List;

@Service
@Transactional
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    public List<Color> findAll() {
        return (List<Color>) colorRepository.findAll();
    }

    public Color findByName(String name) {
        return colorRepository.findByName(name);
    }

    public void save(Color product) {
        colorRepository.save(product);
    }

    public Color get(long id) {
        return colorRepository.findById(id).get();
    }

    public void delete(long id) {
        colorRepository.deleteById(id);
    }

}
