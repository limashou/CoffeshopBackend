package spring.backend.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import spring.backend.dto.*;
import spring.backend.entity.Coffeeshop;
import spring.backend.entity.RecallReaction;
import spring.backend.exception.AppRuntimeException;
import spring.backend.repository.jpa.ReactionRepository;
import spring.backend.service.CoffeeshopService;
import spring.backend.service.DopInfoService;
import spring.backend.service.ReactionService;
import spring.backend.service.RecallService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/coffeeshop")
public class CoffeeshopController {
    @Autowired
    CoffeeshopService coffeeshopService;
    @Autowired
    DopInfoService dopInfoService;
    @Autowired
    RecallService recallService;
    @Autowired
    ReactionService reactionService;
    ModelMapper modelMapper = new ModelMapper();

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

    /**
     * ============================================================================================================= *
     DopInfo entity controller
     */

    @PostMapping("/{id}/saveInfo")
    public ResponseEntity<String> savedDopInfo(@PathVariable Long id,@RequestBody DopInfoCreateDTO dopInfoCreateDTO) {
        if (dopInfoCreateDTO != null) {
            Coffeeshop coffeeshop = coffeeshopService.getCoffeeShopById(id);
            dopInfoCreateDTO.setCoffeeshop(coffeeshop);
            dopInfoService.saveDopInfo(dopInfoCreateDTO);
            return ResponseEntity.ok("DopInfo successfully save");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Body is empty");
        }
    }

    @DeleteMapping("/{id}/delete/{dopInfo_id}")
    public ResponseEntity<String> deleteDopInfo(@PathVariable Long id,@PathVariable Long dopInfo_id) {
        if (id != null) {
            if (dopInfo_id != null) {
                dopInfoService.deleteDopInfoById(dopInfo_id);
                return ResponseEntity.ok("DopInfo successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DopInfo not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coffeeshop not found");
        }
    }

    @PostMapping("/{coffeshop_id}/setRecall/{dopinfo_id}")
    public ResponseEntity<String> recallCoffeeshop(@PathVariable Long dopinfo_id, @RequestBody RecallDTO ratingDTO) {
        dopInfoService.recallCoffeeshop(dopinfo_id, ratingDTO);
        return ResponseEntity.ok("Rating set successfully");
    }

    @PostMapping("/setReaction/{recall_id}")
    public ResponseEntity<String> reactionSet(@PathVariable Long recall_id,@RequestBody RecallReactionDTO reaction){
        recallService.recallReaction(recall_id,reaction);
        return ResponseEntity.ok("Reaction set successfully");
    }

    @Transactional
    @DeleteMapping("/deleteReaction/{id}")
    public ResponseEntity<String> deleteReaction(@PathVariable Long id) {
        if (id != null) {
            reactionService.deleteReaction(id);
            return ResponseEntity.ok("RecallReaction successfully deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/c{id}/show")
    public ResponseEntity<List<DopInfoDTO>> showDopInfo(@PathVariable Long id) {
        return ResponseEntity.ok(dopInfoService.getByCoffeshop(id));
    }

    @GetMapping("/byCity/{city}")
    public ResponseEntity<List<DopInfoDTO>> showCoffeshopByCity(@PathVariable String city,
                                                                @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(dopInfoService.getByCity(city,pageable));
    }
}
