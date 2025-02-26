package tshirt.ecommerce.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tshirt.ecommerce.library.model.Address;
import tshirt.ecommerce.library.repository.AddressRepository;
import tshirt.ecommerce.library.repository.CountryRepository;
import tshirt.ecommerce.library.web.dto.AddressDto;

import java.util.List;

@Service
@Transactional
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CountryRepository countryRepository;

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public Address getAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setId(addressDto.getId());
        address.setCompany(addressDto.getCompany());
        address.setAddress1(addressDto.getAddress1());
        address.setAddress2(addressDto.getAddress2());
        address.setCountry(countryRepository.findByName(addressDto.getCountry()));
        address.setPostalCode(addressDto.getPostalCode());
        address.setState(addressDto.getState());
        address.setCity(addressDto.getCity());

        return address;
    }

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Address findById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    public AddressDto getAddressDto(Address foundAddress) {
        AddressDto addressDto = new AddressDto();
        addressDto.setId(foundAddress.getId());
        addressDto.setCompany(foundAddress.getCompany());
        addressDto.setAddress1(foundAddress.getAddress1());
        addressDto.setAddress2(foundAddress.getAddress2());
        addressDto.setCountry(foundAddress.getCountry().getName());
        addressDto.setPostalCode(foundAddress.getPostalCode());
        addressDto.setState(foundAddress.getState());
        addressDto.setCity(foundAddress.getCity());

        return addressDto;
    }
}
