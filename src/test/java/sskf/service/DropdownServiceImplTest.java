package sskf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.Assert;
import sskf.common.Constants;
import sskf.common.enums.EnumTypeInput;
import sskf.mapper.MstBumonMapper;
import sskf.mapper.MstYakushokuMapper;
import sskf.mapper.ShainMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.MstHinmokuCategoriesEntity;
import sskf.model.entity.MstYakushokuEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.response.BumonResponse;
import sskf.model.response.MstStoreResponse;
import sskf.model.response.MstYakushokuResponse;
import sskf.model.response.ShainResponse;
import sskf.repository.BumonRepository;
import sskf.repository.MstHinmokuCategoriesRepository;
import sskf.repository.MstYakushokuRepository;
import sskf.repository.ShainRepository;
import sskf.service.impl.DropdownServiceImpl;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class DropdownServiceImplTest {

    @InjectMocks
    private DropdownServiceImpl dropdownService;

    @Mock
    private BumonRepository bumonRepository;

    @Mock
    private MstBumonMapper mstBumonMapper;

    @Mock
    private ShainRepository shainRepository;

    @Mock
    private ShainMapper shainMapper;

    @Mock
    private MstYakushokuRepository yakushokuRepository;

    @Mock
    private MstYakushokuMapper yakushokuMapper;

    @Mock
    private MstHinmokuCategoriesRepository mstHinmokuCategoriesRepository;

    @Mock
    private MstStoreServices mstStoreServices;

    @Mock
    private MstBumonService mstBumonService;

    @Test
    public void listDepartmentTest() {
        List<MstBumonEntity> bumonEntityList = new ArrayList<>();

        BumonResponse bumonResponse = new BumonResponse();

        Mockito.when(bumonRepository.findByBumonCdNkIsNotNull()).thenReturn(bumonEntityList);
        Mockito.when(mstBumonMapper.toResponse(Mockito.any())).thenReturn(bumonResponse);
        List<BumonResponse> actual = dropdownService.listDepartment();
        Assert.assertEquals(actual.size(), bumonEntityList.size());

    }

    @Test
    public void listApplicationTest() {
        String bumonCd = "bumonCd";
        BumonResponse bumonResponse = new BumonResponse();
        List<ShainEntity> shainEntities = new ArrayList<>();
        Mockito.when(shainRepository.findByShainCdNkIsNotNullAndMstBumonEntity_BumonCd(Mockito.anyString())).thenReturn(shainEntities);
        Mockito.when(mstBumonMapper.toResponse(Mockito.any())).thenReturn(bumonResponse);
        List<ShainResponse> actual = dropdownService.listApplication(bumonCd);
        Assert.assertEquals(actual.size(), shainEntities.size());
    }

    @Test
    public void listPaymentPlaceTest() {
        List<MstBumonEntity> mstBumonEntityList = new ArrayList<>();
        BumonResponse bumonResponse = new BumonResponse();
        Mockito.when(bumonRepository.findByBumonCdIn(Mockito.anyList())).thenReturn(mstBumonEntityList);
        Mockito.when(mstBumonMapper.toResponse(Mockito.any())).thenReturn(bumonResponse);
        List<BumonResponse> actual = dropdownService.listPaymentPlace();
        Assert.assertEquals(actual.size(), mstBumonEntityList.size());
    }

    @Test
    public void listPosition() {
        List<MstYakushokuEntity> positions = new ArrayList<>();
        Mockito.when(yakushokuRepository.findAllByIsDeletedIs(Constants.NOT_DELETED)).thenReturn(positions);
        MstYakushokuResponse mstYakushokuResponse = new MstYakushokuResponse();
        Mockito.when(yakushokuMapper.toResponse(Mockito.any())).thenReturn(mstYakushokuResponse);
        List<MstYakushokuResponse> actual = dropdownService.listPosition();
        Assert.assertEquals(actual.size(), positions.size());
    }


    @Test
    public void listCategoryName() {
        List<MstHinmokuCategoriesEntity> mstHinmokuCategoriesEntities = new ArrayList<>();
        Mockito.when(mstHinmokuCategoriesRepository.findByCategoryType(Mockito.anyByte())).thenReturn(mstHinmokuCategoriesEntities);

        List<String> actual = dropdownService.listCategoryName(EnumTypeInput.PRODUCT_CATEGORY);
        Assert.assertEquals(actual.size(), mstHinmokuCategoriesEntities.size());
    }

    @Test
    public void listCategoriesTest() {
        List<MstHinmokuCategoriesEntity> mstHinmokuCategoriesEntities = new ArrayList<>();
        Mockito.when(mstHinmokuCategoriesRepository.findAll()).thenReturn(mstHinmokuCategoriesEntities);
        List<MstHinmokuCategoriesEntity> actual = dropdownService.listCategories();
        Assert.assertEquals(actual.size(), mstHinmokuCategoriesEntities.size());
    }

    @Test
    public void listStoreGroupTest() {
        String storeGnmForSearch = "";
        List<MstStoreResponse> expect = new ArrayList<>();
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        Mockito.when(mstStoreServices.listStores(baseSearchRequest)).thenReturn(expect);
        List<MstStoreResponse> actual = dropdownService.listStoreGroup(storeGnmForSearch);
        Assert.assertEquals(actual.size(), expect.size());
    }

    @Test
    public void listDepartmentForItemTest() {
        String shainCd = "";
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        List<BumonResponse> expect = new ArrayList<>();
        Mockito.when(mstBumonService.listDepartmentForItem(baseSearchRequest)).thenReturn(expect);
        List<BumonResponse> actual = dropdownService.listDepartmentForItem(shainCd);
        Assert.assertEquals(actual.size(), expect.size());
    }
}
