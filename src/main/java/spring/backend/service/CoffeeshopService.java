package spring.backend.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import spring.backend.dto.CoffeeshopDTO;
import spring.backend.entity.Coffeeshop;
import spring.backend.exception.AppRuntimeException;
import spring.backend.repository.jpa.CoffeeshopRepository;

import java.lang.reflect.Type;
import java.util.List;

@Service
//@AllArgsConstructor
public class CoffeeshopService {
    @Autowired
    CoffeeshopRepository coffeeshopRepository;
    ModelMapper modelMapper = new ModelMapper();
    public Coffeeshop saveCoffeeshop(CoffeeshopDTO coffeeshopDTO) {
        if(coffeeshopDTO.getName() == null) {
            throw new AppRuntimeException("Parameter name is not found in request..!!", HttpStatus.NO_CONTENT);
        }
        Coffeeshop coffeeshop = modelMapper.map(coffeeshopDTO, Coffeeshop.class);
        if (coffeeshopDTO.getId() != null){
            Coffeeshop oldCoffeshop = coffeeshopRepository.findById(coffeeshopDTO.getId()).orElseThrow(() ->
                    new AppRuntimeException("Coffeshop not found", HttpStatus.NOT_FOUND));
            if(oldCoffeshop != null) {
                BeanUtils.copyProperties(coffeeshop,oldCoffeshop);
                coffeeshopRepository.save(oldCoffeshop);
            }else {
                throw new AppRuntimeException("Can't find record with identifier: " +
                        coffeeshopDTO.getId(), HttpStatus.NOT_FOUND);
            }
        }
         return coffeeshopRepository.save(coffeeshop);
    }

    public List<CoffeeshopDTO> getAllList() {
        List<Coffeeshop> coffeeshops = coffeeshopRepository.findAll();
        Type setOfDTOsType = new TypeToken<List<CoffeeshopDTO>>(){}.getType();
        List<CoffeeshopDTO> coffeeshopDTOS = modelMapper.map(coffeeshops, setOfDTOsType);
        return coffeeshopDTOS;
    }

    public void deleteCoffeeShopById(Long id) {
        coffeeshopRepository.deleteById(id);
    }

    public Coffeeshop getCoffeeShopById(Long id) {
        return coffeeshopRepository.findById(id).orElse(null);
    }

}
