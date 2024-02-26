package spring.backend.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import spring.backend.dto.info_and_coffeeshop.CoffeeshopDTO;
import spring.backend.dto.info_and_coffeeshop.DopInfoCreateDTO;
import spring.backend.dto.info_and_coffeeshop.DopInfoDTO;
import spring.backend.dto.recall.RecallDTO;
import spring.backend.dto.recall.RecallReactionDTO;
import spring.backend.entity.Coffeeshop;
import spring.backend.exception.AppRuntimeException;
import spring.backend.service.CoffeeshopService;
import spring.backend.service.DopInfoService;
import spring.backend.service.ReactionService;
import spring.backend.service.RecallService;

import java.util.List;

@Controller
@RequestMapping("/api/coffeeshop")
@AllArgsConstructor
public class CoffeeshopController {

    CoffeeshopService coffeeshopService;
    ModelMapper modelMapper;

    /**
     * ============================================================================================================= *
     Coffeeshop entity controller
     */

    @PostMapping("/save")
    public ResponseEntity<String> savedCoffeeshop(@RequestBody CoffeeshopDTO coffeeshopDTO) {
        try {
            if(coffeeshopDTO != null) {
                coffeeshopService.saveCoffeeshop(coffeeshopDTO);
                return ResponseEntity.ok("Coffeeshop successfully save");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Body is empty");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving coffeeshop");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CoffeeshopDTO>> getAllCoffeeshop() {
        try {
            return ResponseEntity.ok(coffeeshopService.getAllList());
        }catch (Exception e){
            throw new AppRuntimeException(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoffeeshopDTO> getCoffeeshopById(@PathVariable Long id) {
        try {
            Coffeeshop coffeeshop = coffeeshopService.getCoffeeShopById(id);
            if (coffeeshop != null) {
                CoffeeshopDTO coffeeshopDTO = modelMapper.map(coffeeshop, CoffeeshopDTO.class);
                return ResponseEntity.ok(coffeeshopDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new AppRuntimeException(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCoffeeshop(@PathVariable Long id) {
        try {
            if (id != null) {
                coffeeshopService.deleteCoffeeShopById(id);
                return ResponseEntity.ok("Coffeeshop successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coffeeshop not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting coffeeshop");
        }
    }

}
