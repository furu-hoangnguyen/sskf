package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseTimeModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "request_exhibition_promotions", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class RequestExhibitionPromotionsEntity extends BaseTimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "subject")
    private String subject;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "purpose_of_others")
    private String purposeOfOthers;

    @Column(name = "store_g_cd")
    private String storeGCd;

    @Column(name = "store_g_nm")
    private String storeGNm;

    @Column(name = "content_of_exhibition")
    private String contentOfExhibition;

    @Column(name = "year1")
    private Long year1;

    @Column(name = "sales1")
    private Long sales1;

    @Column(name = "initial_sales1")
    private Long initialSales1;

    @Column(name = "sales_year_on_year1")
    private BigDecimal salesYearOnYear1;

    @Column(name = "initial_sales_year_on_year1")
    private BigDecimal initialSalesYearOnYear1;

    @Column(name = "marginal_profit1")
    private Long marginalProfit1;

    @Column(name = "initial_marginal_profit1")
    private Long initialMarginalProfit1;

    @Column(name = "year2")
    private Long year2;

    @Column(name = "sales2")
    private Long sales2;

    @Column(name = "initial_sales2")
    private Long initialSales2;

    @Column(name = "sales_year_on_year2")
    private BigDecimal salesYearOnYear2;

    @Column(name = "initial_sales_year_on_year2")
    private BigDecimal initialSalesYearOnYear2;

    @Column(name = "marginal_profit2")
    private Long marginalProfit2;

    @Column(name = "initial_marginal_profit2")
    private Long initialMarginalProfit2;

    @Column(name = "year3")
    private Long year3;

    @Column(name = "sales3")
    private Long sales3;

    @Column(name = "initial_sales3")
    private Long initialSales3;

    @Column(name = "marginal_profit3")
    private BigDecimal marginalProfit3;

    @Column(name = "initial_marginal_profit3")
    private BigDecimal initialMarginalProfit3;

    @Column(name = "sales_year_on_year3")
    private Long salesYearOnYear3;

    @Column(name = "initial_sales_year_on_year3")
    private Long initialSalesYearOnYear3;

    @Column(name = "remarks")
    private String remarks;

    @JsonIgnore
    @OneToMany(mappedBy = "requestExhibitionPromotionsEntity", fetch = FetchType.LAZY)
    private Set<HoldingsForRequestExhibitionPromotionEntity> holdingsForRequestExhibitionPromotionEntities;

    @ManyToOne
    @JoinColumn(name = "request_cd")
    private RequestsEntity requestsEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestExhibitionPromotionsEntity that = (RequestExhibitionPromotionsEntity) o;
        return cd == that.cd &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(purpose, that.purpose) &&
                Objects.equals(purposeOfOthers, that.purposeOfOthers) &&
                Objects.equals(storeGCd, that.storeGCd) &&
                Objects.equals(storeGNm, that.storeGNm) &&
                Objects.equals(contentOfExhibition, that.contentOfExhibition) &&
                Objects.equals(year1, that.year1) &&
                Objects.equals(sales1, that.sales1) &&
                Objects.equals(initialSales1, that.initialSales1) &&
                Objects.equals(salesYearOnYear1, that.salesYearOnYear1) &&
                Objects.equals(initialSalesYearOnYear1, that.initialSalesYearOnYear1) &&
                Objects.equals(marginalProfit1, that.marginalProfit1) &&
                Objects.equals(initialMarginalProfit1, that.initialMarginalProfit1) &&
                Objects.equals(year2, that.year2) &&
                Objects.equals(sales2, that.sales2) &&
                Objects.equals(initialSales2, that.initialSales2) &&
                Objects.equals(salesYearOnYear2, that.salesYearOnYear2) &&
                Objects.equals(initialSalesYearOnYear2, that.initialSalesYearOnYear2) &&
                Objects.equals(marginalProfit2, that.marginalProfit2) &&
                Objects.equals(initialMarginalProfit2, that.initialMarginalProfit2) &&
                Objects.equals(year3, that.year3) &&
                Objects.equals(sales3, that.sales3) &&
                Objects.equals(initialSales3, that.initialSales3) &&
                Objects.equals(marginalProfit3, that.marginalProfit3) &&
                Objects.equals(initialMarginalProfit3, that.initialMarginalProfit3) &&
                Objects.equals(salesYearOnYear3, that.salesYearOnYear3) &&
                Objects.equals(initialSalesYearOnYear3, that.initialSalesYearOnYear3) &&
                Objects.equals(remarks, that.remarks) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, subject, purpose, purposeOfOthers, storeGCd, storeGNm, contentOfExhibition, year1, sales1, initialSales1, salesYearOnYear1, initialSalesYearOnYear1, marginalProfit1, initialMarginalProfit1, year2, sales2, initialSales2, salesYearOnYear2, initialSalesYearOnYear2, marginalProfit2, initialMarginalProfit2, year3, sales3, initialSales3, marginalProfit3, initialMarginalProfit3, salesYearOnYear3, initialSalesYearOnYear3, remarks, createdAt, updatedAt);
    }
}
