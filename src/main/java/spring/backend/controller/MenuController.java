package spring.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.backend.dto.MenuDTO;
import spring.backend.dto.OfferRequest;
import spring.backend.dto.OfferResponse;
import spring.backend.entity.SpecialOffer;
import spring.backend.service.MenuService;
import spring.backend.service.OfferService;

import java.util.List;

@Controller
@RequestMapping("/api/coffeeshop/menu")
public class MenuController {
    @Autowired
    MenuService menuService;
    @Autowired
    OfferService offerService;

    @PostMapping("/save")
    public ResponseEntity<String> savedMenu(@RequestBody MenuDTO menuDTO) {
        if (menuDTO != null) {
            menuService.saveMenu(menuDTO);
            return ResponseEntity.ok("Item successfully save");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Body is empty");
        }
    }

    @GetMapping("/{id}/newest")
    public ResponseEntity<List<MenuDTO>> getNewest(@PathVariable Long id){
        return ResponseEntity.ok(menuService.newestItems(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteItem(@RequestBody MenuDTO menuDTO) {
        if (menuDTO != null) {
            menuService.deleteItem(menuDTO.getId());
            return ResponseEntity.ok("Item successfully delete");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Body is empty");
        }
    }

    @PostMapping("/createOffer")
    public ResponseEntity<String> savedOffer(@RequestBody OfferResponse offerDTO) {
        if (offerDTO != null) {
            offerService.save(offerDTO);
            return ResponseEntity.ok("Item successfully save");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Body is empty");
        }
    }
    @DeleteMapping("/deleteOffer/{id}")
    public ResponseEntity<String> deleteOffer(@PathVariable Long id) {

        SpecialOffer recallReactionOptional = offerService.findByID(id);
        if (recallReactionOptional != null) {
            offerService.delete(recallReactionOptional.getId());
            return ResponseEntity.ok("Offer successfully deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("getAll/{id}")
    public ResponseEntity<List<OfferRequest>> getAll(@PathVariable Long id){
        return ResponseEntity.ok(offerService.getAllbyCoffeeshop(id));
    }

}
