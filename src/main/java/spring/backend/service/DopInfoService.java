package spring.backend.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import spring.backend.dto.info_and_coffeeshop.DopInfoCreateDTO;
import spring.backend.dto.info_and_coffeeshop.DopInfoDTO;
import spring.backend.dto.recall.RecallDTO;
import spring.backend.entity.DopInfoCoffeeshop;
import spring.backend.exception.AppRuntimeException;
import spring.backend.repository.jpa.DopInfoRepository;
import spring.backend.repository.paging.DopInfoRepositoryPaging;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DopInfoService {
    RecallService recallService;
    DopInfoRepository dopInfoRepository;
    CoffeeshopService coffeeshopService;
    UserService userService;
    DopInfoRepositoryPaging dopInfoRepositoryPaging;
    ModelMapper modelMapper;

    public DopInfoCoffeeshop findByID(Long id) {
        return dopInfoRepository.findById(id).orElseThrow(
                ()-> new AppRuntimeException("DopInfo not found with id: " + id, HttpStatus.NOT_FOUND));
    }
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

    public void recallCoffeeshop(Long coffeeshopId, RecallDTO recall) {
        DopInfoCoffeeshop dopInfo = dopInfoRepository.initializeRecallList(coffeeshopId);
        dopInfo.getRecallList().add(recallService.recallCoffeeShop(dopInfo,
                userService.getUser(), recall));
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
