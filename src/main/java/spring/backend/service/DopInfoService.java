package spring.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import spring.backend.dto.DopInfoCreateDTO;
import spring.backend.dto.DopInfoDTO;
import spring.backend.dto.RecallDTO;
import spring.backend.entity.DopInfoCoffeeshop;
import spring.backend.exception.AppRuntimeException;
import spring.backend.repository.jpa.DopInfoRepository;
import spring.backend.repository.paging.DopInfoRepositoryPaging;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DopInfoService {
    @Autowired
    RecallService recallService;
    @Autowired
    DopInfoRepository dopInfoRepository;
    @Autowired
    CoffeeshopService coffeeshopService;
    @Autowired
    UserService userService;
    @Autowired
    DopInfoRepositoryPaging dopInfoRepositoryPaging;

    ModelMapper modelMapper = new ModelMapper();

    public DopInfoCoffeeshop saveDopInfo(DopInfoCreateDTO dopInfoDTO) {
        if (dopInfoDTO.getCoffeeshop() == null) {
            throw new AppRuntimeException("Parameter coffeeshop_id is not found in request..!!", HttpStatus.NO_CONTENT);
        }
        DopInfoCoffeeshop dopInfo = modelMapper.map(dopInfoDTO, DopInfoCoffeeshop.class);
        if (dopInfoDTO.getId() != null) {
            DopInfoCoffeeshop oldDopInfo = dopInfoRepository.findById(dopInfoDTO.getId()).orElseThrow(() ->
                    new AppRuntimeException("DopInfo not found", HttpStatus.NOT_FOUND));
            if (oldDopInfo != null) {
                BeanUtils.copyProperties(dopInfo,oldDopInfo);
                dopInfoRepository.save(oldDopInfo);
            } else {
                throw new AppRuntimeException("Can't find record with identifier: " +
                        dopInfoDTO.getId(), HttpStatus.NOT_FOUND);
            }
        }
        return dopInfoRepository.save(dopInfo);
    }

//    public double getAverageRatingForCoffeeshop(Long dopInfo_id) {
//        DopInfoCoffeeshop dopInfo = dopInfoRepository.findById(dopInfo_id)
//                .orElseThrow(() -> new EntityNotFoundException("DopInfo not found with id: " + dopInfo_id));
//        return dopInfo.getAverageRating();
//    }
    public void recallCoffeeshop(Long coffeeshopId, RecallDTO recall) {
        DopInfoCoffeeshop dopInfo = dopInfoRepository.findById(coffeeshopId)
                .orElseThrow(() -> new EntityNotFoundException("DopInfo not found with id: " + coffeeshopId));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new AppRuntimeException("User is not authenticated", HttpStatus.UNAUTHORIZED);
        } else {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                dopInfo.getRecallList().add(recallService.recallCoffeeShop(dopInfo,
                        userService.findByUsername(((UserDetails) principal).getUsername()),recall));
            }
        }
    }

    public void deleteDopInfoById(Long id) {
        dopInfoRepository.deleteById(id);
    }

    public List<DopInfoDTO> getByCoffeshop(Long id) {
        List<DopInfoCoffeeshop> dopInfoList = dopInfoRepository.findByCoffeeshop(coffeeshopService.getCoffeeShopById(id));
        return dopInfoList.stream()
                .map(dopInfo -> DopInfoDTO.builder()
                        .id(dopInfo.getId())
                        .shop_id(dopInfo.getCoffeeshop().getId())
                        .city(dopInfo.getCity())
                        .address(dopInfo.getAddress())
                        .rating(dopInfo.getAverageRating())
                        .build())
                .collect(Collectors.toList());
    }

    public List<DopInfoDTO> getByCity(String city, Pageable pageable) {
        List<DopInfoCoffeeshop> dopInfoList = dopInfoRepositoryPaging.findAllByCity(city,pageable);
        return dopInfoList.stream()
                .map(dopInfo -> DopInfoDTO.builder()
                        .id(dopInfo.getId())
                        .shop_id(dopInfo.getCoffeeshop().getId())
                        .city(dopInfo.getCity())
                        .address(dopInfo.getAddress())
                        .rating(dopInfo.getAverageRating())
                        .build())
                .collect(Collectors.toList());
    }
}
