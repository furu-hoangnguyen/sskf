package sskf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sskf.mapper.HoldingsForRequestExhibitionPromotionMapper;
import sskf.model.entity.HoldingsForRequestExhibitionPromotionEntity;
import sskf.model.entity.RequestExhibitionPromotionsEntity;
import sskf.model.request.ExhibitionPromotionsRequest;
import sskf.model.request.HoldingsForRequestExhibitionPromotionRequest;
import sskf.repository.HoldingsForRequestExhibitionPromotionRepository;
import sskf.service.impl.HoldingsForRequestExhibitionPromotionServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class HoldingsForRequestExhibitionPromotionServiceTest {

    @InjectMocks
    private HoldingsForRequestExhibitionPromotionServiceImpl holdingsForRequestExhibitionPromotionService;

    @Mock
    private HoldingsForRequestExhibitionPromotionRepository repository;

    @Mock
    private HoldingsForRequestExhibitionPromotionMapper mapper;

    @Test
    public void setHoldingsForRequestExhibitionTest() {
        List<HoldingsForRequestExhibitionPromotionEntity> deletedList = new ArrayList<>();
        Mockito.when(repository.findAllById(Mockito.anyCollection())).thenReturn(deletedList);
        Mockito.doNothing().when(repository).deleteAll(Mockito.anyCollection());

        HoldingsForRequestExhibitionPromotionEntity newEntity = new HoldingsForRequestExhibitionPromotionEntity();
        newEntity.setCd(1L);
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(newEntity);
        List<HoldingsForRequestExhibitionPromotionEntity> updatedList = new ArrayList<>();
        HoldingsForRequestExhibitionPromotionEntity updated = new HoldingsForRequestExhibitionPromotionEntity();
        updated.setCd(1L);
        updatedList.add(updated);

        Mockito.when(repository.findAllById(Mockito.anyCollection())).thenReturn(updatedList);
        Mockito.when(mapper.toEntityUpdated(Mockito.any(), Mockito.any())).thenReturn(updated);
        Mockito.when(repository.saveAll(Mockito.anyCollection())).thenReturn(updatedList);
        RequestExhibitionPromotionsEntity requestExhibitionPromotionsEntity = new RequestExhibitionPromotionsEntity();
        ExhibitionPromotionsRequest exhibitionPromotionsRequest = new ExhibitionPromotionsRequest();
        List<HoldingsForRequestExhibitionPromotionRequest> holdingsForRequestExhibitionPromotionRequestList = new ArrayList<>();
        HoldingsForRequestExhibitionPromotionRequest holdingsForRequestExhibitionPromotionRequest = new HoldingsForRequestExhibitionPromotionRequest();
        holdingsForRequestExhibitionPromotionRequest.setCd(1L);
        holdingsForRequestExhibitionPromotionRequestList.add(holdingsForRequestExhibitionPromotionRequest);
        exhibitionPromotionsRequest.setHoldingsForRequestExhibitionPromotionRequestList(holdingsForRequestExhibitionPromotionRequestList);
        List<Long> implementationStoresForRequestMannequinPromotionRequestDeleted = new ArrayList<>(Arrays.asList(2L));
        exhibitionPromotionsRequest.setHoldingsForRequestExhibitionPromotionIsDeletedList(implementationStoresForRequestMannequinPromotionRequestDeleted);
        holdingsForRequestExhibitionPromotionService.setHoldingsForRequestExhibition(requestExhibitionPromotionsEntity, exhibitionPromotionsRequest);
    }
}
