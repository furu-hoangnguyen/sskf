package sskf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sskf.mapper.RequestMannequinPromotionDetailsMapper;
import sskf.model.entity.RequestMannequinPromotionDetailsEntity;
import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.request.MannequinPromotionsRequest;
import sskf.model.request.RequestMannequinPromotionDetailsRequest;
import sskf.repository.RequestMannequinPromotionDetailsRepository;
import sskf.service.impl.RequestMannequinPromotionDetailsServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class RequestMannequinPromotionDetailsServiceTest {

    @InjectMocks
    private RequestMannequinPromotionDetailsServiceImpl requestMannequinPromotionDetailsService;

    @Mock
    private RequestMannequinPromotionDetailsRepository requestMannequinPromotionDetailsRepository;

    @Mock
    private RequestMannequinPromotionDetailsMapper requestMannequinPromotionDetailsMapper;

    @Test
    public void setRequestMannequinPromotionDetailsForMannequinTest() {
        List<RequestMannequinPromotionDetailsEntity> deletedList = new ArrayList<>();
        Mockito.when(requestMannequinPromotionDetailsRepository.findAllById(Mockito.anyCollection())).thenReturn(deletedList);
        Mockito.doNothing().when(requestMannequinPromotionDetailsRepository).deleteAll(Mockito.anyCollection());

        RequestMannequinPromotionDetailsEntity newEntity = new RequestMannequinPromotionDetailsEntity();
        newEntity.setCd(1L);
        Mockito.when(requestMannequinPromotionDetailsMapper.toEntity(Mockito.any())).thenReturn(newEntity);
        List<RequestMannequinPromotionDetailsEntity> updatedList = new ArrayList<>();
        RequestMannequinPromotionDetailsEntity updated = new RequestMannequinPromotionDetailsEntity();
        updated.setCd(1L);
        updatedList.add(updated);

        Mockito.when(requestMannequinPromotionDetailsRepository.findAllById(Mockito.anyCollection())).thenReturn(updatedList);
        Mockito.when(requestMannequinPromotionDetailsMapper.toEntityUpdated(Mockito.any(), Mockito.any())).thenReturn(updated);
        Mockito.when(requestMannequinPromotionDetailsRepository.saveAll(Mockito.anyCollection())).thenReturn(updatedList);
        RequestMannequinPromotionsEntity requestMannequinPromotionsEntity = new RequestMannequinPromotionsEntity();
        MannequinPromotionsRequest mannequinPromotionsRequest = new MannequinPromotionsRequest();
        List<RequestMannequinPromotionDetailsRequest> requestMannequinPromotionDetailsRequestList = new ArrayList<>();
        RequestMannequinPromotionDetailsRequest requestMannequinPromotionDetailsRequest = new RequestMannequinPromotionDetailsRequest();
        requestMannequinPromotionDetailsRequest.setCd(1L);
        requestMannequinPromotionDetailsRequestList.add(requestMannequinPromotionDetailsRequest);
        mannequinPromotionsRequest.setRequestMannequinPromotionDetailsRequestList(requestMannequinPromotionDetailsRequestList);
        List<Long> requestMannequinPromotionDetailsRequestIsDeletedList = new ArrayList<>(Arrays.asList(2L));
        mannequinPromotionsRequest.setRequestMannequinPromotionDetailsRequestIsDeletedList(requestMannequinPromotionDetailsRequestIsDeletedList);
        requestMannequinPromotionDetailsService.setRequestMannequinPromotionDetailsForMannequin(requestMannequinPromotionsEntity, mannequinPromotionsRequest);
    }
}
