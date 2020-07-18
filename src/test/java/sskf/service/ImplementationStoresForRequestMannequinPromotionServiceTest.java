package sskf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sskf.mapper.ImplementationStoresForRequestMannequinPromotionMapper;
import sskf.model.entity.ImplementationStoresForRequestMannequinPromotionEntity;
import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.request.ImplementationStoresForRequestMannequinPromotionRequest;
import sskf.model.request.MannequinPromotionsRequest;
import sskf.repository.ImplementationStoresForRequestMannequinPromotionRepository;
import sskf.service.impl.ImplementationStoresForRequestMannequinPromotionServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class ImplementationStoresForRequestMannequinPromotionServiceTest {

    @InjectMocks
    private ImplementationStoresForRequestMannequinPromotionServiceImpl implementationStoresForRequestMannequinPromotionService;

    @Mock
    private ImplementationStoresForRequestMannequinPromotionRepository repository;

    @Mock
    private ImplementationStoresForRequestMannequinPromotionMapper mapper;

    @Test
    public void setRequestMannequinPromotionDetailsForMannequinTest() {
        List<ImplementationStoresForRequestMannequinPromotionEntity> deletedList = new ArrayList<>();
        Mockito.when(repository.findAllById(Mockito.anyCollection())).thenReturn(deletedList);
        Mockito.doNothing().when(repository).deleteAll(Mockito.anyCollection());

        ImplementationStoresForRequestMannequinPromotionEntity newEntity = new ImplementationStoresForRequestMannequinPromotionEntity();
        newEntity.setCd(1L);
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(newEntity);
        List<ImplementationStoresForRequestMannequinPromotionEntity> updatedList = new ArrayList<>();
        ImplementationStoresForRequestMannequinPromotionEntity updated = new ImplementationStoresForRequestMannequinPromotionEntity();
        updated.setCd(1L);
        updatedList.add(updated);

        Mockito.when(repository.findAllById(Mockito.anyCollection())).thenReturn(updatedList);
        Mockito.when(mapper.toEntityUpdated(Mockito.any(), Mockito.any())).thenReturn(updated);
        Mockito.when(repository.saveAll(Mockito.anyCollection())).thenReturn(updatedList);
        RequestMannequinPromotionsEntity requestMannequinPromotionsEntity = new RequestMannequinPromotionsEntity();
        MannequinPromotionsRequest mannequinPromotionsRequest = new MannequinPromotionsRequest();
        List<ImplementationStoresForRequestMannequinPromotionRequest> implementationStoresForRequestMannequinPromotionRequests = new ArrayList<>();
        ImplementationStoresForRequestMannequinPromotionRequest implementationStoresForRequestMannequinPromotionRequest = new ImplementationStoresForRequestMannequinPromotionRequest();
        implementationStoresForRequestMannequinPromotionRequest.setCd(1L);
        implementationStoresForRequestMannequinPromotionRequests.add(implementationStoresForRequestMannequinPromotionRequest);
        mannequinPromotionsRequest.setImplementationStoresForRequestMannequinPromotionRequestList(implementationStoresForRequestMannequinPromotionRequests);
        List<Long> implementationStoresForRequestMannequinPromotionRequestDeleted = new ArrayList<>(Arrays.asList(2L));
        mannequinPromotionsRequest.setImplementationStoresForRequestMannequinPromotionRequestIsDeletedList(implementationStoresForRequestMannequinPromotionRequestDeleted);
        implementationStoresForRequestMannequinPromotionService.setImplementationStoresForMannequin(requestMannequinPromotionsEntity, mannequinPromotionsRequest);
    }
}
